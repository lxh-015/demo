package com.linxh.paas.demo.config.http;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * RestTemplate Interceptor Add Header
 * <p>
 * header 拦截器需要放在第一位,否则打印日志输出时无法打印所有请求头信息
 *
 * @author linxh
 */
@Component
public class HeaderInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        this.addHeader(request);
        return execution.execute(request, body);
    }

    /**
     * 添加信息到header中
     *
     * @param request
     */
    protected void addHeader(HttpRequest request) {
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss").format(new Date());
        request.getHeaders().add("currentTime", date);
    }
}
