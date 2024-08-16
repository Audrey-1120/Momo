package com.osundosun.momo.utils;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Component
public class MyFileUtils {

  @Value("${service.file.uploadurl}")
  public String UP_DIR;
  
  // 날짜 기준으로 디렉토리 생성
  
  // 현재 날짜
  public static final LocalDate TODAY = LocalDate.now();
  
  // 프로필 이미지 경로 반환
  public String getUploadPath() {
    return UP_DIR + "/profile" + DateTimeFormatter.ofPattern("/yyyy/MM/dd").format(TODAY);
    // C드라이브 root임. C드라이브/profile/yyyy/MM/dd
  }
  
  // 저장될 파일명 반환
  public String getFilesystemName(String originalFilename) {
    
    // 파일명.확장자 - 파일명 부분은 저장할때 사용 X, 확장자만 살려서 사용한다.
    // 일부 확장자의 경우, 확장자에 .이 포함된다. .tar.gz - 이 부분도 처리해주기
    String extName = null;
    if(originalFilename.endsWith(".tar.gz")) {
      extName = ".tar.gz";
    } else {
      // 마지막 .부터 끝까지 추출한다.
      extName = originalFilename.substring(originalFilename.lastIndexOf("."));
    }
    return UUID.randomUUID().toString().replace("-", "") + extName;
  }
  
  // DB에 저장할 profile_path 반환
  public String setProfilePicture(MultipartHttpServletRequest multipartRequest, String paramName) {
    
    // 첨부 파일 목록
    MultipartFile profile = multipartRequest.getFile(paramName);
    
    String profilePath = "";
      
      if(profile != null || !profile.isEmpty() && profile.getSize() > 0) {
        
        StringBuilder builder = new StringBuilder();
        
        // 첨부파일 경로
        String uploadPath = getUploadPath();
        
        // 첨부 파일 경로 디렉터리 생성
        File dir = new File(uploadPath);
        if(!dir.exists()) {
          dir.mkdirs();
        }
        
        // 첨부 파일 original name
        String originlFileName = profile.getOriginalFilename();
        
        // 첨부 파일 저장 이름
        String filesystemName = getFilesystemName(originlFileName);
        String relativePath = uploadPath.substring(UP_DIR.length());
        profilePath = builder.append(relativePath).append("/") .append(filesystemName).toString();
        builder.setLength(0);
        
        // 첨부 파일 File 객체
        File file = new File(dir, filesystemName);
        
        try {
          
          profile.transferTo(file);
          
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      return profilePath;
  }
  
  

}
