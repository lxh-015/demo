package com.linxh.paas.demo.controller;

import com.linxh.paas.demo.utils.RequestUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author v.linxh@gmail.com
 * @version 0.1
 */
@Api(tags = "web接口")
@RestController
public class HelloController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);
    /**
     * 第一次请求时间
     */
    private static final String START = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss").format(new Date());

    @Autowired
    private Environment environment;

    @ApiOperation(value = "首页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称，如果携带会返回"),
    })
    @GetMapping("/")
    public String get(HttpServletRequest request, String name) throws UnknownHostException {

        String ipAddress = RequestUtils.getIpAddress(request);
        String dataStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss").format(new Date());

        LOGGER.trace("[{}]请求时间:{}, 请求IP:{}", "trace", dataStr, ipAddress);
        LOGGER.debug("[{}]请求时间:{}, 请求IP:{}", "debug", dataStr, ipAddress);
        LOGGER.info("[{}]请求时间:{}, 请求IP:{}", "info", dataStr, ipAddress);
        LOGGER.warn("[{}]请求时间:{}, 请求IP:{}", "warn", dataStr, ipAddress);
        LOGGER.error("[{}]请求时间:{}, 请求IP:{}", "error", dataStr, ipAddress);

        String ip = Inet4Address.getLocalHost().getHostAddress();
        String port = environment.getProperty("local.server.port");

        StringBuilder sb = new StringBuilder(ip);
        sb.append(":").append(port);
        if (StringUtils.isEmpty(name)) {
            return sb.toString();
        }
        return sb.append(" ").append(name).toString();
    }

    @ApiOperation(value = "session 会话保持")
    @GetMapping("/session")
    public Map<String, Object> session(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("first", START);
        map.put("session", request.getSession().getId());
        return map;
    }
}