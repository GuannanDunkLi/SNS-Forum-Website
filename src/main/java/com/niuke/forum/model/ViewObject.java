package com.niuke.forum.model;

import java.util.HashMap;
import java.util.Map;

// 用来传递freemarker与Controller之间的参数的
public class ViewObject {
    private Map<String, Object> objs = new HashMap<>();

     public void set(String key, Object values){
         objs.put(key, values);
     }

     public Object get(String key){
         return objs.get(key);
     }
}
