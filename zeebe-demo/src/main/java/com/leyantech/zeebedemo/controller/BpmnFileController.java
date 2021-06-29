package com.leyantech.zeebedemo.controller;

import com.leyantech.zeebedemo.common.base.ServerResponse;
import com.leyantech.zeebedemo.controller.vo.BpmnFileVo;
import com.leyantech.zeebedemo.service.BpmnFileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author yan.sun, {@literal yan.sun@leyantech.com}
 * @date 2021-06-22.
 */
@RestController
@RequestMapping("/bpmn-file")
public class BpmnFileController extends BaseController {

  @Autowired
  private BpmnFileService bpmnFileService;

  /**
   * 获取流程文件.
   */
  @GetMapping("/{processId}")
  public ServerResponse getProcessFile(@PathVariable("processId") Long processId) throws IOException {
    return ServerResponse.createBySuccess(bpmnFileService.getProcessFile(processId));
  }

  /**
   * 保存流程文件.
   */
  @PostMapping("/save")
  public ServerResponse saveProcessFile(@RequestBody BpmnFileVo bpmnFileVo) throws IOException {
    return ServerResponse.createBySuccess(bpmnFileService.saveProcessFile(bpmnFileVo));
  }

}
