package com.javalab.board.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;

@Controller
@Slf4j
public class HomeController {

		@GetMapping("/")
		public String home(Model model) {
			log.info("여기는 DemoController home()");
			model.addAttribute("date", new Date());
			return "index";
		}	
	}