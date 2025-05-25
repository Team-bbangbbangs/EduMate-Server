package com.edumate.eduserver;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping
    public ResponseEntity<TestResponse> getTest() {
        return ResponseEntity.ok(new TestResponse("testStr", 1));
    }

    @PostMapping
    public ResponseEntity<TestResponse> postTest(@RequestBody TestRequest request) {
        return ResponseEntity.ok(new TestResponse(request.testStr(), request.testInt()));
    }
}
