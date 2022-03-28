import threading
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

#再写一个脚本,判断当前路径子路径是否在两个以内。即可除去对memberjson的判断！！

def getConn():
    return pymysql.Connect(host='110.42.155.172',port=3306,user='root',password='LiuLong123123+',db='youth_learn')

def getOrgByNameStage(orgName,stage,dataList):
    # url='http://dxx.ahyouth.org.cn/api/peopleRankStage?table_name=%s&level1=%s'%(stage,urllib.parse.quote(orgNames))
    url='http://dxx.ahyouth.org.cn/api/peopleRankStage?table_name=%s'%(stage)
    for i in range(len(orgName)):
        url+="&level%s=%s"%(i+1,urllib.parse.quote(orgName[i]))
    response=requests.get(url=url,headers=headers)
    pageJson=json.loads(response.content.decode())
    # print(urllib.parse.unquote_plus(url))
    # print(url)
    if(len(pageJson['list']['list'])!=0):
        for i in pageJson['list']['list']:
            if(list(i.keys())[0]!="username"):
                dataList.append(i)
                break
            else:
                # 读取的是user信息
                break

if __name__=="__main__":
    # 是否打印时间
    TESTPRINT=True
    TESTPRINT=False

    if TESTPRINT:print('===========================++++++++===========================')
    if TESTPRINT:t1=time.time()

    headers={
        "User-Agent":"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36 NetType/WIFI MicroMessenger/7.0.20.1781(0x6700143B) WindowsWechat(0x6304051b)"
    }


    if TESTPRINT:t2=time.time()
    # 数据库连接
    conn = getConn()
    cursor=conn.cursor()
    sql = ' select stage from t_stage order by id asc limit 0,1'
    cursor.execute(sql)
    allStage=cursor.fetchall()
    if TESTPRINT:print("获取stage耗时:",time.time()-t2)

    #获取java传来的值
    # '["团省委","直属单位"]'
    orgNames=eval(sys.argv[1])
    # orgNames=["地市"]

    availableFlag=False
    if TESTPRINT:print(orgNames)
    for i in range(3):
        #遍历结果,多线程启动爬虫
        dataList=[]

        for raw in allStage:
            stage=raw[0]
            getOrgByNameStage(orgNames,stage,dataList)


        if(dataList==[]):
            availableFlag=True
            break

        for org in dataList:
            orgNames.append(list(org.values())[0])
        if TESTPRINT:print(orgNames)

    print(availableFlag)



    if TESTPRINT:print("耗时:", time.time()-t1)
    if TESTPRINT:print('===========================++++++++===========================')

