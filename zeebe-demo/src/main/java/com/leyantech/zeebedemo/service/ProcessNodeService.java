package com.leyantech.zeebedemo.service;

import static com.leyantech.zeebedemo.db.zeebe_demo_db.Tables.T_PROCESS_NODE;
import static com.leyantech.zeebedemo.db.zeebe_demo_db.Tables.T_PROCESS_NODE_FIELD_OPTION;
import static com.leyantech.zeebedemo.db.zeebe_demo_db.Tables.T_PROCESS_NODE_FIELD_VALUE;
import static com.leyantech.zeebedemo.db.zeebe_demo_db.tables.TProcessNodeDef.T_PROCESS_NODE_DEF;
import static com.leyantech.zeebedemo.db.zeebe_demo_db.tables.TProcessNodeField.T_PROCESS_NODE_FIELD;

import com.leyantech.zeebedemo.common.constants.Constants;
import com.leyantech.zeebedemo.common.exception.RestException;
import com.leyantech.zeebedemo.controller.vo.FieldSubmitVo;
import com.leyantech.zeebedemo.controller.vo.FieldSubmitVo.FieldAnswerVo;
import com.leyantech.zeebedemo.controller.vo.NodeDefListVo;
import com.leyantech.zeebedemo.controller.vo.NodeDefListVo.XmlDefVo;
import com.leyantech.zeebedemo.controller.vo.NodeDefVo;
import com.leyantech.zeebedemo.controller.vo.NodeDefVo.FieldOptionVo;
import com.leyantech.zeebedemo.controller.vo.NodeDefVo.PropertyVo;
import com.leyantech.zeebedemo.controller.vo.NodeVo;
import com.leyantech.zeebedemo.db.zeebe_demo_db.tables.pojos.TProcessNodeDef;
import com.leyantech.zeebedemo.db.zeebe_demo_db.tables.pojos.TProcessNodeField;
import com.leyantech.zeebedemo.db.zeebe_demo_db.tables.pojos.TProcessNodeFieldValue;
import com.leyantech.zeebedemo.db.zeebe_demo_db.tables.records.TProcessNodeFieldValueRecord;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yan.sun, {@literal yan.sun@leyantech.com}
 * @date 2021-06-28.
 */
@Service
public class ProcessNodeService {

  private static final Logger LOGGER = LogManager.getLogger(ProcessNodeService.class);

  @Autowired
  private DSLContext dslContext;

  public NodeDefListVo getNodeDefList() {
    NodeDefListVo nodeDefListVo = new NodeDefListVo();
    nodeDefListVo.setName(Constants.BPMN_NODE_LIST_NAME);
    nodeDefListVo.setUri(Constants.BPMN_URI);
    nodeDefListVo.setPrefix(Constants.BPMN_PREFIX);
    nodeDefListVo.setXml(new XmlDefVo(Constants.BPMN_TAG_ALIAS));
    nodeDefListVo.setTypes(Lists.newArrayList());

    // 查询节点定义列表
    List<TProcessNodeDef> nodeDefList = dslContext.select()
        .from(T_PROCESS_NODE_DEF)
        .orderBy(T_PROCESS_NODE_DEF.POSITION.asc())
        .fetch()
        .into(TProcessNodeDef.class);
    if (CollectionUtils.isEmpty(nodeDefList)) {
      return nodeDefListVo;
    }

    // 查询节点参数列表
    List<Long> nodeDefIdList = nodeDefList.stream().map(TProcessNodeDef::getId).collect(Collectors.toList());
    List<TProcessNodeField> nodeFieldList = dslContext.select(T_PROCESS_NODE_FIELD.ID, T_PROCESS_NODE_FIELD.NODE_DEF_ID,
        T_PROCESS_NODE_FIELD.FIELD_NAME, T_PROCESS_NODE_FIELD.FIELD_FORMAT, T_PROCESS_NODE_FIELD.DEFAULT_VALUE)
        .from(T_PROCESS_NODE_FIELD)
        .where(T_PROCESS_NODE_FIELD.NODE_DEF_ID.in(nodeDefIdList))
        .orderBy(T_PROCESS_NODE_FIELD.NODE_DEF_ID.asc(), T_PROCESS_NODE_FIELD.POSITION.asc())
        .fetch()
        .into(TProcessNodeField.class);

    // 合并Vo数据
    mergeNodeDefData(nodeDefListVo, nodeDefList, nodeFieldList);
    return nodeDefListVo;
  }

