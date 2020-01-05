package com.springtest.demo.handler;

import com.springtest.demo.constant.TaskCopyProcessEnum;
import com.springtest.demo.constant.TaskManageOperatorEnum;
import com.springtest.demo.constant.TaskManageTagEnum;
import com.springtest.demo.entity.TaskCopyProcessVo;
import com.springtest.demo.entity.TaskManageParam;
import com.springtest.demo.entity.TaskManageTemplateVo;
import com.springtest.demo.util.TaskManageXmlReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.springtest.demo.constant.TaskManageOperatorEnum.*;

public class TaskManageHandler {

    // 模块名称
    public static final String MODUAL_NANE = "example";

    // 当前模块默认的任务管理XML
    private static final String TASK_MANAGE_XML_PATH = "暂时还没想好放哪";

    /**
     * 任务复制
     * @param taskManageTemplate 操作解析结果
     * @param taskManageParamMap 参数结构<column_name, TaskManageParam>
     * @return 复制结果
     */
    public static boolean copyTask(TaskManageTemplateVo taskManageTemplate,  Map<String, TaskManageParam> taskManageParamMap) {
        // TODO step1: 对taskManageTemplate中的table，使用taskManageTemplate.columnList作为查询列，
        //  使用taskManageParamMap.get(column).old作为入参，进行查询返回List<Map<String, Object>>

        // TODO step2: 对查询结果，删除taskManageTemplate.ignoreList中所有列，
        //  对taskManageTemplate.columnList中所有列，更新成taskManageParamMap.get(column).new

        // TODO step3: 将结果插入回数据表，
        //  如果catch了Mybatis操作异常，记录相应结果，并且返回失败

        // 操作成功
        return true;
    }

    /**
     * 删除任务
     * @param taskManageTemplate
     * @param taskManageParamMap
     * @return
     */
    public static boolean deleteTask(TaskManageTemplateVo taskManageTemplate,  Map<String, TaskManageParam> taskManageParamMap) {
        // TODO taskManageTemplate.table，对于该表中taskManageTemplate.columnList中所有列，如果匹配上了taskManageParamMap.get(column).new的数据，需要删除掉

        // TODO 如果catch了Mybatis操作异常，记录相应结果并返回失败

        // 操作成功
        return true;
    }

    // 其他操作各个微服务自己定义

    // 定义任务管理操作函数，用于对解析结果进行注入
    /**
     * 定义本微服务下某些标签对应的操作，用于绑定xml中的operate和自定义Operator
     */
    private static HashMap<String, TaskManageOperator> operateFactory;
    static {
        for (TaskManageOperatorEnum tag : values()) {
            switch (tag){
                case COPY :
                    operateFactory.put(COPY.name(), (template, paramMap)->copyTask(template, paramMap));
                    break;
                case DELETE:
                    operateFactory.put(DELETE.name(), (template, paramMap)->deleteTask(template, paramMap));
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 对于XML的文件路径，解析之后，取出所需的tag及其对应的所有数据表，然后注入自定义operate实现
     *
     * @param tag
     * @param operateFactory
     * @return
     */
    // finish
    public static List<TaskManageTemplateVo>getTaskManageFactory(String xmlPath, String tag, Map<String, TaskManageOperator> operateFactory) {
        Map<String, List<TaskManageTemplateVo>> taskManageTemplate = TaskManageXmlReader.getTaskManageXmlResult(xmlPath);
        return getTaskManageFactory(taskManageTemplate, tag, operateFactory);
    }

    /**
     * 对于XML的解析结果，注入自定义operate实现，同时取出所需的tag及其对应的所有数据表
     * @param tag 需要曲的标签，用于取出需要用的表操作
     * @param operateFactory 需要注入的自定义Operator实现，用于标志xml中operate的实现
     * @return
     */
    // finish
    public static List<TaskManageTemplateVo> getTaskManageFactory(Map<String, List<TaskManageTemplateVo>> taskManageTemplateMap, String tag, Map<String, TaskManageOperator> operateFactory) {
        List<TaskManageTemplateVo> partOfAll = taskManageTemplateMap.get(tag);
        // 对每一个tag下每一张表，根据operate注入相应的操作
        partOfAll.forEach(template -> template.setOperator(operateFactory.get(template.getOperate())));
        return partOfAll;
    }

    /**
     * 根据tag执行相应的动作，需要传入配置文件中column中所需要的参数，业务调用的主要入口
     * @param tag 配置文件中的root的下一个节点的标签，用于获取某一个标签下的所有表操作
     * @param paramMap 每一个表操作的入参在这里进行控制
     */
    // finish
    public void compute(TaskManageTagEnum tag, Map<String, TaskManageParam> paramMap) {
        List<TaskManageTemplateVo> taskManageTemplateList = getTaskManageFactory(TASK_MANAGE_XML_PATH, tag.name(), operateFactory);
        int count = 0;

        // task_id + dc_name + modual_name作为key
        String processKey = new StringBuilder().append(paramMap.get("task_id").getNewParam()).append("+").append(paramMap.get("dc_name").getNewParam()).append("+").append(MODUAL_NANE).toString();
        for (TaskManageTemplateVo template : taskManageTemplateList) {
            // 即使复制失败，依然继续执行，直到所有任务表复制完成，等待上游指令（通常上游会下发删除回滚指令）
            Object oprateResult = template.operator.operating(template, paramMap);
            if (TaskManageTagEnum.NON_LAZY_COPY.equals(tag)) {
                updateCopyTaskProcess(oprateResult, (float)(++count)/taskManageTemplateList.size(), processKey);
            }
        }
    }

    /**
     * 任务复制进度管理
     */
    private static ConcurrentHashMap<String, TaskCopyProcessVo> taskCopyProcessRecord;
    static {
        taskCopyProcessRecord = new ConcurrentHashMap<>(256);
    }

    /**
     * 刷新任务复制进度
     * @param oprateResult 本次任务复制操作结果
     * @param process 本次任务复制进展
     * @param processKey 本次任务复制的唯一标志
     */
    // finish
    private void updateCopyTaskProcess(Object oprateResult, float process, String processKey){
        // 立即复制需要统计进度
        if (oprateResult instanceof Boolean) {
            // 成功时，刷新进度
            if(Boolean.TRUE.equals(oprateResult)){
                taskCopyProcessRecord.get(processKey).setProcess(process);
                // 复制完成
                if(Math.abs(process-1.0) <= 0.0001){
                    taskCopyProcessRecord.get(processKey).setStatus(TaskCopyProcessEnum.FINISH);
                } else {
                    taskCopyProcessRecord.get(processKey).setStatus(TaskCopyProcessEnum.PROCESSING);
                }
            }else {
                taskCopyProcessRecord.get(processKey).setStatus(TaskCopyProcessEnum.ERROR);
            }
        }
    }
}
