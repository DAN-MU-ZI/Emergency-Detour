package com.example.demo.navigation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class NavigationController {

    private final NavigationService navigationService;
    
    /**
     * 목적지를 검색하는 API
     * 
     */
    @GetMapping("/places")
    public ResponseEntity<?> searchPlaces(
            @RequestParam("query") String location
    ) throws Exception {
        
        return navigationService.searchPlaces(location);
    }

    /**
     * 경로를 계산하는 API
     * 
     */
    @PostMapping("/routes")
    public ResponseEntity<?> getRoutes(@RequestBody RouteRequest request) {
        return navigationService.getRoutes(request);
    }
}
