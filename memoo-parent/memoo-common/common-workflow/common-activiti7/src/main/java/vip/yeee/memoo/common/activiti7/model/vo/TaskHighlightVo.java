package vip.yeee.memoo.common.activiti7.model.vo;

import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 * description......
 *
 * @author yeeee
 * @since 2024/5/2 14:56
 */
@Data
public class TaskHighlightVo {

    private Set<String> highLine;

    private Set<String> highPoint;

    private Set<String> iDo;

    private Set<String> waitingToDo;

    private Map<String, String> finishedUserTaskMap;

    private Map<String, String> unFinishedUserTaskMap;
}
