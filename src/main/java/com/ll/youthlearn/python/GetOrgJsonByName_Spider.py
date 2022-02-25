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
                dataList.append(i)
            else:
                # 读取的是user信息
                pass

if __name__=="__main__":

    t1=time.time()

    headers={
        "User-Agent":"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36 NetType/WIFI MicroMessenger/7.0.20.1781(0x6700143B) WindowsWechat(0x6304051b)"
    }


    t2=time.time()
    # 数据库连接
    conn = getConn()
    cursor=conn.cursor()
    sql = ' select stage from t_stage order by mid(stage,13,20)+1 desc limit 0,35'
    cursor.execute(sql)
    allStage=cursor.fetchall()
    # print("获取stage耗时:",time.time()-t2)

    #获取java传来的值
    # orgName=sys.argv[1]
    orgNames=eval(sys.argv[1])
    # '["团省委","直属单位"]'

    #遍历结果,多线程启动爬虫
    resultDict={}
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

    for org in dataList:
        resultDict[list(org.values())[0]]=0

    for key in resultDict.keys():
        print(key)

    # print("耗时:", time.time()-t1)
    # print('===========================++++++++===========================')

