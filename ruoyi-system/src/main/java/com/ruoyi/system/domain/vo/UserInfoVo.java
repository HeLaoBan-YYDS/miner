package com.ruoyi.system.domain.vo;

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

   private BigDecimal myTotalComputePower;

   private String dailyYieldPerT;

   private BigDecimal usdtAccount;

   private BigDecimal btcAccount;

   private String dailyPowerFee;

   private String platformTotalComputePower;

   private BigDecimal yesterdayIncome;

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
