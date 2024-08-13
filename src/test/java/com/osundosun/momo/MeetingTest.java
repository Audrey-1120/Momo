package com.osundosun.momo;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.osundosun.momo.dto.MeetingDto;
import com.osundosun.momo.dto.PageDto;
import com.osundosun.momo.entity.MeetingOwner;
import com.osundosun.momo.repository.MeetingOwnerRepository;
import com.osundosun.momo.service.MeetService;

@SpringBootTest
@Transactional
public class MeetingTest {
  
  @Autowired
  private MeetingOwnerRepository meetingOwnerRepository;

  @BeforeEach
  public void before() {
    System.out.println("====== Test Before ======");
  }
  
  @AfterEach
  public void after(){
      System.out.println("====== Test After ======");
  }
  
  @Test
  @DisplayName("meeting데이터를 한페이지당 9개씩 가져온다.")
  void getMeetingTest() {
    System.out.println("---시작합니다---");
    Pageable pageable = PageRequest.of(0, 9, Sort.by(Sort.Direction.ASC, "meetingNo"));
    Page<Object[]> results = meetingOwnerRepository.findAllWithParticipants(pageable);
    
    List<MeetingDto> meetingList = results.getContent()
        .stream()
        .map(result -> {
            MeetingOwner meetingOwner = (MeetingOwner) result[0];
            Long participantsCount = (Long) result[1];
            
            MeetingDto dto = toMeetingDto(meetingOwner);
            dto.setParticipantsCount(participantsCount); // participantsCount 설정
            
            return dto;
        })
        .collect(Collectors.toList());
    
    PageDto<MeetingDto> meetingDtoPage = new PageDto<MeetingDto>(meetingList, results.getNumber(), results.getSize(), results.getTotalElements());
    System.out.println("---끝났습니다---");
  }
  
  // MeetingDto로 변환
  private MeetingDto toMeetingDto(MeetingOwner meetingOwner) {
    MeetingDto meetingDto = new MeetingDto();
    meetingDto.setMeetingNo(meetingOwner.getMeetingNo());
    meetingDto.setMeetingTitle(meetingOwner.getMeetingTitle());
    meetingDto.setCapacity(meetingOwner.getCapacity());
    meetingDto.setCreatedDate(meetingOwner.getCreatedDate());
    meetingDto.setLeaderNo(meetingOwner.getLeaderNo());
    meetingDto.setCateNo(meetingOwner.getCateNo());
    meetingDto.setTagNo(meetingOwner.getTagNo());
    
    return meetingDto;
    
  }
}
