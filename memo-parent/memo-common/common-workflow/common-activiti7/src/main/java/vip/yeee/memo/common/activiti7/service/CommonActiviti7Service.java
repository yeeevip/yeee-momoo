package vip.yeee.memo.common.activiti7.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.model.payloads.DeleteProcessPayload;
import org.activiti.api.process.model.payloads.ResumeProcessPayload;
import org.activiti.api.process.model.payloads.SuspendProcessPayload;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Order;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.model.payloads.CompleteTaskPayload;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vip.yeee.memo.base.model.exception.BizException;
import vip.yeee.memo.base.model.vo.PageReqVO;
import vip.yeee.memo.base.model.vo.PageVO;
import vip.yeee.memo.common.activiti7.mapper.ActivitiMapper;
import vip.yeee.memo.common.activiti7.model.request.DefDeleteReq;
import vip.yeee.memo.common.activiti7.model.request.InsDeleteReq;
import vip.yeee.memo.common.activiti7.model.request.InstCreateReq;
import vip.yeee.memo.common.activiti7.model.vo.DefinitionVo;
import vip.yeee.memo.common.activiti7.model.vo.InstanceVo;
import vip.yeee.memo.common.activiti7.model.vo.TaskVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/12/15 17:39
 */
@Slf4j
@Component
public class CommonActiviti7Service {

//    @Autowired
//    private ActivitiMapper activitiMapper;
    @Resource
    private RepositoryService repositoryService;
    @Autowired
    private ProcessRuntime processRuntime;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskRuntime taskRuntime;

    public PageVO<DefinitionVo> definitionList(PageReqVO<?> reqVO) {
        PageVO<DefinitionVo> pageVO = new PageVO<>(reqVO.getPageNum(), reqVO.getPageSize());

        int totalNum = repositoryService.createProcessDefinitionQuery().list().size();
        pageVO.setTotal((long) totalNum);
        if (totalNum == 0) {
            return pageVO;
        }
        List<ProcessDefinition> definitionList = repositoryService.createProcessDefinitionQuery()
                .orderByProcessDefinitionVersion()
                .desc()
                .listPage((reqVO.getPageNum() - 1) * reqVO.getPageSize(), reqVO.getPageSize());
        List<DefinitionVo> voList = definitionList
                .stream()
                .map(po -> {
                    DefinitionVo vo = new DefinitionVo();
                    vo.setProcessDefinitionID(po.getId());
                    vo.setName(po.getName());
                    vo.setKey(po.getKey());
                    vo.setResourceName(po.getResourceName());
                    vo.setDeploymentID(po.getDeploymentId());
                    vo.setVersion(po.getVersion());
                    return vo;
                })
                .collect(Collectors.toList());
        pageVO.setResult(voList);
        return pageVO;
    }

    public Void definitionDelete(DefDeleteReq req) {
        for (String depId : req.getIds()) {
            repositoryService.deleteDeployment(depId, true);
        }
        return null;
    }

    public Void definitionAddDeploymentByString(String stringBPMN) {
        Deployment deployment = repositoryService.createDeployment()
                .addString("CreateWithYeeeSystem.bpmn", stringBPMN)
                .name("未命名的部署名称")
                .deploy();
        return null;
    }

    public void definitionDetailXml(HttpServletResponse response, String deploymentId, String resourceName) {
        InputStream inputStream = repositoryService.getResourceAsStream(deploymentId, resourceName);
        response.setContentType("text/xml");
        try {
            IoUtil.copy(inputStream, response.getOutputStream());
        } catch (IOException e) {
            log.error("获取失败", e);
            throw new BizException("获取失败");
        }
    }

