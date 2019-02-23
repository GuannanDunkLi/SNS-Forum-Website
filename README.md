#SNS+社区问答类网站

Springboot+Mybatis+Thymeleaf开发，数据库使用Mysql+redis，异步框架处理消息推送，同时使用了Pyspider爬虫进行网站内容数据填充。

***

**功能**

[1. 用户注册登录管理](#用户注册登录管理)

[2. 问题发布，敏感词过滤，问题广场](#问题发布，敏感词过滤，问题广场)

[3. 评论中心，站内信](#评论中心，站内信)

[4. Redis实现赞踩功能](#Redis实现赞踩功能)

[5. 异步设计](#异步设计)

[6. sns关注功能，关注和粉丝列表页实现](#sns关注功能，关注和粉丝列表页实现)

[7. timeline（新鲜事）实现](#timeline（新鲜事）实现)

[8. python爬虫实现数据抓取和导入](#python爬虫实现数据抓取和导入)

[9. 站内全文搜索](#站内全文搜索)

***

## 用户注册登录管理

![img](https://github.com/GuannanDunkLi/forum/blob/master/src/main/resources/static/images/img/log.png)
![img](https://github.com/GuannanDunkLi/forum/blob/master/src/main/resources/static/images/img/navigator1.png)
![img](https://github.com/GuannanDunkLi/forum/blob/master/src/main/resources/static/images/img/navigator2.png)
![img](https://github.com/GuannanDunkLi/forum/blob/master/src/main/resources/static/images/img/navigator3.png)

## 问题发布，敏感词过滤，问题广场

![img](https://github.com/GuannanDunkLi/forum/blob/master/src/main/resources/static/images/img/question.png)
![img](https://github.com/GuannanDunkLi/forum/blob/master/src/main/resources/static/images/img/sensitive.png)
![img](https://github.com/GuannanDunkLi/forum/blob/master/src/main/resources/static/images/img/page.png)

## 评论中心，站内信

![img](https://github.com/GuannanDunkLi/forum/blob/master/src/main/resources/static/images/img/comment.png)
![img](https://github.com/GuannanDunkLi/forum/blob/master/src/main/resources/static/images/img/message.png)
![img](https://github.com/GuannanDunkLi/forum/blob/master/src/main/resources/static/images/img/message1.png)

## Redis实现赞踩功能

![img](https://github.com/GuannanDunkLi/forum/blob/master/src/main/resources/static/images/img/like.png)

## 异步设计

![img](https://github.com/GuannanDunkLi/forum/blob/master/src/main/resources/static/images/img/asynchronous.jpg)

## sns关注功能，关注和粉丝列表页实现

![img](https://github.com/GuannanDunkLi/forum/blob/master/src/main/resources/static/images/img/profile.png)
![img](https://github.com/GuannanDunkLi/forum/blob/master/src/main/resources/static/images/img/follow.png)
![img](https://github.com/GuannanDunkLi/forum/blob/master/src/main/resources/static/images/img/followQuestion.png)

## timeline（新鲜事）实现

![img](https://github.com/GuannanDunkLi/forum/blob/master/src/main/resources/static/images/img/feed.png)

## python爬虫实现数据抓取和导入

现有数据使用Pyspider爬取自[https://www.v2ex.com/](https://www.v2ex.com/)  Pyspider代码见：[Pyspider代码](https://github.com/GuannanDunkLi/forum/blob/master/src/main/resources/Spider.py)

## 站内全文搜索

![img](https://github.com/GuannanDunkLi/forum/blob/master/src/main/resources/static/images/img/search.png)


