package com.osundosun.momo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.osundosun.momo.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;

@RequestMapping("/member")
@Controller
public class MemberController {
  
  private MemberService userService;
  
  public MemberController(MemberService userService) {
    super();
    this.userService = userService;
  }

  // 로그인 페이지 이동
  @GetMapping("/login.page")
  public String loginPage(HttpServletRequest request, Model model) {
    model.addAttribute("naverLoginURL", userService.getNaverLoginURL(request));
    return "user/login";
  }
  
  @GetMapping("/naver/getAccessToken.do")
  public String getAccessToken(HttpServletRequest request) {
    String accessToken = "네이버에서 accessToken 얻기";
    return "redirect:/user/naver/getProfile.do?accessToken=" + accessToken;
  }

}