    public PageVO<InstanceVo> instanceList(PageReqVO<?> reqVO) {
        PageVO<InstanceVo> pageVO = new PageVO<>(reqVO.getPageNum(), reqVO.getPageSize());
        Order order = Order.by("startDate", Order.Direction.DESC);
        Pageable pageable = Pageable.of((reqVO.getPageNum() - 1) * reqVO.getPageSize(), reqVO.getPageSize(), order);
        Page<ProcessInstance>  page = processRuntime.processInstances(pageable);
        if (CollectionUtil.isEmpty(page.getContent())) {
            return pageVO;
        }
        Set<String> pdIds = page.getContent().stream().map(ProcessInstance::getProcessDefinitionId).collect(Collectors.toSet());
        List<ProcessDefinition> definitionList = repositoryService.createProcessDefinitionQuery().processDefinitionIds(pdIds).list();
        Map<String, ProcessDefinition> definitionMap = definitionList.stream()
                .collect(Collectors.toMap(ProcessDefinition::getId, Function.identity(), (o, n) -> n));
        List<InstanceVo> voList = page.getContent()
                .stream()
                .map(po -> {
                    InstanceVo vo = new InstanceVo();
                    vo.setId(po.getId());
                    vo.setName(po.getName());
                    vo.setStatus(po.getStatus().name());
                    vo.setProcessDefinitionId(po.getProcessDefinitionId());
                    vo.setProcessDefinitionKey(po.getProcessDefinitionKey());
                    vo.setStartDate(po.getStartDate());
                    vo.setProcessDefinitionVersion(po.getProcessDefinitionVersion());
                    ProcessDefinition processDefinition = definitionMap.get(po.getProcessDefinitionId());
                    vo.setResourceName(processDefinition.getResourceName());
                    vo.setDeploymentId(processDefinition.getDeploymentId());
                    return vo;
                })
                .collect(Collectors.toList());
        pageVO.setTotal((long) page.getTotalItems());
        pageVO.setResult(voList);
        return pageVO;
    }

    public Void instanceCreate(InstCreateReq req) {
        Map<String,Object> variables = new HashMap<>();
        variables.put("applicant", SecurityContextHolder.getContext().getAuthentication().getName());
        runtimeService.startProcessInstanceById(req.getPdId(), variables);
        return null;
    }

    public Void instanceSuspend(String instanceId) {
        SuspendProcessPayload payload = ProcessPayloadBuilder
                .suspend()
                .withProcessInstanceId(instanceId)
                .build();
        processRuntime.suspend(payload);
        return null;
    }

    public Void instanceResume(String instanceId) {
        ResumeProcessPayload payload = ProcessPayloadBuilder
                .resume()
                .withProcessInstanceId(instanceId)
                .build();
        processRuntime.resume(payload);
        return null;
    }

    public Void instanceDelete(InsDeleteReq req) {
        for (String instanceId : req.getIds()) {
            DeleteProcessPayload payload = ProcessPayloadBuilder
                    .delete()
                    .withProcessInstanceId(instanceId)
                    .build();
            processRuntime.delete(payload);
        }
        return null;
    }

    public PageVO<TaskVo> taskList(PageReqVO<?> reqVO) {
        PageVO<TaskVo> pageVO = new PageVO<>(reqVO.getPageNum(), reqVO.getPageSize());
        Order order = Order.by("createdDate", Order.Direction.DESC);
        Pageable pageable = Pageable.of((reqVO.getPageNum() - 1) * reqVO.getPageSize(), reqVO.getPageSize(), order);
        Page<Task> page = taskRuntime.tasks(pageable);
        if (CollectionUtil.isEmpty(page.getContent())) {
            return pageVO;
        }
        Set<String> piIds = page.getContent().stream().map(Task::getProcessInstanceId).collect(Collectors.toSet());
        List<org.activiti.engine.runtime.ProcessInstance> instanceList = runtimeService.createProcessInstanceQuery().processInstanceIds(piIds).list();
        Map<String, org.activiti.engine.runtime.ProcessInstance> definitionMap = instanceList.stream()
                .collect(Collectors.toMap(org.activiti.engine.runtime.ProcessInstance::getId, Function.identity(), (o, n) -> n));
        List<TaskVo> voList = page.getContent()
                .stream()
                .map(po -> {
                    TaskVo vo = new TaskVo();
                    vo.setId(po.getId());
                    vo.setName(po.getName());
                    vo.setStatus(po.getStatus().name());
                    vo.setCreatedDate(po.getCreatedDate());
                    vo.setAssignee(po.getAssignee());
                    org.activiti.engine.runtime.ProcessInstance instance = definitionMap.get(po.getProcessInstanceId());
                    vo.setInstanceName(instance.getProcessDefinitionName());
                    return vo;
                })
                .collect(Collectors.toList());
        pageVO.setTotal((long) page.getTotalItems());
        pageVO.setResult(voList);
        return pageVO;
    }

    public Void taskComplete(String taskId) {
        Task task = taskRuntime.task(taskId);
        if (task == null) {
            throw new BizException("代办任务不存在");
        }
        if (task.getAssignee() == null) {
            taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
        }
        CompleteTaskPayload payload = TaskPayloadBuilder.complete().withTaskId(task.getId())
                //.withVariable("num", "2") // 执行环节设置变量
                .build();
        taskRuntime.complete(payload);
        return null;
    }
}
