package com.ruoyi.system.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 用户信息 VO
 *
 * @author Remi
 */
@Data
@Getter
@Setter
public class UserInfoVo {

   @ApiModelProperty("我的算力")
   private BigDecimal myTotalComputePower;

   @ApiModelProperty("每日单T产出")
   private String dailyYieldPerT;

   @ApiModelProperty("USDT账户")
   private BigDecimal usdtAccount;

   @ApiModelProperty("BTC账户")
   private BigDecimal btcAccount;

   @ApiModelProperty("电费")
   private String dailyPowerFee;

   @ApiModelProperty("平台总算力")
   private String platformTotalComputePower;

   @ApiModelProperty("昨日奖励")
   private BigDecimal yesterdayIncome;

   @ApiModelProperty("总收益")
   private BigDecimal totalIncome;

   public BigDecimal getUsdtAccount() {
      if (usdtAccount == null) {
         usdtAccount = BigDecimal.ZERO;
      }
      return usdtAccount;
   }

   public BigDecimal getBtcAccount() {
      if (btcAccount == null) {
         btcAccount = BigDecimal.ZERO;
      }
      return btcAccount;
   }
}
