from tkinter.messagebox import QUESTION
import selenium
from selenium import webdriver
from selenium.webdriver.support import ui
import time
from selenium.webdriver.common.action_chains import ActionChains
from webdriver_manager.chrome import ChromeDriverManager
import random
import string
from multiprocessing import Process

def process():
    wait_time = 180
    runtime = 300

    # 1.打开浏览器，输入网址
    browser = webdriver.Chrome(ChromeDriverManager().install())
    browser.maximize_window() # 浏览器最大化
    browser.get('https://jinshuju.net/f/ocKDEt')
    browser.implicitly_wait(30) # 加入隐式等待，防止崩溃

    for i in range(0,500):

        # 获取类型-file1
        file1_label_objs=[]
        file1_label_objs=browser.find_elements_by_xpath('//div[@data-api-code="field_1"]//span[@class="other-choice-option-name"]/parent::*')   
        # 从0~max 范围内随机选其一个下表
        temp_max_length=len(file1_label_objs)
        temp_index=random.randint(0,temp_max_length-1)
        # 点击
        browser.execute_script("arguments[0].click();", file1_label_objs[temp_index])

        # 获取类型【file2-field_】
        for field__index in [2,3,4,6]:
            xpathStr='//input[@name="field_'+str(field__index)+'"]'
            file_input_objs=browser.find_element_by_xpath(xpathStr)   
            # 填充乱码
            key_value_str = ''.join(random.sample(string.ascii_letters + string.digits + string.punctuation, 64))
            file_input_objs.send_keys(key_value_str)

        # 模拟真实沉睡
        random_sleep_time=random.randint(40,150)
        print("第",i+1,"次模拟沉睡开始:",time.asctime( time.localtime(time.time()) ),",预计睡眠时间(s):",random_sleep_time)
        time.sleep(random_sleep_time)

        # 提交
        submit_button_obj=browser.find_element_by_xpath("//button")
        browser.execute_script("arguments[0].click();",submit_button_obj )

        #删除所有cookie
        browser.delete_all_cookies()

        #打印当前循环次数
        browser.refresh()
        #删除所有cookie
        browser.delete_all_cookies()
        browser.refresh()

if __name__=='__main__':
    process_list=[]
    for i in range(0,1):
        temp_p=Process(target=process,args=())
        temp_p.start()
        process_list.append(temp_p)
    
    for p in process_list:
        p.join()
    
    print("测试完成!")
