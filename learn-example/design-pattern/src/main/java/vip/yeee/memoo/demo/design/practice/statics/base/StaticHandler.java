package vip.yeee.memoo.demo.design.practice.statics.base;

import vip.yeee.memoo.demo.design.practice.statics.vo.StaticBo;

/**
 * 获取方式：{@code AdStaticContext.getAdvertHandler()、AdStaticContext.getLaunchHandler()}
 *
 * @author https://www.yeee.vip
 * @since 2023/1/3 16:54
 */
public interface StaticHandler {

    void handle(StaticBo bo);

}
