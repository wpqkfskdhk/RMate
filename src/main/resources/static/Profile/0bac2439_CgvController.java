package com.icia.cgv.controller;

import com.icia.cgv.service.CgvService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class CgvController {

    private final CgvService csvc;

    @GetMapping("/cgv")
    public ModelAndView cgv(){
        return csvc.cgv();
    }
}
