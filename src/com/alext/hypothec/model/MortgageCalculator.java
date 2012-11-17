package com.alext.hypothec.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

public class MortgageCalculator {
    private final int creditSum;
    private final BigDecimal percent;
    private final int estimatedMonths;
    private final BigDecimal monthlyPayment;
    private BigDecimal lastPayment;
    private int actualMonths;
    private List<BigDecimal> repayDistribution;
    private List<BigDecimal> percentsDistribution;
    private List<BigDecimal> remaindersDistribution;
    private final Map<Integer,BigDecimal> injections = new HashMap<Integer, BigDecimal>();

    private static final MathContext MATH_CONTEXT = MathContext.DECIMAL128;
    private static final double LAST_PAYMENT_THRESHOLD = 1.20;

    public MortgageCalculator(int creditSum, BigDecimal percent, int estimatedMonths) {
        this.creditSum = creditSum;
        this.percent = percent;
        this.estimatedMonths = estimatedMonths;
        this.monthlyPayment = calculateMonthlyPayment();
    }

    public MortgageCalculator(int creditSum, BigDecimal percent, int estimatedMonths, BigDecimal monthlyPayment) {
        this.creditSum = creditSum;
        this.percent = percent;
        this.estimatedMonths = estimatedMonths;
        this.monthlyPayment = monthlyPayment;
    }

    public BigDecimal getMonthlyPayment() {
        return monthlyPayment;
    }

    public void injectPayment(BigDecimal injectedPayment, int month) {
        if (!injections.containsKey(month)) {
            injections.put(month,BigDecimal.ZERO);
        }
        injections.put(month, injections.get(month).add(injectedPayment));
    }

    public void removeInjectedPayment(int month) {
        injections.remove(month);
    }

    public int getActualMonths() {
        return actualMonths;
    }

    public List<BigDecimal> getRepayDistribution() {
        return Collections.unmodifiableList(repayDistribution);
    }

    public List<BigDecimal> getPercentsDistribution() {
        return Collections.unmodifiableList(percentsDistribution);
    }

    public List<BigDecimal> getRemaindersDistribution() {
        return Collections.unmodifiableList(remaindersDistribution);
    }

    private BigDecimal calculateMonthlyPayment() {
        BigDecimal p = percent.divide(BigDecimal.valueOf(1200), MATH_CONTEXT);
        return p.multiply(BigDecimal.valueOf(creditSum))
                .divide(BigDecimal.ONE.subtract(p.add(BigDecimal.ONE).pow(-estimatedMonths, MATH_CONTEXT)), MATH_CONTEXT).setScale(2, RoundingMode.HALF_UP);

    }

    private void calculateDistributions() {
        BigDecimal remainder = BigDecimal.valueOf(creditSum);
        final BigDecimal monthPercent = percent.divide(BigDecimal.valueOf(1200), MATH_CONTEXT);
        percentsDistribution = new ArrayList<BigDecimal>(estimatedMonths);
        repayDistribution = new ArrayList<BigDecimal>(estimatedMonths);
        remaindersDistribution = new ArrayList<BigDecimal>(estimatedMonths);
        int month =0;
        while (remainder.compareTo(monthlyPayment.multiply(BigDecimal.valueOf(LAST_PAYMENT_THRESHOLD)))>0) {
            month++;
            BigDecimal percentSum = calculatePercents(remainder, monthPercent);
            BigDecimal repay = monthlyPayment.subtract(percentSum);
            if (injections.containsKey(month)) {
                remainder = remainder.subtract(injections.get(month));
            }
            remainder = remainder.subtract(repay);
            percentsDistribution.add(percentSum);
            repayDistribution.add(repay);
            remaindersDistribution.add(remainder);
        }
        percentsDistribution.add(calculatePercents(remainder,monthPercent));
        repayDistribution.add(remainder);
        remaindersDistribution.add(BigDecimal.ZERO);
        lastPayment = remainder.add(percentsDistribution.get(month));
        actualMonths = month+1;
    }

    private BigDecimal calculatePercents(BigDecimal base, BigDecimal percentsRate) {
        return base.multiply(percentsRate).setScale(2,RoundingMode.HALF_UP);
    }

    public static void main(String[] args) {
        MortgageCalculator calc = new MortgageCalculator(3500000,new BigDecimal("14.0"),120);
        calc.calculateDistributions();
        System.out.println(calc.getMonthlyPayment());
        System.out.println(calc.percentsDistribution);
        System.out.println(calc.repayDistribution);
        System.out.println(calc.remaindersDistribution);
        System.out.println(calc.lastPayment);
        System.out.println(calc.actualMonths);
        calc.injectPayment(BigDecimal.valueOf(200000),12);
        calc.injectPayment(BigDecimal.valueOf(200000),24);
        calc.injectPayment(BigDecimal.valueOf(200000),36);
        calc.injectPayment(BigDecimal.valueOf(200000),48);
        calc.calculateDistributions();
        System.out.println(calc.getMonthlyPayment());
        System.out.println(calc.percentsDistribution);
        System.out.println(calc.repayDistribution);
        System.out.println(calc.remaindersDistribution);
        System.out.println(calc.lastPayment);
        System.out.println(calc.actualMonths);
    }

}

