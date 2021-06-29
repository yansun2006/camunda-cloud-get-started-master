package com.leyantech.zeebedemo.controller;

import com.leyantech.zeebedemo.common.base.ServerResponse;
import com.leyantech.zeebedemo.service.ProcessInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yan.sun, {@literal yan.sun@leyantech.com}
 * @date 2021-06-22.
 */
@RestController
@RequestMapping("/process")
public class ProcessInfoController extends BaseController {

  @Autowired
  private ProcessInfoService processInfoService;

  /**
   * 流程列表.
   */
  @GetMapping("/list")
  public ServerResponse getProcessList() {
    return ServerResponse.createBySuccess(processInfoService.getProcessInfoList());
  }

  /**
   * 部署流程.
   */
  @GetMapping("/deploy/{processId}")
  public ServerResponse depolyProcess(@PathVariable("processId") Long processId) {
    processInfoService.depolyProcess(processId);
    return ServerResponse.createBySuccess();
  }

  /**
   * 启动流程.
   */
  @GetMapping("/start/{processId}")
  public ServerResponse startProcess(@PathVariable("processId") Long processId) {
    processInfoService.startProcess(processId);
    return ServerResponse.createBySuccess();
  }


}
