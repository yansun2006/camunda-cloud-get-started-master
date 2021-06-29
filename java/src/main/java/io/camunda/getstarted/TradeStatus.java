package io.camunda.getstarted;

/**
 * @author yan.sun, {@literal yan.sun@leyantech.com}
 * @date 2021-06-18.
 */
public enum TradeStatus {
  DEFAULT_TRADE_STATUS(0),
  PAY_PENDING(1),
  SELLER_CONSIGNED_PART(2),
  TRADE_BUYER_SIGNED(3),
  TRADE_CLOSED(4),
  TRADE_CLOSED_BY_TAOBAO(5),
  TRADE_FINISHED(6),
  TRADE_NO_CREATE_PAY(7),
  WAIT_BUYER_CONFIRM_GOODS(8),
  WAIT_BUYER_PAY(9),
  WAIT_PRE_AUTH_CONFIRM(10),
  WAIT_SELLER_SEND_GOODS(11),
  ALL_WAIT_PAY(12),
  ALL_CLOSED(13),
  PAID_FORBID_CONSIGN(14),
  UNRECOGNIZED(-1);

  private final int value;

  private TradeStatus(int value) {
    this.value = value;
  }

  public final int getNumber() {
    if (this == UNRECOGNIZED) {
      throw new IllegalArgumentException("Can't get the number of an unknown enum value.");
    } else {
      return this.value;
    }
  }
}
