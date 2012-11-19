package com.alext.hypothec.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

public class MortgageCalculator extends Observable {
    private int creditSum;
    private BigDecimal percent;
    private int estimatedMonths;
    private BigDecimal monthlyPayment;
    private BigDecimal lastPayment;
    private int actualMonths;
    private List<BigDecimal> repayDistribution;
    private List<BigDecimal> percentsDistribution;
    private List<BigDecimal> remaindersDistribution;
    private final Map<Integer,BigDecimal> injections = new HashMap<Integer, BigDecimal>();

    private static final MathContext MATH_CONTEXT = MathContext.DECIMAL128;
    private static final double LAST_PAYMENT_THRESHOLD = 1.20;

    public void setCreditSum(int creditSum) {
        setChanged();
        this.creditSum = creditSum;
    }

    public void setEstimatedMonths(int estimatedMonths) {
        setChanged();
        this.estimatedMonths = estimatedMonths;
    }

    public void setPercent(BigDecimal percent) {
        setChanged();
        this.percent = percent;
    }

    public void setMonthlyPayment(BigDecimal monthlyPayment) {
        setChanged();
        this.monthlyPayment = monthlyPayment;
    }

    public BigDecimal getMonthlyPayment() {
        if (monthlyPayment==null) {
            monthlyPayment = calculateMonthlyPayment();
        }
        return monthlyPayment;
    }

    public void injectPayment(BigDecimal injectedPayment, int month) {
        setChanged();
        if (!injections.containsKey(month)) {
            injections.put(month,BigDecimal.ZERO);
        }
        injections.put(month, injections.get(month).add(injectedPayment));
    }

    public void removeInjectedPayment(int month) {
        setChanged();
        injections.remove(month);
    }

    public Integer getActualMonths() {
        calculateDistributions();
        return actualMonths;
    }

    public List<BigDecimal> getRepayDistribution() {
        calculateDistributions();
        return Collections.unmodifiableList(repayDistribution);
    }

    public List<BigDecimal> getPercentsDistribution() {
        calculateDistributions();
        return Collections.unmodifiableList(percentsDistribution);
    }

    public List<BigDecimal> getRemaindersDistribution() {
        calculateDistributions();
        return Collections.unmodifiableList(remaindersDistribution);
    }

    public BigDecimal getLastPayment() {
        calculateDistributions();
        return lastPayment;
    }

    private BigDecimal calculateMonthlyPayment() {
        BigDecimal p = percent.divide(BigDecimal.valueOf(1200), MATH_CONTEXT);
        return p.multiply(BigDecimal.valueOf(creditSum))
                .divide(BigDecimal.ONE.subtract(p.add(BigDecimal.ONE).pow(-estimatedMonths, MATH_CONTEXT)), MATH_CONTEXT).setScale(2, RoundingMode.HALF_UP);

    }

    public void calculateDistributions() {
        if (!hasChanged()) {
            return;
        }

        BigDecimal remainder = BigDecimal.valueOf(creditSum);
        final BigDecimal monthPercent = percent.divide(BigDecimal.valueOf(1200), MATH_CONTEXT);
        percentsDistribution = new ArrayList<BigDecimal>(estimatedMonths);
        repayDistribution = new ArrayList<BigDecimal>(estimatedMonths);
        remaindersDistribution = new ArrayList<BigDecimal>(estimatedMonths);
        int month =0;
        while (remainder.compareTo(getMonthlyPayment().multiply(BigDecimal.valueOf(LAST_PAYMENT_THRESHOLD)))>0) {
            month++;
            BigDecimal percentSum = calculatePercents(remainder, monthPercent);
            BigDecimal repay = getMonthlyPayment().subtract(percentSum);
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
        notifyObservers();
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
        calc.calculateDistributions();
        System.out.println(calc.getMonthlyPayment());
        System.out.println(calc.getPercentsDistribution());
        System.out.println(calc.getRepayDistribution());
        System.out.println(calc.getRemaindersDistribution());
        System.out.println(calc.getLastPayment());
        System.out.println(calc.getActualMonths());
        calc.injectPayment(BigDecimal.valueOf(200000),12);
        calc.injectPayment(BigDecimal.valueOf(200000),24);
        calc.injectPayment(BigDecimal.valueOf(200000),36);
        calc.injectPayment(BigDecimal.valueOf(200000),48);
        calc.calculateDistributions();
        System.out.println(calc.getMonthlyPayment());
        System.out.println(calc.getPercentsDistribution());
        System.out.println(calc.getRepayDistribution());
        System.out.println(calc.getRemaindersDistribution());
        System.out.println(calc.getLastPayment());
        System.out.println(calc.getActualMonths());
    }

}

