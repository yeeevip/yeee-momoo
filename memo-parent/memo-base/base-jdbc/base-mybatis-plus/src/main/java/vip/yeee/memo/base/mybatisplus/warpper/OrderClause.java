package vip.yeee.memo.base.mybatisplus.warpper;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yeee丶一页 (https://www.yeee.vip)
 */
@Data
public class OrderClause implements Serializable {

    private String k; //key
    private String t; //type
    
}