  private void mergeNodeDefData(NodeDefListVo nodeDefListVo, List<TProcessNodeDef> nodeDefList,
      List<TProcessNodeField> nodeFieldList) {
    // 组装 NodeDefVo
    Map<Long, NodeDefVo> nodeDefVoMap = Maps.newHashMap();
    for (TProcessNodeDef nodeDef : nodeDefList) {
      var nodeDefVo = new NodeDefVo();
      nodeDefVo.setName(nodeDef.getNodeName());
      nodeDefVo.setJobType(nodeDef.getNodeType());
      nodeDefVo.setSuperClass(Collections.singletonList(Constants.BPMN_SERVICE_TASK_SUPER_CLASS));
      nodeDefVo.setProperties(Lists.newArrayList());
      nodeDefListVo.getTypes().add(nodeDefVo);
      nodeDefVoMap.put(nodeDef.getId(), nodeDefVo);
    }

    // 组装 PropertyVo 字段Vo
    for (TProcessNodeField nodeField : nodeFieldList) {
      var propertyVo = new PropertyVo();
      propertyVo.setFieldId(nodeField.getId());
      propertyVo.setName(nodeField.getFieldName());
      propertyVo.setAttr(true);
      propertyVo.setType(nodeField.getFieldFormat());

      // 加载选型列表（由于是demo，后期有类似功能避免在循环里面查询DB）
      if (Constants.FIELD_FORMAT_LIST.equalsIgnoreCase(propertyVo.getType())) {
        propertyVo.setFieldOptionVoList(getFieldOptionList(nodeField.getId()));
      }

      var nodeDefVo = nodeDefVoMap.get(nodeField.getNodeDefId());
      if (nodeDefVo != null) {
        nodeDefVo.getProperties().add(propertyVo);
      }
    }
  }

  private List<FieldOptionVo> getFieldOptionList(Long fieldId) {
    return dslContext.select()
        .from(T_PROCESS_NODE_FIELD_OPTION)
        .where(T_PROCESS_NODE_FIELD_OPTION.FIELD_ID.eq(fieldId))
        .orderBy(T_PROCESS_NODE_FIELD_OPTION.OPTION_KEY.asc())
        .fetch()
        .into(FieldOptionVo.class);
  }

  public NodeDefVo getNodeFieldList(NodeVo nodeVo) {
    // 节点定义是否存在
    Record nodeDefRecord = dslContext.select()
        .from(T_PROCESS_NODE_DEF)
        .where(T_PROCESS_NODE_DEF.NODE_TYPE.eq(nodeVo.getNodeType()))
        .fetchAny();
    if (nodeDefRecord == null) {
      throw new RestException("node def is not exist.");
    }
    TProcessNodeDef nodeDef = nodeDefRecord.into(TProcessNodeDef.class);

    var nodeDefVo = new NodeDefVo();
    nodeDefVo.setName(nodeDef.getNodeName());
    nodeDefVo.setJobType(nodeDef.getNodeType());
    nodeDefVo.setSuperClass(Collections.singletonList(Constants.BPMN_SERVICE_TASK_SUPER_CLASS));
    nodeDefVo.setProperties(Lists.newArrayList());
    nodeDefVo.setAnswers(Lists.newArrayList());

    //加载字段列表
    getFieldListByNodeId(nodeDefVo, nodeDef);
    // 加载字段值列表
    getFieldValueListByNodeId(nodeDefVo, nodeVo);
    return nodeDefVo;
  }

  private void getFieldListByNodeId(NodeDefVo nodeDefVo, TProcessNodeDef nodeDef) {
    List<TProcessNodeField> nodeFieldList = dslContext.select(T_PROCESS_NODE_FIELD.ID, T_PROCESS_NODE_FIELD.NODE_DEF_ID,
        T_PROCESS_NODE_FIELD.FIELD_NAME, T_PROCESS_NODE_FIELD.FIELD_FORMAT, T_PROCESS_NODE_FIELD.DEFAULT_VALUE)
        .from(T_PROCESS_NODE_FIELD)
        .where(T_PROCESS_NODE_FIELD.NODE_DEF_ID.eq(nodeDef.getId()))
        .orderBy(T_PROCESS_NODE_FIELD.POSITION.asc())
        .fetch()
        .into(TProcessNodeField.class);

    // 组装 PropertyVo 字段Vo
    for (TProcessNodeField nodeField : nodeFieldList) {
      var propertyVo = new PropertyVo();
      propertyVo.setFieldId(nodeField.getId());
      propertyVo.setName(nodeField.getFieldName());
      propertyVo.setAttr(true);
      propertyVo.setType(nodeField.getFieldFormat());

      // 加载选型列表（由于是demo，后期有类似功能避免在循环里面查询DB）
      if (Constants.FIELD_FORMAT_LIST.equalsIgnoreCase(propertyVo.getType())) {
        propertyVo.setFieldOptionVoList(getFieldOptionList(nodeField.getId()));
      }
      nodeDefVo.getProperties().add(propertyVo);
    }
  }

