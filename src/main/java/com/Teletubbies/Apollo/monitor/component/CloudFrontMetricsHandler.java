package com.Teletubbies.Apollo.monitor.component;

import com.Teletubbies.Apollo.monitor.service.CloudWatchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import software.amazon.awssdk.services.cloudwatch.model.Metric;

import java.util.List;

@Component
@AllArgsConstructor
public class CloudFrontMetricsHandler extends TextWebSocketHandler {
    private final CloudWatchService cloudWatchService;

    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        List<Metric> cloudFrontMetrics = cloudWatchService.getCloudFrontMetrics();
        String jsonMetrics = new ObjectMapper().writeValueAsString(cloudFrontMetrics);
        session.sendMessage(new TextMessage(jsonMetrics));
    }
}
