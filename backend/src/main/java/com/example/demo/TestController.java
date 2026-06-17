package com.example.demo;

import com.example.demo.utils.ExternalApiUtils;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final ExternalApiUtils externalApiUtils;

    @GetMapping("/")
    public String hello() {
        return "안녕!";
    }

    /**
     * 재난상황정보 조회 API 테스트
     * 
     * @return 재난상황정보 조회 결과
     * @throws Exception
     */
    @GetMapping("/disaster")
    public ResponseEntity<String> disaster() throws Exception {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(externalApiUtils.getDisasterInfo());
    }

    /**
     * 돌발상황정보 조회 API 테스트
     * 
     * @return 돌발상황정보 조회 결과
     * @throws Exception
     */
    @GetMapping("/event")
    public ResponseEntity<String> event() throws Exception {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(externalApiUtils.getEventInfo());
    }
}
