package com.osundosun.momo.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.osundosun.momo.dto.MemberDto;
import com.osundosun.momo.service.AdminUserService;
import com.osundosun.momo.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RequestMapping("/admin")
@Controller
public class AdminController {
  
  private AdminUserService adminUserService;
  
  public AdminController(AdminUserService adminUserService) {
    super();
    this.adminUserService = adminUserService;
  }

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
  
  // 관리자 로그인 페이지 이동
  @GetMapping("/adminLogin.page")
  public String adminLogin() {
    return "admin/adminLogin";
  }
  
  // 관리자 로그인
  @PostMapping("/login.do")
  public String login(HttpServletRequest request, Model model) {
    
    // 결과값 받을 Map 생성
    Map<String, Object> result = adminUserService.login(request);
    
    // status값이 success이면 메인화면으로..
    if(result.get("status").equals("success")) {
      model.addAttribute("loginMember", result.get("loginMember"));
      return "redirect:/admin/adminUser.page";
    } else {
      // 로그인 실패 시 에러메시지와 함께 로그인 화면으로..
      String errorMessage = result.get("message").toString();
      if(errorMessage != null ) {
        model.addAttribute("errorMessage", errorMessage);
      }
      return "admin/adminLogin";
    }
    
  }
  
  
  
  

}
