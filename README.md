# 低仿知乎类SNS+社区问答网站(牛客网高级项目) / [English version](https://github.com/GuannanDunkLi/forum/blob/master/English.md)

Springboot+Mybatis+Thymeleaf开发，数据库使用Mysql+redis，异步框架处理消息推送，同时使用了Pyspider爬虫进行网站内容数据填充。

***

**功能**

[1. 用户注册登录管理](#用户注册登录管理)

[2. 问题管理](#问题管理)

[3. 评论中心与站内信](#评论中心与站内信)

[4. Redis实现赞踩功能](#Redis实现赞踩功能)

[5. 异步设计](#异步设计)

[6. sns关注功能和粉丝列表页实现](#sns关注功能和粉丝列表页实现)

[7. timeline新鲜事实现](#timeline新鲜事实现)

[8. python爬虫实现数据抓取和导入](#python爬虫实现数据抓取和导入)

[9. 站内全文搜索](#站内全文搜索)

***

## 用户注册登录管理

**登陆界面：**

![img](https://github.com/GuannanDunkLi/forum/blob/master/src/main/resources/static/images/img/log.png)

**导航栏（登陆前）：**

![img](https://github.com/GuannanDunkLi/forum/blob/master/src/main/resources/static/images/img/navigator1.png)

**导航栏（登陆后）：**

![img](https://github.com/GuannanDunkLi/forum/blob/master/src/main/resources/static/images/img/navigator2.png)

**个人信息导航：**

![img](https://github.com/GuannanDunkLi/forum/blob/master/src/main/resources/static/images/img/navigator3.png)

## 问题管理

**问题发布：**

![img](https://github.com/GuannanDunkLi/forum/blob/master/src/main/resources/static/images/img/question.png)

**敏感词过滤（内容已被过滤）：**

![img](https://github.com/GuannanDunkLi/forum/blob/master/src/main/resources/static/images/img/sensitive.png)

**问题广场（首页显示）：**

![img](https://github.com/GuannanDunkLi/forum/blob/master/src/main/resources/static/images/img/page.png)

## 评论中心与站内信

**评论页面：**

![img](https://github.com/GuannanDunkLi/forum/blob/master/src/main/resources/static/images/img/comment.png)

**个人站内信：**

![img](https://github.com/GuannanDunkLi/forum/blob/master/src/main/resources/static/images/img/message.png)

**站内信详情：**

![img](https://github.com/GuannanDunkLi/forum/blob/master/src/main/resources/static/images/img/message1.png)

## Redis实现赞踩功能

**评论的赞踩：**

![img](https://github.com/GuannanDunkLi/forum/blob/master/src/main/resources/static/images/img/like.png)

## 异步设计

**异步设计原理：**

![img](https://github.com/GuannanDunkLi/forum/blob/master/src/main/resources/static/images/img/asynchronous.jpg)

## sns关注功能和粉丝列表页实现

**用户关注：**

![img](https://github.com/GuannanDunkLi/forum/blob/master/src/main/resources/static/images/img/profile.png)

**关注列表：**

![img](https://github.com/GuannanDunkLi/forum/blob/master/src/main/resources/static/images/img/follow.png)

**问题关注：**

![img](https://github.com/GuannanDunkLi/forum/blob/master/src/main/resources/static/images/img/followQuestion.png)

## timeline新鲜事实现

**新鲜事（目前只有关注、评论问题被列为新鲜事）：**

![img](https://github.com/GuannanDunkLi/forum/blob/master/src/main/resources/static/images/img/feed.png)

## python爬虫实现数据抓取和导入

现有数据使用Pyspider爬取自[https://www.v2ex.com/](https://www.v2ex.com/)  Pyspider代码见：[Pyspider代码](https://github.com/GuannanDunkLi/forum/blob/master/src/main/resources/Spider.py)

## 站内全文搜索

**全文搜索（ElasticSearch）：**

![img](https://github.com/GuannanDunkLi/forum/blob/master/src/main/resources/static/images/img/search.png)


