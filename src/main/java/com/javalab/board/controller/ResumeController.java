package com.javalab.board.controller;


import com.javalab.board.service.ResumeService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/resume")
@Slf4j
public class ResumeController {
    private final ResumeService resumeService;

    @GetMapping("/save")    public String save() {
        return "save";



    }
}