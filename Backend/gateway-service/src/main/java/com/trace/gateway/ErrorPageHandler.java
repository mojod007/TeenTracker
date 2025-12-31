package com.trace.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@Order(-2)
@Slf4j
public class ErrorPageHandler extends AbstractErrorWebExceptionHandler {

    public ErrorPageHandler(ErrorAttributes errorAttributes, WebProperties webProperties,
                            ApplicationContext applicationContext, ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, webProperties.getResources(), applicationContext);
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Map<String, Object> error = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        int status = (Integer) error.get("status");
        String path = (String) error.get("path");
        String message = (String) error.get("message");
        String errorType = (String) error.get("error");

        // Add additional request details for better debugging
        error.put("requestMethod", request.methodName());
        error.put("requestHeaders", request.headers().asHttpHeaders().toString());
        error.put("requestURI", request.uri().toString());
        error.put("remoteAddress", request.remoteAddress().map(addr -> addr.getAddress().getHostAddress()).orElse("unknown"));

        log.error("Error occurred - Status: {}, Path: {}, Error: {}, Message: {}, Method: {}, URI: {}",
                 status, path, errorType, message, request.methodName(), request.uri().toString());

        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(error);
    }
}
