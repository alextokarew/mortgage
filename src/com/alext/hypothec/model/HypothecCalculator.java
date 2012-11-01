package com.alext.hypothec.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class HypothecCalculator {

    private final int creditSum;
    private final BigDecimal percent;
    private final int months;
    private final BigDecimal monthlyPayment;
    private BigDecimal lastPayment;
    private List<BigDecimal> repayDistribution;
    private List<BigDecimal> percentsDistribution;

    private static final MathContext MATH_CONTEXT = MathContext.DECIMAL128;

    public HypothecCalculator(int creditSum, BigDecimal percent, int months) {
        this.creditSum = creditSum;
        this.percent = percent;
        this.months = months;
        this.monthlyPayment = calculateMonthlyPayment();
    }

    public HypothecCalculator(int creditSum, BigDecimal percent, int months, BigDecimal monthlyPayment) {
        this.creditSum = creditSum;
        this.percent = percent;
        this.months = months;
        this.monthlyPayment = monthlyPayment;
    }

    public BigDecimal getMonthlyPayment() {
        return monthlyPayment;
    }

    private BigDecimal calculateMonthlyPayment() {
        BigDecimal p = percent.divide(BigDecimal.valueOf(1200), MATH_CONTEXT);
        return p.multiply(BigDecimal.valueOf(creditSum))
                .divide(BigDecimal.ONE.subtract(p.add(BigDecimal.ONE).pow(-months, MATH_CONTEXT)), MATH_CONTEXT).setScale(2, RoundingMode.HALF_UP);

    }

    private void calculateDistributions() {
        BigDecimal remainder = BigDecimal.valueOf(creditSum);
        final BigDecimal monthPercent = percent.divide(BigDecimal.valueOf(1200), MATH_CONTEXT);
        percentsDistribution = new ArrayList<BigDecimal>(months);
        repayDistribution = new ArrayList<BigDecimal>(months);
        for (int month=0;month<(months-1);month++) {
            BigDecimal percentSum = calculatePercents(remainder, monthPercent);
            BigDecimal repay = monthlyPayment.subtract(percentSum);
            percentsDistribution.add(percentSum);
            repayDistribution.add(repay);
            remainder = remainder.subtract(repay);
        }
        percentsDistribution.add(calculatePercents(remainder,monthPercent));
        repayDistribution.add(remainder);
        lastPayment = remainder.add(percentsDistribution.get(months-1));
    }

    private BigDecimal calculatePercents(BigDecimal base, BigDecimal percentsRate) {
        return base.multiply(percentsRate).setScale(2,RoundingMode.HALF_UP);
    }

    public static void main(String[] args) {
        HypothecCalculator calc = new HypothecCalculator(3500000,new BigDecimal("14.0"),120,new BigDecimal("54350.00"));
        calc.calculateDistributions();
        System.out.println(calc.getMonthlyPayment());
        System.out.println(calc.percentsDistribution);
        System.out.println(calc.repayDistribution);
        System.out.println(calc.lastPayment);
    }

}

