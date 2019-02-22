package com.niuke.forum.async;

import com.alibaba.fastjson.JSON;
import com.niuke.forum.utils.JedisAdapter;
import com.niuke.forum.utils.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {
    @Autowired
    JedisAdapter jedisAdapter;

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    // 通过map实现的最简单的消息分发
    private Map<EventType, List<EventHandler>> config = new HashMap<>();
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {
        // 找到EventHandler所有的实现类
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (beans != null) {
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()) {
                // 每个EventHandler支持的EventTypes
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();
                // 找到每个EventType对应的EventHandler，保存在config里
                for (EventType type : eventTypes) {
                    if (!config.containsKey(type)) {
                        config.put(type, new ArrayList<>());
                    }
                    config.get(type).add(entry.getValue());
                }
            }
        }
        // 线程会一直去取队列里的值
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String key = RedisKeyUtil.getEventQueueKey();
                    // 获取list最右边的值（先进先出），如果没有值会一直阻塞知道有值可弹出
                    List<String> events = jedisAdapter.brpop(0, key);
                    for (String message : events) {
                        // 前面brpop的返回值第一个值是key，应该过滤掉
                        if (message.equals(key)) {
                            continue;
                        }
                        // 解析message，找到EventModel
                        EventModel eventModel = JSON.parseObject(message, EventModel.class);
                        if (!config.containsKey(eventModel.getType())) {
                            System.out.println(eventModel.getType());
                            logger.error("不能识别的事件");
                            continue;
                        }
                        // 找到handler list，一个个去处理这个event
                        for (EventHandler handler : config.get(eventModel.getType())) {
                            handler.doHandle(eventModel);
                        }
                    }
                }
            }
        });
        thread.start();
    }
}