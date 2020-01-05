package com.springtest.demo.handler;

import com.springtest.demo.entity.TaskManageParam;
import com.springtest.demo.entity.TaskManageTemplateVo;

import java.util.Map;

/**
 * 自定义函数接口，用于任务管理注入
 */
@FunctionalInterface
public interface TaskManageOperator {
    Object operating(TaskManageTemplateVo taskManageTemplate, Map<String, TaskManageParam> taskManageParamMap);
}
