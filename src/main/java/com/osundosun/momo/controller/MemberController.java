package com.osundosun.momo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.osundosun.momo.dto.MemberDto;
import com.osundosun.momo.entity.MemberEntity;
import com.osundosun.momo.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("/member")
@Controller
public class MemberController {
  
  private MemberService memberService;
  
  public MemberController(MemberService memberService) {
    super();
    this.memberService = memberService;
  }

  // 로그인 페이지 이동
  @GetMapping("/login.page")
  public String loginPage(HttpServletRequest request, Model model) {
    model.addAttribute("naverLoginURL", memberService.getNaverLoginURL(request));
    return "user/login";
  }
  
  // 접근 토큰 발급
  @GetMapping("/naver/getAccessToken.do")
  public String getAccessToken(HttpServletRequest request) {
    String accessToken = memberService.getNaverLoginAccessToken(request);
    return "redirect:/member/naver/getProfile.do?accessToken=" + accessToken;
  }
  
  // 네이버로부터 프로필 정보 받아오기
  @GetMapping("/naver/getProfile.do")
  public String getProfile(HttpServletRequest request, Model model) {
    
    // 네이버로부터 프로필 정보 받는다.
    MemberDto member = memberService.getNaverLoginProfile(request.getParameter("accessToken"));
    
    // 반환 경로 초기화
    String path = null;
    
    // 받은 프로필 정보가 member_t에 있는지 확인한다. (hasUser)
    if(memberService.hasUser(member.getEmail())) {
      // 1. 데이터가 있을 경우 로그인 시킨다.
      memberService.naverLogin(request, member);
      path = "redirect:/index.html";
      
    } else {
     // 2. 데이터가 없을 경우 회원가입 화면으로 이동한다. (naverSignup.html)
      model.addAttribute("member", member);
      path = "user/naverSignup";
    }
    return path;
  }
  
  // 네이버 로그인 -> 회원가입
  @PostMapping("/naver/naverSignup.do")
  public String naverSignup(MultipartHttpServletRequest multipartRequest, RedirectAttributes redirectAttributes) {
    
    // naverSignup 폼에서 제출한 데이터들을 request로 받아서 서비스에서 회원가입 처리한다.
    String result = memberService.naverSignup(multipartRequest).get("status").toString();
    
    if(result.equals("success")) {
      redirectAttributes.addFlashAttribute("message", "회원가입에 성공했어요!🤗");
    } else {
      redirectAttributes.addFlashAttribute("message", "회원가입에 실패했어요.😭");
    }
    return "redirect:/index.html";
  }
  
  @GetMapping("/signout.do")
  public void signout(HttpServletRequest request, HttpServletResponse response) {
    memberService.signout(request, response);
  }

}
