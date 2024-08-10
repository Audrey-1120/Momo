package com.osundosun.momo.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.osundosun.momo.dto.MemberDto;
import com.osundosun.momo.entity.MemberEntity;
import com.osundosun.momo.repository.AdminUserRepository;
import com.osundosun.momo.utils.MySecurityUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class AdminUserServiceImpl implements AdminUserService {
  
  private AdminUserRepository adminUserRepository;

  public AdminUserServiceImpl(AdminUserRepository adminUserRepository) {
    super();
    this.adminUserRepository = adminUserRepository;
  }

  @Override
  public Map<String, Object> login(HttpServletRequest request) {
    
    try {
      
      Map<String, Object> statusMap = new HashMap<>();
      
      // email과 pw 받기
      String email = request.getParameter("email");
      String pw = MySecurityUtils.getSha256(request.getParameter("pw"));
      
      // 접속 기록
      String ip = request.getRemoteAddr();
      
      // 접속 수단
      String userAgent = request.getHeader("User-Agent");
      
      // DB에 해당 회원 데이터 있는지 조회
      MemberEntity member = adminUserRepository.findByEmailAndPwAndRole(email, pw, 0);
      
      // 회원 정보가 있는 경우!
      if(member != null) {
        
        // MemberDto 형태로 변환
        MemberDto loginMember = toMemberDto(member);
        
        // 세션에 회원정보 추가
        HttpSession session = request.getSession();
        session.setAttribute("member", member);
        
        // statusMap에 status값 추가
        statusMap.put("status", "success");
        statusMap.put("loginMember", loginMember);
        
        return statusMap;
        
      } else {
        
        statusMap.put("status", "error");
        statusMap.put("message", "아이디나 비밀번호를 확인해주세요.");
        
        return statusMap;
        
      }
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return null;
    
    }
  
  
    // MemberEntity -> MemberDto 객체로 변환
  private MemberDto toMemberDto(MemberEntity memberEntity) {
    
    MemberDto memberDto = new MemberDto();
    memberDto.setMemberNo(memberEntity.getMemberNo());
    memberDto.setEmail(memberEntity.getEmail());
    memberDto.setPw(memberEntity.getPw());
    memberDto.setName(memberEntity.getName());
    memberDto.setNickName(memberEntity.getNickName());
    memberDto.setMobile(memberEntity.getMobile());
    memberDto.setGender(memberEntity.getGender());
    memberDto.setProfilePath(memberEntity.getProfilePath());
    memberDto.setSignupKind(memberEntity.getSignupKind());
    memberDto.setSignupDate(memberEntity.getSignupDate());
    memberDto.setTagNo(memberEntity.getTagNo());
    memberDto.setRole(memberEntity.getRole());
    return memberDto;
  }
  }

