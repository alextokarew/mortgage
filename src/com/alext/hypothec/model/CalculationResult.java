package com.alext.hypothec.model;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Admin
 * Date: 23.11.12
 * Time: 22:38
 * To change this template use File | Settings | File Templates.
 */
public class CalculationResult {
    private final BigDecimal monthlyPayment;
    private final BigDecimal overallAmount;
    private final BigDecimal lastPayment;
    private final int actualMonths;
    private final List<BigDecimal> repayDistribution;
    private final List<BigDecimal> percentsDistribution;
    private final List<BigDecimal> remaindersDistribution;

    public CalculationResult(BigDecimal monthlyPayment,
                             BigDecimal overallAmount,
                             BigDecimal lastPayment,
                             int actualMonths,
                             List<BigDecimal> repayDistribution,
                             List<BigDecimal> percentsDistribution,
                             List<BigDecimal> remaindersDistribution) {
        this.monthlyPayment = monthlyPayment;
        this.overallAmount = overallAmount;
        this.lastPayment = lastPayment;
        this.actualMonths = actualMonths;
        this.repayDistribution = Collections.unmodifiableList(repayDistribution);
        this.percentsDistribution = Collections.unmodifiableList(percentsDistribution);
        this.remaindersDistribution = Collections.unmodifiableList(remaindersDistribution);
    }

    public Integer getActualMonths() {
        return actualMonths;
    }

    public BigDecimal getLastPayment() {
        return lastPayment;
    }

    public BigDecimal getMonthlyPayment() {
        return monthlyPayment;
    }

    public BigDecimal getOverallAmount() {
        return overallAmount;
    }

    public List<BigDecimal> getPercentsDistribution() {
        return percentsDistribution;
    }

    public List<BigDecimal> getRemaindersDistribution() {
        return remaindersDistribution;
    }

    public List<BigDecimal> getRepayDistribution() {
        return repayDistribution;
    }
}
