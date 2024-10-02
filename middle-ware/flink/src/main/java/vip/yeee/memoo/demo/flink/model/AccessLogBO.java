package vip.yeee.memoo.demo.flink.model;

import lombok.Data;
import vip.yeee.memoo.demo.flink.constant.OperateEventEnum;

import java.util.Date;

@Data
public class AccessLogBO {

    /**
     * @see OperateEventEnum
     */
    private String event;
    private Long subjectId;
    private Date timestamp;
    private String ip;
    private String domain;
    private Integer status;
    private Float responseTime;
    private String device;

}
