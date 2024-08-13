package com.osundosun.momo.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.osundosun.momo.dto.CateDto;
import com.osundosun.momo.dto.MeetingDto;
import com.osundosun.momo.dto.PageDto;
import com.osundosun.momo.entity.CateEntity;
import com.osundosun.momo.entity.MeetingOwner;
import com.osundosun.momo.repository.CateRepository;
import com.osundosun.momo.repository.MeetingOwnerRepository;
import com.osundosun.momo.repository.TagRepository;
import com.osundosun.momo.utils.MyPageUtils;

@Service
public class MeetService {

  @Autowired
  private TagRepository tagRepository;
  
  @Autowired
  private CateRepository cateRepository;
  
  @Autowired
  private MeetingOwnerRepository meetingOwnerRepository;
  
  @Autowired
  private MyPageUtils myPageUtils;
  
  public ResponseEntity<Map<String, Object>> getTagList() {
    return new ResponseEntity<>(Map.of("tagList", tagRepository.findAll()), HttpStatus.OK);
  }
  
  // 채원 - 카테고리 데이터 가져오기
  public ResponseEntity<Map<String, Object>> getCategory() {
    List<CateDto> categoryList = cateRepository.findAll()
                                      .stream()
                                      .map(cate -> toCateDto(cate))
                                      .collect(Collectors.toList());
    
    return new ResponseEntity<>(Map.of("categoryList", categoryList), HttpStatus.OK);
  }
  
  // 채원 - 모임 데이터 가져오기
  public ResponseEntity<PageDto<MeetingDto>> getMeetingList(Pageable pageable) {
    //Pageable pageable = PageRequest.of(page, 9, Sort.by(Sort.Direction.ASC, "meetingNo"));
    
    // 페이지는 0부터 시작이므로 0 이상이면 1빼주기
    int pageNumber = (pageable.getPageNumber() > 0) ? pageable.getPageNumber() - 1: 0;
    
    // 페이지 번호 조정해서 새롭게 생성!
    Pageable FinalPageable = PageRequest.of(pageNumber, pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "meetingNo"));
    
    Page<Object[]> results = meetingOwnerRepository.findAllWithParticipants(FinalPageable);
    
    // 리스트 돌면서 participantsCount 설정해주기!
    List<MeetingDto> meetingList = results.get()
        .map(result -> {
            MeetingOwner meetingOwner = (MeetingOwner) result[0];
            Long participantsCount = (Long) result[1];
            
            MeetingDto dto = toMeetingDto(meetingOwner);
            dto.setParticipantsCount(participantsCount); // participantsCount 설정
            
            return dto;
        })
        .collect(Collectors.toList());
    
    // totalPage, BeginPage 구하기 위한 페이징 세팅
    myPageUtils.setPaging(results.getTotalElements(), 9, results.getNumber());
    
    // PageDto 객체로 반환
    PageDto<MeetingDto> meetingDtoPage = new PageDto<MeetingDto>(meetingList, results.getNumber(), results.getTotalPages(), myPageUtils.getBeginPage(), myPageUtils.getEndPage());
    
    return ResponseEntity.ok(meetingDtoPage);
}
  
  // cateDto로 변환
  private CateDto toCateDto(CateEntity cateEntity) {
    CateDto cateDto = new CateDto();
    cateDto.setCateNo(cateEntity.getCateNo());
    cateDto.setTopCate(cateEntity.getTopCate());
    cateDto.setParentNo(cateEntity.getParentNo());
    cateDto.setCateName(cateEntity.getCateName());
    cateDto.setCreatedDate(cateEntity.getCreatedDate());
    return cateDto;
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
