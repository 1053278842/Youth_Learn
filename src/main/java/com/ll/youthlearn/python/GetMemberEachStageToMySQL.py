import threading
from turtle import stamp
from typing import final
from pymysql import NULL
import requests
import pymysql
from ast import If, Return
from hashlib import new
from itertools import count
import json
import multiprocessing
from multiprocessing import Manager, Pool, Process, Queue
import os
import random
import time
from urllib import response
import urllib.parse
import requests
import sys
import datetime



def getConn():
    return pymysql.Connect(host='110.42.155.172',port=3306,user='root',password='LiuLong123123+',db='youth_learn')

def getOrgByNameStage(orgNames,stage,dataList):
    # url='http://dxx.ahyouth.org.cn/api/peopleRankStage?table_name=%s&level1=%s'%(stage,urllib.parse.quote(orgNames))
    url='http://dxx.ahyouth.org.cn/api/peopleRankStage?table_name=%s'%(stage)
    for i in range(len(orgNames)):
        url+="&level%s=%s"%(i+1,urllib.parse.quote(orgNames[i]))
    response=requests.get(url=url,headers=headers)
    pageJson=json.loads(response.content.decode())
    # print(urllib.parse.unquote_plus(url))
    # print(url)
    if(len(pageJson['list']['list'])!=0):
        for i in pageJson['list']['list']:
            if(list(i.keys())[0]!="username"):
                # dataList.append(i)
                pass
            else:
                # 读取的是user信息
                i['stage']=pageJson['list']['title']
                dataList.append(i)


if __name__=="__main__":
    
    # 获取java传来的值
    userId=sys.argv[1]
    orgNames=eval(sys.argv[2])
    maxStage=sys.argv[3]
    pathId=sys.argv[4]
    # orgNames=eval("['系统团委','安徽省消防救援总队团委','黄山市消防救援支队团委','屯光特勤站团支部']")
    # maxStage=35
    # userId=1
    # pathId=9


    t1=time.time()
    t2=time.time()
    headers={
        "User-Agent":"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36 NetType/WIFI MicroMessenger/7.0.20.1781(0x6700143B) WindowsWechat(0x6304051b)"
    }  

    # 数据库连接
    t2=time.time()
    conn = getConn()
    cursor=conn.cursor()
    sql = 'select stage,id from t_stage ORDER BY t_stage.stage_date DESC, t_stage.stage DESC limit 0,'+str(maxStage)
    cursor.execute(sql)
    allStage=cursor.fetchall()
    allStageDict={}
    for s in allStage:
        s_name=s[0]
        s_id=s[1]
        allStageDict[s_name]=s_id
    print("获取stage耗时:",time.time()-t2)


    #遍历结果,多线程启动爬虫
    t2=time.time()
    dataList=[]
    #线程池
    threads = []
    for raw in allStage:
        stage=raw[0]
        t=threading.Thread(target=getOrgByNameStage,args=(orgNames,stage,dataList))
        threads.append(t)
    
    # 开启新线程
    for t in threads:
        t.start()

    # 等待所有线程完成
    for t in threads:
        t.join()
    print("爬虫耗时:", time.time()-t2)  

    # for org in dataList:
    #     print(org)

    # 数据库连接
    t2=time.time()
    cursor=conn.cursor()
    sql = 'select name,id from t_member where parent_user_id ='+str(userId)+" AND path_id="+str(pathId)
    cursor.execute(sql)
    fixedMember=cursor.fetchall()
    fixedMemberDict={}
    for m in fixedMember:
        m_name=m[0]
        m_id=m[1]
        fixedMemberDict[m_name]=m_id
    print("获取固定成员组耗时:",time.time()-t2)

    # 拼接SQL语句，批量插入member
    t2=time.time()
    cursor=conn.cursor()
    sql = 'insert into t_member_each_stage (member_id,timestamp,stage_id,user_id) values '
    for member in dataList:
        # 数据不属于用户预先规划好的时,放弃存储该条数据.后期要修改为新用户自动添加到member中，并建立each关系
        if member['username'] not in fixedMemberDict.keys():
            continue
        if member['stage'] not in allStageDict.keys():
            continue

        member_id=fixedMemberDict[member['username']]
        addTime=member['addtime']
        stageId = allStageDict[member['stage']]
        # print(org['addtime'],org['username'])
        #TODO 这里需要改一下这个1900-01-01的固定值，可能又隐患bug
        if addTime is None:
            stamptime=datetime.datetime.strptime("1900-01-01","%Y-%m-%d")
        else:
            stamptime=datetime.datetime.strptime(addTime,"%Y-%m-%d")
        # print(stamptime)
        sql+='("%s","%s","%s","%s"),'%(
            member_id,stamptime,stageId,userId
        )
    # 去掉最后一个','!
    sql=sql[0:-1]
    sql+=(" on duplicate key update member_id=member_id")
    print("拼接SQL耗时:", time.time()-t2)  

    # 执行事务
    t2=time.time()
    try:
        cursor.execute(sql)
        conn.commit()
    except Exception as e:
        print(e)
        conn.rollback()
    print("Commit耗时:", time.time()-t2)  
    

    print("总耗时:", time.time()-t1)
    print('===========================++++++++===========================')

