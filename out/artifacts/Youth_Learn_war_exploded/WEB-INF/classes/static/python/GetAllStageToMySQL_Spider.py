#! /usr/bin/env python3
import requests
import pymysql
import json
from urllib import response
import requests

if __name__=="__main__":
    url='http://dxx.ahyouth.org.cn/api/peopleRankList?level1=%E5%9B%A2%E7%9C%81%E5%A7%94'

    headers={
        # "Host":"dxx.ahyouth.org.cn",
        # "Referer": "http://dxx.ahyouth.org.cn/",
        "User-Agent":"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36 NetType/WIFI MicroMessenger/7.0.20.1781(0x6700143B) WindowsWechat(0x6304051b)"
    }  

    response=requests.get(url=url,headers=headers)
    pageJson=json.loads(response.content.decode())
    dataList=pageJson['list']

    # 数据库连接
    conn = pymysql.Connect(host='110.42.155.172',port=3306,user='root',password="LiuLong123123+",db='youth_learn')
    cursor=conn.cursor()
    sql = 'insert into t_stage (name,stage) values '
    for stage in dataList:
        sql+='("%s","%s"),'%(stage['name'],stage['table_name'])
    # 去掉最后一个','!
    sql=sql[0:-1]
    sql+=" on duplicate key update name = values(name)"

    #执行事务
    try:
        cursor.execute(sql)
        conn.commit()
    except Exception as e:
        print(e)
        conn.rollback()