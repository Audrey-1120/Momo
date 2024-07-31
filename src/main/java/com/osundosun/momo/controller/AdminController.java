package com.osundosun.momo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
  
  // 사용자 관리 페이지
  @GetMapping("/adminUser.page")
  public String adminUser() {
    return "admin/adminUser";
  }
  
  // 모임 관리 페이지
  @GetMapping("/adminMeet.page")
  public String adminMeet() {
    return "admin/adminMeet";
  }
  
  // 신고 관리 페이지
  @GetMapping("/adminReport.page")
  public String adminReport() {
    return "admin/adminReport";
  }
  
  // 카테고리 관리 페이지
  @GetMapping("/adminCategory.page")
  public String adminCategory() {
    return "admin/adminCategory";
  }
  
  // 문의 관리 페이지
  @GetMapping("/adminAsk.page")
  public String adminAsk() {
    return "admin/adminAsk";
  }
  

}
