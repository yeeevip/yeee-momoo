package vip.yeee.memoo.demo.elasticsearch.domain.es.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/4/26 16:01
 */
@Data
public class BaseIndex {
    @Id
    private Integer id;
}
