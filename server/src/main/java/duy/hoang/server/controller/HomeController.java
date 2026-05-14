package duy.hoang.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping({"/status", "/health"})
public class HomeController {

     @GetMapping
     public String healthCheck() {
          return "Application is running";
     }
}
