package com.osundosun.momo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TeamController {
  
  @GetMapping("/myMeeting.page")
  public String myMeeting() {
    return "meetingOwner/myMeeting";
  }
  
  @GetMapping("/createMeeting.page")
  public String createMeeting() {
    return "meetingOwner/createMeeting";
  }

  @GetMapping("/myMeetingDetail.page")
  public String  myMeetingDetail() {
    return "meetingOwner/myMeetingDetail";
  }
 
}
