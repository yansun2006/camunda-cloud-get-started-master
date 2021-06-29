package com.leyantech.zeebedemo.common.base;

import java.io.Serializable;
import java.util.List;

/**
 * @author ：menghui.cao, menghui.cao@leyantech.com
 * @date ：2019-07-17 17:42
 */
public class PageInfo<T> implements Serializable {

  /**
   *  当前页面
   */
  private Long currentPage;

  /**
   * 每页显示数量
   */
  private Long pageSize;

  /**
   *  总页数
   */
  private Long totalPage;

  /**
   * 总数量
   */
  private Long totalSize;

  /**
   * 详细数据
   */
  private List<T> content;

  public List<T> getContent() {
    return content;
  }

  public void setContent(List<T> content) {
    this.content = content;
  }

  public Long getTotalSize() {
    return totalSize;
  }

  public void setTotalSize(Long totalSize) {
    this.totalSize = totalSize;
  }

  public Long getCurrentPage() {
    return currentPage;
  }

  public void setCurrentPage(Long currentPage) {
    this.currentPage = currentPage;
  }

  public Long getTotalPage() {
    if (totalSize == null || pageSize == null || pageSize == 0) {
      return 0L;
    }
    return (totalSize % pageSize == 0) ? totalSize / pageSize : (totalSize / pageSize + 1);
  }

  public void setTotalPage(Long totalPage) {
    this.totalPage = totalPage;
  }

  public Long getPageSize() {
    return pageSize;
  }

  public void setPageSize(Long pageSize) {
    this.pageSize = pageSize;
  }

}
