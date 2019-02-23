#!/usr/bin/env python
# -*- encoding: utf-8 -*-
# Created on 2019-02-21 19:38:47
# Project: v2ex

from pyspider.libs.base_handler import *
import pymysql
import random

class Handler(BaseHandler):
    crawl_config = {
    }

    def __init__(self):
        # 数据库连接所需参数
        self.db = pymysql.connect(user='root', passwd='', host='localhost', port=3306, db='forum', charset='utf8')
    
    def add_question(self, title, content):
        try:
            cursor = self.db.cursor()
            sql = 'insert into question(title, content, user_id, created_date, comment_count) values ("%s" ,"%s" ,%d, now(), 0)' % (title, content, random.randint(2,10))
            print sql
            cursor.execute(sql)
            print cursor.lastrowid
            self.db.commit()
        except Exception, e:
            print e
            self.db.rollback()
    
    @every(minutes=24 * 60)
    def on_start(self):
        self.crawl('https://www.v2ex.com', callback=self.index_page, validate_cert=False)

    @config(age=10 * 24 * 60 * 60) # 这表示我们认为 10 天内页面有效,不会再次进行更新抓取
    def index_page(self, response):
        for each in response.doc('a[href^="https://www.v2ex.com/?tab="]').items():
            self.crawl(each.attr.href, callback=self.tab_page, validate_cert=False)

    @config(priority=2)
    def tab_page(self, response):
        for each in response.doc('a[href^="https://www.v2ex.com/go/"]').items():
            self.crawl(each.attr.href, callback=self.board_page, validate_cert=False)
            
    @config(priority=2)
    def board_page(self, response):
        for each in response.doc('a[href^="https://www.v2ex.com/t/"]').items():
            url = each.attr.href
            if url.find('#reply') > 0:
                url = url[0:url.find('#')]
            self.crawl(url, callback=self.detail_page, validate_cert=False) 
        for each in response.doc('a.page_normal').items():
            self.crawl(each.attr.href, callback=self.board_page, validate_cert=False)
    
    @config(priority=2)
    def detail_page(self, response):
        title = response.doc('h1').text()
        content = response.doc('div.topic_content').html().replace('"','\\"')
        self.add_question(title,content)
        return {
            "url": response.url,
            "title": response.doc('title').text(),
        }
