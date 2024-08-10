package com.osundosun.momo.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.osundosun.momo.dto.MemberDto;
import com.osundosun.momo.entity.MemberEntity;
import com.osundosun.momo.repository.MemberRepository;
import com.osundosun.momo.utils.MyFileUtils;
import com.osundosun.momo.utils.MySecurityUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
  
  @Value("${naver.client-id}")
  private String naverClientId;
  
  @Value("${naver.client-secret}")
  private String naverClientSecret;
  
  private final MemberRepository memberRepository;
  private final MyFileUtils myFileUtils;
  
  /*
   * public MemberServiceImpl(MemberRepository memberRepository, MyFileUtils
   * myFileUtils) { super(); this.memberRepository = memberRepository;
   * this.myFileUtils = myFileUtils; }
   */

  
  //네이버 로그인 요청 주소 생성해서 반환
  @Override
  public String getNaverLoginURL(HttpServletRequest request) {
    
    String redirectUri = "http://localhost:8080" + "/member/naver/getAccessToken.do";
    String state = new BigInteger(130, new SecureRandom()).toString();
    
    StringBuilder builder = new StringBuilder();
    builder.append("https://nid.naver.com/oauth2.0/authorize");
    builder.append("?response_type=code");
    builder.append("&client_id=" + naverClientId);
    builder.append("&redirect_uri=" + redirectUri);
    
    return builder.toString();
  }
  
  // 네이버로부터 Access Token을 발급 받아 반환
  @Override
  public String getNaverLoginAccessToken(HttpServletRequest request) {
    
    // 네이버 로그인 첫 단계에서 전달한 redirect_uri 에서 동작한다.
    // code와 state 파라미터를 받아서 AccessToken을 발급 받을 때 사용한다.
    String code = request.getParameter("code");
    String state = request.getParameter("state");
    
    String spec = "https://nid.naver.com/oauth2.0/token";
    String grantType = "authorization_code";
    String clientId = naverClientId;
    String clientSecret = naverClientSecret;
    
    StringBuilder builder = new StringBuilder();
    builder.append(spec);
    builder.append("?grant_type=" + grantType);
    builder.append("&client_id=" + clientId);
    builder.append("&client_secret=" + clientSecret);
    builder.append("&code=" + code);
    builder.append("&state=" + state);
    
    HttpURLConnection con = null;
    ObjectMapper obj = null;
    String accessToken = null;
    
    try {
      
      // 요청
      URL url = new URL(builder.toString());
      con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("GET");
      
      // 응답 스트림
      BufferedReader reader = null;
      int responseCode = con.getResponseCode();
      if(responseCode == HttpURLConnection.HTTP_OK) {
        reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      } else {
        reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
      }
      
      // 응답 데이터 받기
      String line = null;
      StringBuilder responseBody = new StringBuilder();
      while((line = reader.readLine())!= null) {
        responseBody.append(line);
      }
      
      // 응답 데이터 JSON 객체 변환
      obj = new ObjectMapper();
      JsonNode jsonNode = obj.readTree(responseBody.toString());
      accessToken = jsonNode.get("access_token").asText();
      
      // 응답 스트림 닫기
      reader.close();
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    con.disconnect();
    
    return accessToken;
  }
  
  // 네이버로부터 프로필 받기
  @Override
  public MemberDto getNaverLoginProfile(String accessToken) {

    // 1. 네이버로부터 프로필 정보 받기
    // 2. 프로필과 별명 설정 화면으로 이동!
    
    String spec = "https://openapi.naver.com/v1/nid/me";
    HttpURLConnection con = null;
    ObjectMapper obj = null;
    MemberDto member = null;
    
    try {
      
      URL url = new URL(spec);
      con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("GET");      
      
      // 요청 헤더
      con.setRequestProperty("Authorization", "Bearer " + accessToken);
      
      // 응답 스트림 생성
      BufferedReader reader = null;
      
      // 성공시 200이다.
      int responseCode = con.getResponseCode(); 
      if(responseCode == HttpURLConnection.HTTP_OK) {
        reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      } else {
        reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
      }
      
      // 회원이름, 연락처 이메일 주소, 별명, 프로필사진, 성별, 휴대전화번호
      String line = null;
      StringBuilder responseBody = new StringBuilder();
      while((line = reader.readLine()) != null) {
        responseBody.append(line);
      }
      
      // 응답 데이터 JSON 객체 변환
      obj = new ObjectMapper();
      JsonNode jsonNode = obj.readTree(responseBody.toString());
      JsonNode response = jsonNode.get("response");
      
      member = MemberDto.builder()
                      .email(response.get("email").asText())
                      .name(response.has("name") ? response.get("name").asText() : "none")
                      .nickName(response.has("nickname") ? response.get("nickname").asText() : "none")
                      .gender(response.has("gender") ? response.get("gender").asText() : "none")
                      .mobile(response.has("mobile") ? response.get("mobile").asText() : "none")
                  .build();

      reader.close();
      
      
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    con.disconnect();
    return member;
  }
  
  // Member_t에 멤버 객체 존재여부 확인
  @Override
  public boolean hasUser(String email) {
    // 해당 email에 해당하는 멤버 객체 있는가?
    return memberRepository.findByEmail(email) != null;
  }
  
  // 로그인 - 네이버
  @Override
  public void naverLogin(HttpServletRequest request, MemberDto member) {

    // 1. 해당 로그인 객체 가져오기
    MemberDto loginMember = toMemberDto(memberRepository.findByEmail(member.getEmail()));
    
    // 2. session에 저장하기
    request.getSession().setAttribute("member", loginMember);
    
  }
  
  // 회원가입 - 네이버
  @Override
  public Map<String, Object> naverSignup(MultipartHttpServletRequest multipartRequest) {

    // 반환할 Map
    Map<String, Object> map = new HashMap<>();
    
    try {
      
      // 받은 회원 데이터
      String email = MySecurityUtils.getPreventXss(multipartRequest.getParameter("email"));  
      String name = MySecurityUtils.getPreventXss(multipartRequest.getParameter("name"));  
      String gender = MySecurityUtils.getPreventXss(multipartRequest.getParameter("gender"));  
      String tagNo = MySecurityUtils.getPreventXss(multipartRequest.getParameter("tagNo"));  
      String nickName = MySecurityUtils.getPreventXss(multipartRequest.getParameter("nickName"));
      String mobile = MySecurityUtils.getPreventXss(multipartRequest.getParameter("mobile"));
      
      
      // 프로필 이미지 업로드 및 경로 반환
      String profilePath = "";
      
      if(multipartRequest.getFile("profileImage") != null) {
        // 만약에 이미지 file 데이터가 없다면 "default"라고 저장한다.
        profilePath = "default";
      } else {
        profilePath = myFileUtils.setProfilePicture(multipartRequest, "profileImage");
      }
        
        
      
      // DB에 저장할 회원 데이터 생성
      MemberEntity member = new MemberEntity();
      member.setEmail(email);
      member.setPw("none");
      member.setName(name);
      member.setNickName(nickName);
      member.setMobile(mobile);
      member.setGender(gender);
      member.setProfilePath(profilePath);
      member.setSignupKind(0);
      member.setTagNo(tagNo);
      member.setRole(1);
      
      // DB에 회원 데이터 저장
      MemberEntity loginMember = memberRepository.save(member);
      
      if(loginMember != null) {
        // session 추가
        multipartRequest.getSession().setAttribute("member", loginMember);
        map.put("status", "success");
      } else {
        map.put("status", "error");
      }
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    return map;
  }
  
  // 로그아웃
  @Override
  public void signout(HttpServletRequest request, HttpServletResponse response) {
    try {
      request.getSession().invalidate();
      response.sendRedirect("/index.html");
    } catch (Exception e) {
      e.printStackTrace();
    }
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
