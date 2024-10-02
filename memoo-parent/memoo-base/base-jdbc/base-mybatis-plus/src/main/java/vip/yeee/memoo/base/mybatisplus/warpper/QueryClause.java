package vip.yeee.memoo.base.mybatisplus.warpper;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author yeee丶一页 (https://www.yeee.vip)
 */
@Data
public class QueryClause implements Serializable {
    
    private List<WhereClause> w;
    private List<OrderClause> o;
    private PageClause p;
    
}
