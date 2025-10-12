package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class IncomeInfo {

    private final BigDecimal userIncome;

    private final BigDecimal commission;

    private final BigDecimal dailyIncome;

    private final BigDecimal dividendRatio;

    public IncomeInfo(BigDecimal userIncome, BigDecimal commission,BigDecimal dailyIncome) {
        this.userIncome = userIncome;
        this.commission = commission;
        this.dailyIncome = dailyIncome;
        this.dividendRatio = userIncome.divide(dailyIncome, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
    }

    public BigDecimal getUserIncome() {
        return userIncome;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public BigDecimal getDailyIncome() {
        return dailyIncome;
    }

    public BigDecimal getDividendRatio() {
        return dividendRatio;
    }
}
