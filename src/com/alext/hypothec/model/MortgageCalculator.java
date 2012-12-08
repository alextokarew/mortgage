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

    public CalculationResult calculateDistributions() throws CalculationException {
        if (lastResult!=null) {
            return lastResult;
        }

        final BigDecimal monthPercent = percent.divide(BigDecimal.valueOf(1200), MATH_CONTEXT);
        if (monthPercent.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CalculationException(CalculationException.ErrorType.INVALID_PERCENT);
        }

        if (monthlyPayment==null) {
            monthlyPayment = calculateMonthlyPayment();
        }

        BigDecimal remainder = BigDecimal.valueOf(creditSum);
        List<BigDecimal> percentsDistribution = new ArrayList<BigDecimal>(estimatedMonths);
        List<BigDecimal> repayDistribution = new ArrayList<BigDecimal>(estimatedMonths);
        List<BigDecimal> remaindersDistribution = new ArrayList<BigDecimal>(estimatedMonths);
        int month = 0;
        BigDecimal overallAmount = BigDecimal.ZERO;
        while (remainder.compareTo(monthlyPayment.multiply(BigDecimal.valueOf(LAST_PAYMENT_THRESHOLD)))>0) {
            month++;
            BigDecimal percentSum = calculatePercents(remainder, monthPercent);
            BigDecimal repay = monthlyPayment.subtract(percentSum);
            if (repay.compareTo(BigDecimal.ZERO)==0) {
                throw new CalculationException(CalculationException.ErrorType.ZERO_REPAY);
            }
            overallAmount = overallAmount.add(monthlyPayment);
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
        overallAmount= overallAmount.add(lastPayment);
        lastResult = new CalculationResult(monthlyPayment,
                overallAmount,
                overallAmount.subtract(BigDecimal.valueOf(creditSum)),
                lastPayment,
                actualMonths,
                repayDistribution,
                percentsDistribution,
                remaindersDistribution);
        return lastResult;
    }

    private BigDecimal calculatePercents(BigDecimal base, BigDecimal percentsRate) {
        return base.multiply(percentsRate).setScale(2,RoundingMode.HALF_UP);
    }
}

