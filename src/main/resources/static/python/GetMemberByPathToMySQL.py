#! /usr/bin/env python3
import threading
import requests
import pymysql
import json
import time
import urllib.parse
import requests
import sys
import datetime
import io
import sys

sys.stderr = io.TextIOWrapper(sys.stderr.buffer, encoding='utf-8')
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')


def getConn():
    return pymysql.Connect(host='110.42.155.172',port=3306,user='root',password='xxx',db='youth_learn')

def getOrgByNameStage(orgNames,stage,dataList,titleList):
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
            if("username" not in i.keys()):
                # dataList.append(i)
                pass
            else:
                # 读取的是user信息
                dataList.append(i)
                titleList.append(pageJson['list']['title'])


if __name__=="__main__":
    print("获取成员信息脚本启动2.0!")
    # 获取java传来的值
    # userId=9
    # orgNames=eval("['团省委', '机关', '学校部']")
    # maxStage=80
    # orgNames=eval("['直属高校', '合肥学院', '人工智能与大数据学院','2019级信息管理与信息系统(对口)班']")
    userId=sys.argv[1]
    orgNames=eval(sys.argv[2])
    maxStage=sys.argv[3]
    pathId=sys.argv[4]

    # print(userId," | ",orgNames," | ",maxStage," | ",pathId)

    t1=time.time()
    t2=time.time()
    headers={
        "User-Agent":"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36 NetType/WIFI MicroMessenger/7.0.20.1781(0x6700143B) WindowsWechat(0x6304051b)"
    }

    # 数据库连接
    conn = getConn()
    cursor=conn.cursor()
    sql = 'select stage from t_stage ORDER BY t_stage.stage_date DESC, t_stage.stage DESC limit 0,'+str(maxStage)
    cursor.execute(sql)
    allStage=cursor.fetchall()



    #遍历结果,多线程启动爬虫
    dataList=[]
    titleList=[]
    #线程池
    threads = []
    for raw in allStage:
        stage=raw[0]
        t=threading.Thread(target=getOrgByNameStage,args=(orgNames,stage,dataList,titleList))
        threads.append(t)

    # 开启新线程
    for t in threads:
        t.start()

    # 等待所有线程完成
    for t in threads:
        t.join()
    print("爬虫耗时:", time.time()-t1)

    # 去重计次重构
    t2=time.time()
    finalDict={}
    for org in dataList:
        if(finalDict.__contains__(org['username'])):
            finalDict[org['username']]=[finalDict[org['username']][0],finalDict[org['username']][1]+1]
        else:
            finalDict[org['username']]=[org['addtime'],1]
    print("去重计次耗时:", time.time()-t2)


    # 拼接SQL语句，批量插入member
    t2=time.time()
    cursor=conn.cursor()
    sql = 'insert into t_member (name,timestamp,path,parent_user_id,path_id) values '
    for key,value in finalDict.items():
        userName=key
        addTime=value[0]
        times=value[1]
        # print("测试用1："+userName)
        # print("测试用1："+addTime)
        # print("测试用1："+times)
        # print(org['addtime'],org['username'])
        if addTime is None:
            stamptime=datetime.datetime.strptime("2000-01-01","%Y-%m-%d")
        else:
            stamptime=datetime.datetime.strptime(addTime,"%Y-%m-%d")
        # print(stamptime)
        sql+='("%s","%s","%s","%s","%s"),'%(
            userName,stamptime,orgNames,userId,pathId
        )
    # 去掉最后一个','!
    sql=sql[0:-1]
    sql+=(" on duplicate key update timestamp=timestamp")
    print("拼接SQL耗时:", time.time()-t2)
    # print(sql)

    #执行事务
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

