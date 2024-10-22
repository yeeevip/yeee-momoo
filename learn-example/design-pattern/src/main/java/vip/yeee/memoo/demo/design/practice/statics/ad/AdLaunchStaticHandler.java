package vip.yeee.memoo.demo.design.practice.statics.ad;

import org.springframework.stereotype.Component;
import vip.yeee.memoo.demo.design.practice.statics.vo.StaticAdVo;
import vip.yeee.memoo.demo.design.practice.statics.vo.StaticBo;
import vip.yeee.memoo.demo.design.practice.statics.vo.TCmsSite;

import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

/**
 * 投放操作
 *
 * @author https://www.yeee.vip
 * @since 2023/1/3 16:58
 */
@Component
public class AdLaunchStaticHandler extends AbstractAdStaticHandler {

    @Override
    protected List<StaticBo.OprType> supportOprTypes() {
        return Collections.singletonList(StaticBo.OprType.UPDATE);
    }

    @Override
    protected TreeMap<String, StaticAdVo> handleUpdate(TCmsSite site, StaticBo bo, TreeMap<String, StaticAdVo> adDataMap) throws Exception {
        return adDataMap;
    }

}
