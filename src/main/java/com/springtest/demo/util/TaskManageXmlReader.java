package com.springtest.demo.util;

import com.springtest.demo.entity.TaskManageTemplateVo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务解析工具类，建议放到CloudDesign微服务中，用于其他微服务引用
 */
public class TaskManageXmlReader {
    // 缓存解析结果，减少文件IO
    static ConcurrentHashMap<String, Map<String, List<TaskManageTemplateVo>>> cache;
    static {
        cache = new ConcurrentHashMap<>();
    }

    // 构造函数私有化，防止被实例化
    private TaskManageXmlReader(){}

    /**
     * 传入xml的配置路径，找到xml的解析结果
     * @param xmlPath xml的文件路径
     * @return xml的解析结果<tag, List<Vo>>
     */
    public static Map<String, List<TaskManageTemplateVo>> getTaskManageXmlResult(String xmlPath) {
        if(cache.containsKey(xmlPath)) {
            return cache.get(xmlPath);
        }
        // TODO 解析XML

        // TODO return解析之前，记得将解析结果缓存进cache
        return null;
    }
}
