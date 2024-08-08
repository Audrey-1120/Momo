package com.osundosun.momo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
 
}  
  
    