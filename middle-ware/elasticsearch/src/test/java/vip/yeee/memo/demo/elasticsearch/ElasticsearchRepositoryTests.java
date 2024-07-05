package vip.yeee.memo.demo.elasticsearch;

import org.junit.jupiter.api.Test;
import vip.yeee.memo.demo.elasticsearch.service.ITProjectService;
import vip.yeee.memo.demo.elasticsearch.domain.mysql.entity.TProject;
import vip.yeee.memo.demo.elasticsearch.domain.es.entity.TProjectIndex;
import vip.yeee.memo.demo.elasticsearch.domain.es.repository.TProjectIndexRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.stream.Collectors;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/4/25 22:41
 */
@Slf4j
@SpringBootTest(classes = ElasticsearchApplication.class)
public class ElasticsearchRepositoryTests {

    // 直接操作文档
    @Autowired
    private TProjectIndexRepository projectIndexRepository;
    @Autowired
    private ITProjectService iTProjectService;

    @Test
    public void createIndex() {
        log.info("系统初始化会自动扫描实体类中的@Document注解创建索引");
    }

    /**
     * 删除索引数据
     */
    @Test
    public void testDeleteData() {
        // 数据库中的数据
        List<TProject> dbDataList = iTProjectService.list();
        List<Integer> idList = dbDataList
                .stream()
                .map(TProject::getId)
                .collect(Collectors.toList());
        projectIndexRepository.deleteAllById(idList);
    }

    /**
     * 批量同步到ES
     */
    @Test
    public void testBatchSaveData() {
        // 数据库中的数据
        List<TProject> dbDataList = iTProjectService.list();
        // 构建索引数据
        List<TProjectIndex> esDataList = dbDataList.stream()
                .map(item -> {
                    TProjectIndex projectIndex = new TProjectIndex();
                    BeanUtils.copyProperties(item, projectIndex);
                    projectIndex.setId(item.getId());
                    projectIndex.setCreateTime(item.getLaunchDateRaising());
                    return projectIndex;
                })
                .collect(Collectors.toList());
        // 同步到es
        Iterable<TProjectIndex> res = projectIndexRepository.saveAll(esDataList);
        log.info("-------------bulk res = {}------------------", res);
    }

    /**
     * 查找全部
     */
    @Test
    public void testFindAllData() {
        Page<TProjectIndex> res = (Page<TProjectIndex>) projectIndexRepository.findAll();
        log.info("-------------bulk res = {}------------------", res.getTotalElements());
    }

    /**
     * 根据方法名称自动识别查询条件
     */
    @Test
    public void testQueryCreationMechanism() {
        List<TProjectIndex> list1 = projectIndexRepository.queryByTitleOrBlurbOrderByIdDesc("书", "一");
        log.info("----------------------------- list1 = {} --------------------------------------", list1);
        List<TProjectIndex> list2 = projectIndexRepository.findByTitleOrBlurbOrderByIdDesc("书", "一");
        log.info("----------------------------- list2 = {} --------------------------------------", list2);
        Page<TProjectIndex> page = projectIndexRepository
                .findByTitleOrBlurbOrderByIdDesc("书", "一", PageRequest.of(0, 5));
        log.info("----------------------------- page = {} --------------------------------------", page);
    }

}
