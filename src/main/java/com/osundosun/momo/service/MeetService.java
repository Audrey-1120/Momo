package com.osundosun.momo.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import com.osundosun.momo.dto.TagDto;
import com.osundosun.momo.entity.CateEntity;
import com.osundosun.momo.entity.MeetingOwner;
import com.osundosun.momo.entity.Tag;
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
  public ResponseEntity<Map<String, Object>> getMeetingList(Pageable pageable, long cateNo) {
    
    // 페이지는 0부터 시작이므로 0 이상이면 1빼주기
    int pageNumber = (pageable.getPageNumber() > 0) ? pageable.getPageNumber() - 1: 0;
    
    // 페이지 번호 조정해서 새롭게 생성!
    Pageable FinalPageable = PageRequest.of(pageNumber, pageable.getPageSize(), pageable.getSort());
    
    Page<Object[]> results = null;
    
    // cateNo가 0이면 전체 조회, 0이 아니면 cateNo 별로 조회
    if(cateNo == 0) {
      results = meetingOwnerRepository.findAllWithParticipants(FinalPageable);
    } else {
      results = meetingOwnerRepository.findAllWithParticipantsByCateNo(FinalPageable, cateNo);
    }
    
    // TagName 가져오기 위해서 TagNo담아줄 Set 생성
    Set<Integer> tagNoList = new HashSet<>();
    
    // CateName 가져오기 위해서 CateNo 담아줄 Set 생성
    Set<Long> cateNoList = new HashSet<>();
    
    // 리스트 돌면서 participantsCount 설정해주기!
    List<MeetingDto> meetingList = results.get()
        .map(result -> {
            MeetingOwner meetingOwner = (MeetingOwner) result[0];
            Long participantsCount = (Long) result[1];
            
            MeetingDto dto = toMeetingDto(meetingOwner);
            dto.setParticipantsCount(participantsCount); // participantsCount 설정
            
            // tagNo값 split해서 Set에 저장한다. -> 태그 데이터 가져오기위함.
            String[] tagNum = dto.getTagNo().split(",");
            for(String tag : tagNum) {
              tagNoList.add(Integer.parseInt(tag));
            }
            
            // cateNo값 Set에 저장한다. -> 카테고리 이름 가져오기 위함.
            cateNoList.add(dto.getCateNo());
            
            return dto;
        })
        .collect(Collectors.toList());
    
    // totalPage, BeginPage 구하기 위한 페이징 세팅
    myPageUtils.setPaging(results.getTotalElements(), 9, results.getNumber() + 1);
    
    // PageDto 객체로 반환
    PageDto<MeetingDto> meetingDtoPage = new PageDto<MeetingDto>(meetingList, results.getNumber() + 1, results.getTotalPages(), myPageUtils.getBeginPage(), myPageUtils.getEndPage());
    
    
    // TagDto 객체로 변환
    List<Tag> tagBeforeList = tagRepository.findAllByTagNoIn(tagNoList);
    List<TagDto> tagList = new ArrayList<>();
    
    for(Tag tag: tagBeforeList) {
      tagList.add(toTagDto(tag));
    }
    
    // CateDto 객체로 변환
    List<CateEntity> cateBeforeList = cateRepository.findAllByCateNoIn(cateNoList);
    List<CateDto> cateList = new ArrayList<>();
    
    for(CateEntity category : cateBeforeList) {
      cateList.add(toCateDto(category));
    }
    
    return ResponseEntity.ok(Map.of("meetingDtoPage", meetingDtoPage
                                  , "cateList", cateList
                                  , "tagList", tagList));
}
  
  
  
  
  
  
  
  
  
  
  
  
  
  // tagDto로 변환
  private TagDto toTagDto(Tag tag) {
    
    TagDto cateDto = TagDto.builder()
                        .tagNo(tag.getTagNo())
                        .tagName(tag.getTagName())
                        .createdDate(tag.getCreatedDate())
                       .build();
    return cateDto;
    
  }
  
  // cateDto로 변환
  private CateDto toCateDto(CateEntity cateEntity) {
    
    CateDto cateDto = CateDto.builder()
                          .cateNo(cateEntity.getCateNo())
                          .topCate(cateEntity.getTopCate())
                          .parentNo(cateEntity.getParentNo())
                          .cateName(cateEntity.getCateName())
                          .createdDate(cateEntity.getCreatedDate())
                        .build();
    return cateDto;
  }
  
  // MeetingDto로 변환
  private MeetingDto toMeetingDto(MeetingOwner meetingOwner) {
    
    MeetingDto meetingDto = MeetingDto.builder()
                                .meetingNo(meetingOwner.getMeetingNo())
                                .meetingTitle(meetingOwner.getMeetingTitle())
                                .capacity(meetingOwner.getCapacity())
                                .createdDate(meetingOwner.getCreatedDate())
                                .leaderNo(meetingOwner.getLeaderNo())
                                .cateNo(meetingOwner.getCateNo())
                                .tagNo(meetingOwner.getTagNo())
                              .build();
    return meetingDto;
    
  }
  
  
}
