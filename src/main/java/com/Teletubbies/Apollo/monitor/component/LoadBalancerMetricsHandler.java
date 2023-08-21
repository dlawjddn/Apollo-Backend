//package com.Teletubbies.Apollo.monitor.component;
//
//import com.Teletubbies.Apollo.monitor.service.CloudWatchService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//import software.amazon.awssdk.services.cloudwatch.model.Metric;
//
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class LoadBalancerMetricsHandler extends TextWebSocketHandler {
//    private final CloudWatchService cloudWatchService;
//
//    public void handleTextMessage(WebSocketSession session) throws Exception {
//        List<Metric> loadBalancerMetrics = cloudWatchService.getLoadBalancerMetrics();
//        String jsonMetrics = new ObjectMapper().writeValueAsString(loadBalancerMetrics);
//        session.sendMessage(new TextMessage(jsonMetrics));
//    }
//}
