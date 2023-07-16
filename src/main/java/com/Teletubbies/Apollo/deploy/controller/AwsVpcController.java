package com.Teletubbies.Apollo.deploy.controller;

import com.Teletubbies.Apollo.deploy.service.AwsVpcService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AwsVpcController {
    private final AwsVpcService awsVpcService;

    public AwsVpcController(AwsVpcService awsVpcService) {
        this.awsVpcService = awsVpcService;
    }

    @PostMapping("/vpc")
    public ResponseEntity<String> createVpc() {
        String vpcId = awsVpcService.createVpc();
        return ResponseEntity.ok(vpcId);
    }

    @DeleteMapping("/vpc/{vpcId}")
    public ResponseEntity<String> deleteVpc(@PathVariable String vpcId) {
        awsVpcService.deleteVpc(vpcId);
        return ResponseEntity.ok(vpcId);
    }
}
