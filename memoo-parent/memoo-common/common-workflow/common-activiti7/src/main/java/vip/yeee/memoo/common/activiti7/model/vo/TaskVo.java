package vip.yeee.memoo.common.activiti7.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/12/15 17:45
 */
@Data
public class TaskVo {

    private String id;

    private String name;

    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endDate;

    private String assignee;

    private String instanceName;
}
