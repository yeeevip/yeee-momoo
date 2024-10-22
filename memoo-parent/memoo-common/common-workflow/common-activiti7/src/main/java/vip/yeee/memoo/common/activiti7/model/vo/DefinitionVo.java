package vip.yeee.memoo.common.activiti7.model.vo;

import lombok.Data;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/12/15 17:45
 */
@Data
public class DefinitionVo {

    private String processDefinitionID;

    private String name;

    private String key;

    private String resourceName;

    private String deploymentID;

    private Integer version;
}
