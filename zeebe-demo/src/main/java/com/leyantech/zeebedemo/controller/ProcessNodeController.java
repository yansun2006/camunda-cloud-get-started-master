package com.leyantech.zeebedemo.controller;

import com.leyantech.zeebedemo.common.base.ServerResponse;
import com.leyantech.zeebedemo.controller.vo.FieldSubmitVo;
import com.leyantech.zeebedemo.controller.vo.NodeVo;
import com.leyantech.zeebedemo.service.ProcessNodeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yan.sun, {@literal yan.sun@leyantech.com}
 * @date 2021-06-28.
 */

@RestController
@RequestMapping("/node")
public class ProcessNodeController extends BaseController {

  @Autowired
  private ProcessNodeService nodeService;

  /**
   * 节点定义列表(包含节点参数定义).
   */
  @GetMapping("/def/list")
  public ServerResponse getNodeDefList() {
    return ServerResponse.createBySuccess(nodeService.getNodeDefList());
  }

  /**
   * 节点字段和值列表.
   * nodeBpmnId 和 nodeType.
   */
  @PostMapping("/field/list")
  public ServerResponse getNodeFieldList(@RequestBody NodeVo nodeVo) {
    return ServerResponse.createBySuccess(nodeService.getNodeFieldList(nodeVo));
  }

  /**
   * 添加节点.
   */
  @PostMapping("/save")
  public ServerResponse saveNode(@RequestBody NodeVo nodeVo) {
    nodeService.saveNode(nodeVo);
    return ServerResponse.createBySuccess();
  }

  /**
   * 删除节点(考虑是否逻辑删除).
   */
  @DeleteMapping("/delete/{nodeBpmnId}")
  public ServerResponse deleteNode(@PathVariable("nodeBpmnId") String nodeBpmnId) {
    nodeService.deleteNode(nodeBpmnId);
    return ServerResponse.createBySuccess();
  }

  /**
   * 保存节点参数.
   */
  @PostMapping("/save/params")
  public ServerResponse saveParams(@RequestBody FieldSubmitVo fieldSubmitVo) {
    nodeService.saveParams(fieldSubmitVo);
    return ServerResponse.createBySuccess();
  }
}
