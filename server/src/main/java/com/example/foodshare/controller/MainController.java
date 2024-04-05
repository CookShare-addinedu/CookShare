
package com.example.foodshare.controller;

import com.example.foodshare.dto.EnrollUser;
import com.example.foodshare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class MainController {

    @Autowired
    UserService userService;


    @GetMapping("/")
    public String mainRoot() {
        return "Main Controller";
        /* 로그인 해야 가능 */

    }

    @PostMapping("/enroll")
    public String userEnroll(EnrollUser enrollUser) {
        System.out.println(enrollUser.getUsername());
        UserService.userEnroll(enrollUser);
        return "ok";
    }


}

