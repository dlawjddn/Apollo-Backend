package com.Teletubbies.Apollo.deploy.controller;

import com.Teletubbies.Apollo.deploy.service.AwsSubnetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AwsSubnetController {

    private final AwsSubnetService awsSubnetService;

    @PostMapping("/subnet/{vpcId}")
    public ResponseEntity<String> createSubnets(@PathVariable String vpcId) {
        String subnetId = awsSubnetService.createSubnets(vpcId);
        return ResponseEntity.ok(subnetId);
    }

    @DeleteMapping("/subnet/{subnetId}")
    public ResponseEntity<String> deleteSubnet(@PathVariable String subnetId) {
        awsSubnetService.deleteSubnet(subnetId);
        return ResponseEntity.ok(subnetId);
    }
}
