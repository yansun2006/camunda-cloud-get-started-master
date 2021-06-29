package io.camunda.getstarted;

/**
 * @author yan.sun, {@literal yan.sun@leyantech.com}
 * @date 2021-06-18.
 */
public class TradeVo {

  private String tradeId;

  private TradeStatus status;

  private String tradeCondition;

  public String getTradeId() {
    return tradeId;
  }

  public void setTradeId(String tradeId) {
    this.tradeId = tradeId;
  }

  public TradeStatus getStatus() {
    return status;
  }

  public void setStatus(TradeStatus status) {
    this.status = status;
  }

  public String getTradeCondition() {
    return tradeCondition;
  }

  public void setTradeCondition(String tradeCondition) {
    this.tradeCondition = tradeCondition;
  }
}
