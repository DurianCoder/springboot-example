package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author ying.jiang
 * @define Todo
 * @date 2020-10-15-13:50:00
 */
@RestController
@RequestMapping("provider")
public class ProviderController {

    @GetMapping("/hello")
    public String provider() {
        return "Hello, I'm Provider!";
    }
}