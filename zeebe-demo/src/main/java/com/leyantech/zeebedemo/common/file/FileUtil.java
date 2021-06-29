package com.leyantech.zeebedemo.common.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author sunyan, yan.sun@leyantech.com
 * @date 2020/3/11.
 */
@Component
public class FileUtil {

  private static String fileUrl;

  @Value("${file.url}")
  public void setFileUrl(String fileUrl) {
    FileUtil.fileUrl = fileUrl;
  }

  public static boolean createDirectory(String directory){
    boolean result = false;
    File file = new File(directory);
    if(!file.exists()){
      System.out.println("FileUtils " + directory + " 文件不存在");
      result = file.mkdirs();
    }

    return result;
  }

  /**
   * 获取 Docker 容器下自定义 Resource 目录，不能直接使用java的resource目录
   * @return
   * @throws FileNotFoundException
   */
  public static String getDockerResourcePath() {
    System.out.println(fileUrl);
    String tmpPath = fileUrl;
    createDirectory(tmpPath);
    return tmpPath;
  }

  /**
   * 获取 Docker 容器下自定义 Resource 目录，不能直接使用java的resource目录
   * @return
   * @throws FileNotFoundException
   */
  public static String getDockerResourcePathPdf() {
    String tmpPath = fileUrl+LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+"/customerPdf/";
    createDirectory(tmpPath);
    return tmpPath;
  }


  /**
   * 获取 Docker 容器下自定义 Resource 目录，不能直接使用java的resource目录
   * @return
   * @throws FileNotFoundException
   */
  public static String getDockerResourcePathUpload() {
    String tmpPath = fileUrl+LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+"/upload/";
    createDirectory(tmpPath);
    return tmpPath;
  }

  public static String getBpmnFilePath(Long processId) {
    return FileUtil.getDockerResourcePath() + "/process-file-" + processId + ".bpmn";
  }

  public static void main(String[] args) {
    System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
  }
}
