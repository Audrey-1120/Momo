package com.osundosun.momo.utils;

import java.security.MessageDigest;

public class MySecurityUtils {
  
  public static String getSha256(String original) {
    StringBuilder builder = new StringBuilder();
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      digest.update(original.getBytes());
      byte[] bytes = digest.digest();
      for(int i = 0; i < bytes.length; i++) {
        builder.append(String.format("%02X", bytes[i]));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return builder.toString();
  }

}
