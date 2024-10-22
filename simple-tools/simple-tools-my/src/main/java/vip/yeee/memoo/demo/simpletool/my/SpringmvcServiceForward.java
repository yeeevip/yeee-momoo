package vip.yeee.memoo.demo.simpletool.my;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vip.yeee.memoo.base.web.utils.HttpRequestUtils;

import java.util.LinkedHashMap;

/**
 *
 * @author https://www.yeee.vip
 * @since 2021/12/6 14:22
 */
@Slf4j
@RequestMapping("/onlineInterview")
public class SpringmvcServiceForward {

    private final String gp = "channeApilVal";
    private final String name = "chcMiniAppTempServerAddress";

    @ResponseBody
    @RequestMapping("/**")
    public Object chcTest(HttpServletRequest request, @RequestParam LinkedHashMap<String, Object> reqParam) {
        String url = ""/*ServiceUtils.getConfigService().getValue(gp, name) + request.getRequestURI()*/;
        if(StrUtil.isBlank(url)) return null;
        String res;
        HttpRequest req = HttpRequest.get(url).header("linkTicket", request.getHeader("linkTicket")).form(reqParam);
        if(!"GET".equalsIgnoreCase(request.getMethod())) {
            req.body(HttpRequestUtils.getBody(request));
        }
        res = req.execute().body();
        log.info("channel中转，url = {}，params = {}, res = {}", url, reqParam, res);
        return JSON.parseObject(res, LinkedHashMap.class, Feature.OrderedField);
    }

}
