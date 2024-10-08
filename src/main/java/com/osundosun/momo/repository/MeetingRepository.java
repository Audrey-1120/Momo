package com.osundosun.momo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.osundosun.momo.entity.MeetingEntity;

@Repository
public interface MeetingRepository extends JpaRepository<MeetingEntity, Number> {
  
   // MeetingOwner 엔티티랑 Count값 둘다 받으려고 Object[] 사용함.
   // 반환하는 배열의 첫번째 요소 Object[0]은 MeetingOwner 엔티티, Object[1]은 count 결과
   @Query("SELECT m, COUNT(p) AS participantsCount FROM MeetingEntity m LEFT JOIN m.participants p ON p.status = 1 GROUP BY m")
   Page<Object[]> findAllWithParticipants(Pageable pageable);
   
   // 카테고리 별로 조회
   @Query("SELECT m, COUNT(p) AS participantsCount FROM MeetingEntity m LEFT JOIN m.participants p ON p.status = 1 WHERE m.cateNo = :cateNo GROUP BY m")
   Page<Object[]> findAllWithParticipantsByCateNo(Pageable pageable, @Param("cateNo") long CateNo);
   
}
