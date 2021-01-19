package com.linxh.paas.demo.config.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * RestTemplate Interceptor trace log
 *
 * @author linxh
 */
@Component
public class TraceInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TraceInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        this.traceRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        this.traceResponse(response);
        return response;
    }

    /**
     * 响应信息
     *
     * @param response
     */
    protected void traceResponse(ClientHttpResponse response) throws IOException {
        LOGGER.info("============================response begin==========================================");
        LOGGER.info("Status code  : {}", response.getStatusCode());
        LOGGER.info("Status text  : {}", response.getStatusText());
        LOGGER.info("Headers      : {}", response.getHeaders());
        LOGGER.info("=======================response end=================================================");
    }

    /**
     * 请求信息
     *
     * @param request
     * @param body
     */
    protected void traceRequest(HttpRequest request, byte[] body) {
        LOGGER.info("======= request begin ========");
        LOGGER.info("uri        : {}", request.getURI());
        LOGGER.info("method     : {}", request.getMethod());
        LOGGER.info("headers    : {}", request.getHeaders());
        LOGGER.info("body       : {}", new String(body));
        LOGGER.info("======= request end ========");
    }
}
