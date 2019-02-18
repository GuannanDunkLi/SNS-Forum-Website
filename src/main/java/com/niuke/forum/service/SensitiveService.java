package com.niuke.forum.service;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class SensitiveService implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);
    // 默认敏感词替换符
    private static final String DEFAULT_REPLACEMENT = "敏感词";

    // 前缀树节点
    private class TrieNode {
        // 是不是敏感词的结尾
        private boolean end = false;
        // 当前节点下所有的子节点
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        // 添加节点
        void addSubNode(Character key, TrieNode node) {
            subNodes.put(key, node);
        }

        // 获取子节点
        TrieNode getSubNode(Character key) {
            return subNodes.get(key);
        }

        // 判断是不是结尾
        boolean isKeywordEnd() {
            return end;
        }

        // 初始化结尾
        void setKeywordEnd(boolean end) {
            this.end = end;
        }

        public int getSubNodeCount() {
            return subNodes.size();
        }
    }

    // 根节点
    private TrieNode rootNode = new TrieNode();

    // 判断是否是一个符号
    private boolean isSymbol(char c) {
        int ic = (int) c;
        // 0x2E80-0x9FFF 东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }

    // 过滤敏感词
    public String filter(String text) {
        // 空字符串不用过滤
        if (StringUtils.isBlank(text)) {
            return text;
        }
        String replacement = DEFAULT_REPLACEMENT;
        StringBuilder result = new StringBuilder();
        TrieNode tempNode = rootNode;
        int begin = 0; // 回滚数
        int position = 0; // 当前比较的位置
        while (position < text.length()) {
            char c = text.charAt(position);
            // 特殊符号直接跳过
            if (isSymbol(c)) {
                if (tempNode == rootNode) {
                    result.append(c);
                    ++begin;
                }
                ++position;
                continue;
            }
            tempNode = tempNode.getSubNode(c);
            // 当前位置的匹配结束，没有找到敏感词
            if (tempNode == null) {
                // 以begin开始的字符串不存在敏感词
                result.append(text.charAt(begin));
                // 跳到下一个字符开始测试
                position = begin + 1;
                begin = position;
                // 回到树初始节点
                tempNode = rootNode;
            } else if (tempNode.isKeywordEnd()) {
                // 发现敏感词，从begin到position的位置用replacement替换掉
                result.append(replacement);
                position = position + 1;
                begin = position;
                tempNode = rootNode;
            } else {
                ++position;
            }
        }
        return result.toString();
    }

    // 添加敏感词到树里
    private void addWord(String lineTxt) {
        // 指向根节点
        TrieNode tempNode = rootNode;
        // 循环每个字节
        for (int i = 0; i < lineTxt.length(); i++) {
            Character c = lineTxt.charAt(i);
            // 过滤特殊符号
            if (isSymbol(c)) {
                continue;
            }
            TrieNode node = tempNode.getSubNode(c);
            if (node == null) { // 没初始化
                node = new TrieNode();
                tempNode.addSubNode(c, node);
            }
            // 指向子节点
            tempNode = node;
            if (i == lineTxt.length() - 1) {
                // 关键词结束，设置结束标志
                tempNode.setKeywordEnd(true);
            }
        }
    }

    // 读取敏感词
    @Override
    public void afterPropertiesSet() {
        rootNode = new TrieNode();
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader read = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                lineTxt = lineTxt.trim(); // 前后空格处理下
                addWord(lineTxt);
            }
            read.close();
        } catch (Exception e) {
            logger.error("读取敏感词文件失败" + e.getMessage());
        }
    }
}