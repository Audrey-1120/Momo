package com.osundosun.momo.controller;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.osundosun.momo.dto.MeetingDto;
import com.osundosun.momo.dto.PageDto;
import com.osundosun.momo.entity.MeetingEntity;
import com.osundosun.momo.service.MeetService;

@Controller
public class MeetController {
  
  @Autowired
  private MeetService meetService;
  
  @GetMapping("/myMeeting.page")
  public String myMeeting() {
    return "meetingOwner/myMeeting";
  }
  
  @GetMapping("/createMeeting.page")
  public String createMeeting() {
    return "meetingOwner/createMeeting";
  }

  @GetMapping("/myMeetingDetail.page")
  public String myMeetingDetail() {
    return "meetingOwner/myMeetingDetail";
  }
  
  @GetMapping("/chatiing.page")
  public String  chatting() {
    return "meetingOwner/chat";
  }
  
  @GetMapping("/meetingDetail.page")
  public String meetingDetail() {
	  return "meeting/meetingDetail";
  }
  
  @GetMapping(value="/getTagList.do", produces="application/json")
  public ResponseEntity<Map<String, Object>> getTagList() {
    
    //여기서 서비스 이어주기. 될랑가 된다!
    return meetService.getTagList();
  }
  
  // ------ 채원 -----
  // 모임 전체 조회 페이지 이동
  @GetMapping("/meetingInquiry.page")
  public String meetingInquiry() {
    return "meeting/meetingInquiry";
  }
  
  // 카테고리 데이터 가져오기
  @GetMapping("/getCategory.do")
  public ResponseEntity<Map<String, Object>> getCategory() {
    return meetService.getCategory();
  }
  
  // 모임 데이터 가져오기
  @GetMapping("/getMeetingList.do")
  public ResponseEntity<Map<String, Object>> getMeetingList(@PageableDefault(size = 9, sort= {"createdDate,DESC"}) Pageable pageable, @RequestParam long cateNo) {
    // @PageableDefault를 사용해서 page, size, sort 정해주기 
    return meetService.getMeetingList(pageable, cateNo);
  }
  
}  
  
    