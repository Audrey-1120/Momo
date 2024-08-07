package jisunJpa;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "MEETING_T")
public class MeetingOwner {
  
  //기본키 <- 이 기본키 타입을 extend 할 때 오른쪽에 써주는 것. 아무거나 채워놓는 부분 아니었음...
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "MEETING_NO", nullable = false)
  private int meetingNo;
  
  //그 외(name 안쓰면 기본적으로는 카멜케이스 -> 스네이크케이스)
  @Column(length = 30, nullable = false)
  private String meetingTitle;
  
  @Column(nullable = false)
  private long capacity;
  
  @Column(nullable = false)
  private Timestamp createdDate;
  
  @Column(nullable = false)
  private long cateNo;

  @Column(length = 30, nullable = false)
  private String tagNo;
  
  @Column(nullable = false)
  private long memberNo;
  
}
