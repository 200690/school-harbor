package com.harbor.utils;

import java.util.HashSet;
import java.util.Set;

/**
 * 敏感词过滤工具类
 * 提供敏感词检测和替换功能
 */
public class SensitiveWordFilter {

    /**
     * 敏感词集合
     */
    private static final Set<String> SENSITIVE_WORDS = new HashSet<>();

    /**
     * 替换字符
     */
    private static final char REPLACEMENT = '*';

    static {
        initSensitiveWords();
    }

    /**
     * 初始化敏感词库
     */
    private static void initSensitiveWords() {
        // 示例敏感词列表，实际项目中应该从数据库或配置文件加载
        String[] words = {
            "badword", "test", "脏话", "敏感词", "sb", "你妈"
        };
        for (String word : words) {
            SENSITIVE_WORDS.add(word);
        }
    }

    /**
     * 过滤文本中的敏感词，将其替换为*
     * @param text 原始文本
     * @return 过滤后的文本
     */
    public static String filter(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder result = new StringBuilder(text);
        for (String word : SENSITIVE_WORDS) {
            if (text.contains(word)) {
                // 将敏感词替换为等长的*
                String replacement = String.valueOf(REPLACEMENT).repeat(word.length());
                result = new StringBuilder(result.toString().replace(word, replacement));
            }
        }
        return result.toString();
    }

    /**
     * 检查文本是否包含敏感词
     * @param text 原始文本
     * @return true表示包含敏感词
     */
    public static boolean containsSensitiveWord(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        for (String word : SENSITIVE_WORDS) {
            if (text.contains(word)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 添加敏感词到词库
     * @param word 敏感词
     */
    public static void addSensitiveWord(String word) {
        SENSITIVE_WORDS.add(word);
    }

    /**
     * 从词库移除敏感词
     * @param word 敏感词
     */
    public static void removeSensitiveWord(String word) {
        SENSITIVE_WORDS.remove(word);
    }
}