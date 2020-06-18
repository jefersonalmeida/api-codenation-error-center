package com.error.center.controller;

import com.error.center.dto.UserDTO;
import com.error.center.response.Response;
import com.error.center.util.Util;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping("health")
    public String health() {
        return "{\"message\":\"ok\"}";
    }


    @GetMapping(path = "profile")
    public ResponseEntity<Response<UserDTO>> profile() {
        Response<UserDTO> response = new Response<>();
        response.setData(Util.getUserDTO());
        return ResponseEntity.ok().body(response);
    }
}
