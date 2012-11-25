package com.alext.hypothec.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MortgageCalculator {
    private int creditSum;
    private BigDecimal percent;
    private int estimatedMonths;
    private final Map<Integer,BigDecimal> injections = new HashMap<Integer, BigDecimal>();
    private BigDecimal monthlyPayment;

    private CalculationResult lastResult;


    private static final MathContext MATH_CONTEXT = MathContext.DECIMAL128;
    private static final double LAST_PAYMENT_THRESHOLD = 1.20;

    public void setCreditSum(int creditSum) {
        this.creditSum = creditSum;
    }

    public void setEstimatedMonths(int estimatedMonths) {
        lastResult = null;
        this.estimatedMonths = estimatedMonths;
    }

    public void setPercent(BigDecimal percent) {
        lastResult = null;
        this.percent = percent;
    }

    public void setMonthlyPayment(BigDecimal monthlyPayment) {
        lastResult = null;
        this.monthlyPayment = monthlyPayment;
    }

    private BigDecimal getMonthlyPayment() {
        if (monthlyPayment==null) {
            monthlyPayment = calculateMonthlyPayment();
        }
        return monthlyPayment;
    }

    public void injectPayment(BigDecimal injectedPayment, int month) {
        lastResult = null;
        if (!injections.containsKey(month)) {
            injections.put(month,BigDecimal.ZERO);
        }
        injections.put(month, injections.get(month).add(injectedPayment));
    }

    public void removeInjectedPayment(int month) {
        lastResult = null;
        injections.remove(month);
    }

    private BigDecimal calculateMonthlyPayment() {
        BigDecimal p = percent.divide(BigDecimal.valueOf(1200), MATH_CONTEXT);
        return p.multiply(BigDecimal.valueOf(creditSum))
                .divide(BigDecimal.ONE.subtract(p.add(BigDecimal.ONE).pow(-estimatedMonths, MATH_CONTEXT)), MATH_CONTEXT).setScale(2, RoundingMode.HALF_UP);

    }

    public CalculationResult calculateDistributions() {
        if (lastResult!=null) {
            return lastResult;
        }

        BigDecimal remainder = BigDecimal.valueOf(creditSum);
        final BigDecimal monthPercent = percent.divide(BigDecimal.valueOf(1200), MATH_CONTEXT);
        List<BigDecimal> percentsDistribution = new ArrayList<BigDecimal>(estimatedMonths);
        List<BigDecimal> repayDistribution = new ArrayList<BigDecimal>(estimatedMonths);
        List<BigDecimal> remaindersDistribution = new ArrayList<BigDecimal>(estimatedMonths);
        int month =0;
        BigDecimal overallAmount = BigDecimal.ZERO;
        while (remainder.compareTo(getMonthlyPayment().multiply(BigDecimal.valueOf(LAST_PAYMENT_THRESHOLD)))>0) {
            month++;
            BigDecimal percentSum = calculatePercents(remainder, monthPercent);
            BigDecimal repay = getMonthlyPayment().subtract(percentSum);
            overallAmount = overallAmount.add(getMonthlyPayment());
            if (injections.containsKey(month)) {
                remainder = remainder.subtract(injections.get(month));
                overallAmount = overallAmount.add(injections.get(month));
            }
            remainder = remainder.subtract(repay);
            percentsDistribution.add(percentSum);
            repayDistribution.add(repay);
            remaindersDistribution.add(remainder);
        }
        percentsDistribution.add(calculatePercents(remainder,monthPercent));
        repayDistribution.add(remainder);
        remaindersDistribution.add(BigDecimal.ZERO);
        BigDecimal lastPayment = remainder.add(percentsDistribution.get(month));
        int actualMonths = month+1;
        lastResult = new CalculationResult(monthlyPayment,overallAmount,lastPayment,actualMonths,repayDistribution,percentsDistribution,remaindersDistribution);
        return lastResult;
    }

    private BigDecimal calculatePercents(BigDecimal base, BigDecimal percentsRate) {
        return base.multiply(percentsRate).setScale(2,RoundingMode.HALF_UP);
    }

    public static void main(String[] args) {
        //TODO: to unit test
        MortgageCalculator calc = new MortgageCalculator();
        calc.setCreditSum(3500000);
        calc.setPercent(new BigDecimal("14.0"));
        calc.setEstimatedMonths(120);
        CalculationResult result = calc.calculateDistributions();
        System.out.println(result.getMonthlyPayment());
        System.out.println(result.getPercentsDistribution());
        System.out.println(result.getRepayDistribution());
        System.out.println(result.getRemaindersDistribution());
        System.out.println(result.getLastPayment());
        System.out.println(result.getActualMonths());
        System.out.println(result.getOverallAmount());
        calc.injectPayment(BigDecimal.valueOf(200000),12);
        calc.injectPayment(BigDecimal.valueOf(200000),24);
        calc.injectPayment(BigDecimal.valueOf(200000),36);
        calc.injectPayment(BigDecimal.valueOf(200000),48);
        result = calc.calculateDistributions();
        System.out.println(result.getMonthlyPayment());
        System.out.println(result.getPercentsDistribution());
        System.out.println(result.getRepayDistribution());
        System.out.println(result.getRemaindersDistribution());
        System.out.println(result.getLastPayment());
        System.out.println(result.getActualMonths());
        System.out.println(result.getOverallAmount());
    }

}

