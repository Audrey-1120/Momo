package com.osundosun.momo.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="MEETING_MNG_T")
public class MeetingMngEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "meeting_mng_seq_gen")
  @SequenceGenerator(name = "meeting_mng_seq_gen", sequenceName = "MEETING_MNG_SEQ", allocationSize = 1)
  private int mngNo;
  
  @Column(name = "MEMBER_NO")
  private int memberNo;
  
  @Column(name = "STATUS")
  private int status;
  
  @Column(name = "MEETING_DATE")
  private Timestamp meetingDate;
  
  @ManyToOne
  @JoinColumn(name = "MEETING_NO", nullable = false)
  private MeetingOwner meeting;
  
  
  
  

}
