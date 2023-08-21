//package com.Teletubbies.Apollo.monitor.config;
//
//import com.Teletubbies.Apollo.monitor.component.CloudFrontMetricsHandler;
//import com.Teletubbies.Apollo.monitor.component.LoadBalancerMetricsHandler;
//import com.Teletubbies.Apollo.monitor.service.CloudWatchService;
//import lombok.AllArgsConstructor;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.config.annotation.EnableWebSocket;
//import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
//import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
//
//@Configuration
//@EnableWebSocket
//@RequiredArgsConstructor
//public class WebSocketConfig implements WebSocketConfigurer {
//    private final CloudFrontMetricsHandler cloudFrontMetricsHandler;
//    private final LoadBalancerMetricsHandler loadBalancerMetricsHandler;
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(cloudFrontMetricsHandler, "/api/monitor/cloudfront")
//                .setAllowedOrigins("*");
//
//        registry.addHandler(loadBalancerMetricsHandler, "/api/monitor/loadbalancer")
//                .setAllowedOrigins("*");
//    }
//}
