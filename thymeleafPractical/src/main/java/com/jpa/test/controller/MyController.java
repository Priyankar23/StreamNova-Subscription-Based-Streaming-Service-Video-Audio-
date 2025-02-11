package com.jpa.test.controller;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MyController {

	
	@RequestMapping(value="/about", method = RequestMethod.GET)
	 public String about(Model model) {
		 System.out.println("Inside about handler......");
		model.addAttribute("name","Priyanka");
		 model.addAttribute("currentDate", new Date().toLocaleString());
		 return "about";
		 //about.html
	 }
	
	// handling iteration
	
	@GetMapping("/example-loop")
	public String iterateHandler(Model m) {
		
		//create a list , set collection
		List<String> names = List.of("ankitha","lakshmi","karana","john");
		m.addAttribute("names",names);
		
		return "iterate";
	}
	
	//handling for conditions
	
	@GetMapping("/condition")
	public String conditionHandler(Model m) {
		
		System.out.println(" condition hanlder exec");
		m.addAttribute("isActive",true);
		m.addAttribute("gender","M");
		
		List<Integer> list = List.of(12,345,456,4567);
		m.addAttribute("mylist",list);
		return "condition";
	}
	//handler for including fragment
	
	@GetMapping("/service")
	public String servicesHandler(Model m) {
		return "service";
	}
	
	//for new about
	
	@GetMapping("/newabout")
	public String newAbout() {
		return "aboutnew";
	}
}
