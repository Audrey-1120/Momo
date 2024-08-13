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

@Service
public class MeetService {

  @Autowired
  private TagRepository tagRepository;
  
  @Autowired
  private CateRepository cateRepository;
  
  @Autowired
  private MeetingOwnerRepository meetingOwnerRepository;
  
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
  
  public ResponseEntity<PageDto<MeetingDto>> getMeetingList(Pageable pageable) {
    //Pageable pageable = PageRequest.of(page, 9, Sort.by(Sort.Direction.ASC, "meetingNo"));
    Page<Object[]> results = meetingOwnerRepository.findAllWithParticipants(pageable);
    
    List<MeetingDto> meetingList = results.get()
        .map(result -> {
            MeetingOwner meetingOwner = (MeetingOwner) result[0];
            Long participantsCount = (Long) result[1];
            
            MeetingDto dto = toMeetingDto(meetingOwner);
            dto.setParticipantsCount(participantsCount); // participantsCount 설정
            
            return dto;
        })
        .collect(Collectors.toList());
    
    // PageImpl을 사용하여 Page<MeetingDto> 생성
    // 1. 페이징 정보 제공
    // - PageImpl은 현재 페이지의 데이터 리스트, 전체 데이터의 수, 페이지 번호, 페이지 크기 등의 정보를 포함한다.
    // - 클라이언트는 이 정보를 통해 데이터의 총 개수, 페이지 수, 현재 페이지에 대한 정보를 알 수 있다.
    // 2. String Data의 표준
    //Page<MeetingDto> meetingDtoPage = new PageImpl<>(meetingList, pageable, results.getTotalElements());
    
    PageDto<MeetingDto> meetingDtoPage = new PageDto<MeetingDto>(meetingList, results.getNumber(), results.getSize(), results.getTotalElements());
    
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
