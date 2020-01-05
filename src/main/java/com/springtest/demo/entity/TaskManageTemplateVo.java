package com.springtest.demo.entity;

import com.springtest.demo.handler.TaskManageOperator;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.function.Function;

/**
 * 任务管理xml文件解析后每个节点的解析结果Vo
 */
@Getter
@Setter
public class TaskManageTemplateVo {
    // 表名
    String tableName;

    // 对当前表的操作名称
    String operate;

    // 当前操作下实际的操作名，需要后期注入
    public TaskManageOperator operator;

    // 如果需要和其他表进行判定时，可以增加copyKey相等用于查询条件
    List<String> copyKeyList;

    // 不需要操作的列
    List<String> ignoreColumnList;

    // 操作时，需要入参的列
    List<String> columnList;
}
