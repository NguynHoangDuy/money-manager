package duy.hoang.server.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import duy.hoang.server.service.DashboardService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {
     private final DashboardService dashboardService;

     @GetMapping
     public ResponseEntity<Map<String, Object>> getDashboardData() {
          return ResponseEntity.ok(dashboardService.getDashboardData());
     }
}
