package vip.yeee.memoo.demo.subdt.apachesharding.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/6/22 17:20
 */
@TableName("t_test")
@Data
public class Test {

    private Long id;

    private Integer type;

    private String field1;

    private String field2;

}
