package com.osundosun.momo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
  
  @GetMapping("/index.html")
  public String index() {
    return "admin/adminUser";
  }
  
  @GetMapping("/shop.html")
  public String shop() {
	  return "shop";
  }
  
  @GetMapping("/shop-detail.html")
  public String shopDetail() {
	  return "shop-detail";
  }
  
  @GetMapping("/cart.html")
  public String cart() {
	  return "cart";
  }
  
  @GetMapping("/chackout.html")
  public String chackout() {
	  return "chackout";
  }
  
  @GetMapping("/testimonial.html")
  public String testimonial() {
	  return "testimonial";
  }
  
  @GetMapping("/404.html")
  public String error404() {
	  return "404";
  }
  
  @GetMapping("/contact.html")
  public String contact() {
	  return "contact";
  }

}
