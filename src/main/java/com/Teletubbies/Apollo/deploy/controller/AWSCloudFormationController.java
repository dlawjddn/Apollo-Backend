package com.Teletubbies.Apollo.deploy.controller;

import com.Teletubbies.Apollo.deploy.service.AWSCloudFormationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class AWSCloudFormationController {
    private final AWSCloudFormationService awsCloudFormationService;

    @PostMapping("/cloudformation/{stackName}")
    public ResponseEntity<String> createStack(@PathVariable String stackName, @RequestBody String type) {
        awsCloudFormationService.createStack(stackName, type);
        return ResponseEntity.ok(stackName + " is created successfully");
    }

    @DeleteMapping("/cloudformation/{stackName}")
    public ResponseEntity<String> deleteStack(@PathVariable String stackName) {
        awsCloudFormationService.deleteStack(stackName);
        return ResponseEntity.ok(stackName + " is deleted successfully");
    }

    @GetMapping("/cloudformation")
    public ResponseEntity<String> describeStacks() {
        awsCloudFormationService.describeStacks();
        return ResponseEntity.ok("Describe stacks successfully");
    }
}