  private void getFieldValueListByNodeId(NodeDefVo nodeDefVo, NodeVo nodeVo) {
    // 检查节点是否存在
    Record1<Long> record1 = dslContext.select(T_PROCESS_NODE.ID)
        .from(T_PROCESS_NODE)
        .where(T_PROCESS_NODE.NODE_BPMN_ID.eq(nodeVo.getNodeBpmnId()))
        .fetchAny();
    if (record1 == null) {
      LOGGER.info("node is not exist.");
      throw new RestException("node is not exist.");
    }
    Long nodeId = record1.value1();
    List<TProcessNodeFieldValue> fieldValueList = dslContext
        .select(T_PROCESS_NODE_FIELD_VALUE.ID, T_PROCESS_NODE_FIELD_VALUE.FIELD_ID,
            T_PROCESS_NODE_FIELD_VALUE.FIELD_VALUE, T_PROCESS_NODE_FIELD_VALUE.OPTION_KEY)
        .from(T_PROCESS_NODE_FIELD_VALUE)
        .where(T_PROCESS_NODE_FIELD_VALUE.NODE_ID.eq(nodeId))
        .orderBy(T_PROCESS_NODE_FIELD_VALUE.FIELD_ID.asc())
        .fetch()
        .into(TProcessNodeFieldValue.class);
    if (CollectionUtils.isNotEmpty(fieldValueList)) {
      List<FieldAnswerVo> answerVoList = Lists.newArrayList();
      FieldAnswerVo lastAnswerVo = null;
      for (TProcessNodeFieldValue fieldValue : fieldValueList) {
        if (lastAnswerVo == null || !lastAnswerVo.getFieldId().equals(fieldValue.getFieldId())) {
          FieldAnswerVo fieldAnswerVo = new FieldAnswerVo();
          fieldAnswerVo.setFieldId(fieldValue.getFieldId());
          fieldAnswerVo.getOptionList().add(
              new FieldOptionVo(fieldValue.getId(), fieldValue.getFieldId(), fieldValue.getOptionKey(),
                  fieldValue.getFieldValue()));
          answerVoList.add(fieldAnswerVo);
          lastAnswerVo = fieldAnswerVo;
        } else {
          lastAnswerVo.getOptionList().add(
              new FieldOptionVo(fieldValue.getId(), fieldValue.getFieldId(), fieldValue.getOptionKey(),
                  fieldValue.getFieldValue()));
        }
      }
      nodeDefVo.getAnswers().addAll(answerVoList);
    }
  }

  /**
   * 添加节点.
   */
  public void saveNode(NodeVo nodeVo) {
    if (nodeVo.getId() == null) {
      // add
      dslContext.insertInto(T_PROCESS_NODE)
          .set(T_PROCESS_NODE.PROCESS_ID, nodeVo.getProcessId())
          .set(T_PROCESS_NODE.NODE_BPMN_ID, nodeVo.getNodeBpmnId())
          .execute();
    } else {
      // update
      dslContext.update(T_PROCESS_NODE)
          .set(T_PROCESS_NODE.PROCESS_ID, nodeVo.getProcessId())
          .set(T_PROCESS_NODE.NODE_BPMN_ID, nodeVo.getNodeBpmnId())
          .where(T_PROCESS_NODE.ID.eq(nodeVo.getId()))
          .execute();
    }
  }

  /**
   * (考虑是否逻辑删除).
   */
  public void deleteNode(String nodeBpmnId) {
    // 检查节点是否存在
    Record1<Long> record1 = dslContext.select(T_PROCESS_NODE.ID)
        .from(T_PROCESS_NODE)
        .where(T_PROCESS_NODE.NODE_BPMN_ID.eq(nodeBpmnId))
        .fetchAny();
    if (record1 == null) {
      LOGGER.info("node is not exist.");
      return;
    }

    dslContext.delete(T_PROCESS_NODE)
        .where(T_PROCESS_NODE.NODE_BPMN_ID.eq(nodeBpmnId))
        .execute();
  }

  /**
   * 保存节点参数.
   */
  public void saveParams(FieldSubmitVo fieldSubmitVo) {
    if (StringUtils.isBlank(fieldSubmitVo.getNodeBpmnId())) {
      throw new RestException("node bpmn id is null.");
    }

    // 检查节点是否存在
    Record1<Long> record1 = dslContext.select(T_PROCESS_NODE.ID)
        .from(T_PROCESS_NODE)
        .where(T_PROCESS_NODE.NODE_BPMN_ID.eq(fieldSubmitVo.getNodeBpmnId()))
        .fetchAny();
    if (record1 == null) {
      throw new RestException("node is not exist.");
    }

    // 批量删除旧的字段数据记录
    dslContext.delete(T_PROCESS_NODE_FIELD_VALUE)
        .where(T_PROCESS_NODE_FIELD_VALUE.NODE_ID.eq(record1.value1()))
        .execute();

    // 批量插入新字段数据记录
    if (CollectionUtils.isEmpty(fieldSubmitVo.getAnswerList())) {
      return;
    }
    List<TProcessNodeFieldValueRecord> recordList = Lists.newArrayList();
    for (FieldAnswerVo answerVo : fieldSubmitVo.getAnswerList()) {
      if (answerVo.getOptionList() != null) {
        for (FieldOptionVo optionVo : answerVo.getOptionList()) {
          TProcessNodeFieldValueRecord fieldValue = new TProcessNodeFieldValueRecord();
          fieldValue.setNodeId(fieldSubmitVo.getNodeId());
          fieldValue.setFieldId(answerVo.getFieldId());
          fieldValue.setFieldValue(optionVo.getOptionValue());
          fieldValue.setOptionKey(optionVo.getOptionKey());
          recordList.add(fieldValue);
        }
      }
    }
    if (!recordList.isEmpty()) {
      dslContext.batchInsert(recordList).execute();
    }
  }
}
