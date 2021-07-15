package com.linxh.paas.demo.controller;

import com.linxh.paas.demo.config.http.HeaderInterceptor;
import com.linxh.paas.demo.config.http.TraceInterceptor;
import com.linxh.paas.demo.model.dto.HelloDTO;
import com.linxh.paas.demo.utils.InetAddressUtils;
import com.linxh.paas.demo.utils.RequestUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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

    @Autowired
    private TraceInterceptor traceInterceptor;
    @Autowired
    private HeaderInterceptor headerInterceptor;

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);
    /**
     * 第一次请求时间
     */
    private static final String START = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss").format(new Date());

    @Autowired
    private Environment environment;

    @ApiOperation("保存")
    @GetMapping("/save")
    public String post(HttpServletRequest request, HelloDTO helloDTO) throws UnknownHostException {
        return "成功";
    }

    // @ApiOperation(value = "首页")
    // @ApiImplicitParams({
    //         @ApiImplicitParam(name = "name", value = "名称，如果携带会返回"),
    // })
    // @GetMapping("/")
    // public String get(HttpServletRequest request, String name) throws UnknownHostException {
    //
    //     String ipAddress = RequestUtils.getIpAddress(request);
    //     String dataStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss").format(new Date());
    //
    //     LOGGER.trace("[{}]请求时间:{}, 请求IP:{}", "trace", dataStr, ipAddress);
    //     LOGGER.debug("[{}]请求时间:{}, 请求IP:{}", "debug", dataStr, ipAddress);
    //     LOGGER.info("[{}]请求时间:{}, 请求IP:{}", "info", dataStr, ipAddress);
    //     LOGGER.warn("[{}]请求时间:{}, 请求IP:{}", "warn", dataStr, ipAddress);
    //     LOGGER.error("[{}]请求时间:{}, 请求IP:{}", "error", dataStr, ipAddress);
    //
    //     String ip = Inet4Address.getLocalHost().getHostAddress();
    //     String port = environment.getProperty("local.server.port");
    //
    //     StringBuilder sb = new StringBuilder(ip);
    //     sb.append(":").append(port);
    //     if (StringUtils.isEmpty(name)) {
    //         return sb.toString();
    //     }
    //     return sb.append(" ").append(name).toString();
    // }


    @ApiOperation(value = "session 会话保持")
    @GetMapping("/session")
    public Map<String, Object> session(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("first", START);
        map.put("session", request.getSession().getId());
        map.put("host", InetAddressUtils.getHostName());
        return map;
    }

    @ApiOperation(value = "RestTemplate 测试")
    @GetMapping("/rest")
    public String rest() {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(null);
        String port = environment.getProperty("local.server.port");
        String url = "http://localhost:" + port + "/session";
        restTemplate.getInterceptors().add(headerInterceptor);
        restTemplate.getInterceptors().add(traceInterceptor);
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return exchange.getBody();
    }
}