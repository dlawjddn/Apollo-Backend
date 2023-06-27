package com.Teletubbies.Apollo.deploy.controller;

import com.Teletubbies.Apollo.deploy.service.DeployService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DeployController {
    private final DeployService deployService;

    public DeployController(DeployService deployService) {
        this.deployService = deployService;
    }

    @GetMapping("/deploy")
    public ResponseEntity<Map<Integer, String>> executeCommand() {
        Map<Integer, String> output = deployService.execCommand();
        return ResponseEntity.ok(output);
    }
}
