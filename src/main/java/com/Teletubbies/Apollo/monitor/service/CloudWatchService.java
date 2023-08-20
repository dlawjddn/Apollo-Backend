package com.Teletubbies.Apollo.monitor.service;

import com.Teletubbies.Apollo.deploy.component.AwsClientComponent;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.ListMetricsRequest;
import software.amazon.awssdk.services.cloudwatch.model.Metric;

import java.util.List;

@Service
public class CloudWatchService {
    private final CloudWatchClient cloudWatchClient;

    public CloudWatchService(AwsClientComponent awsClientComponent) {
        this.cloudWatchClient = awsClientComponent.createCloudWatchClient();
    }

    public List<Metric> getCloudFrontMetrics() {
        return cloudWatchClient.listMetrics(ListMetricsRequest.builder()
                .namespace("AWS/CloudFront")
                .build())
                .metrics();
    }

    public List<Metric> getLoadBalancerMetrics() {
        return cloudWatchClient.listMetrics(ListMetricsRequest.builder()
                .namespace("AWS/ApplicationELB")
                .build())
                .metrics();
    }
}
