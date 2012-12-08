package com.alext.hypothec.model;

import junit.framework.TestCase;

import java.math.BigDecimal;

public class MortgageCalculatorTest extends TestCase {

    private MortgageCalculator calc = new MortgageCalculator();

    public void testAdequateCase() throws CalculationException {
        calc.setCreditSum(3500000);
        calc.setPercent(BigDecimal.valueOf(14.0));
        calc.setEstimatedMonths(120);

        CalculationResult result = calc.calculateDistributions();

        assertEquals(BigDecimal.valueOf(54343.25), result.getMonthlyPayment());
        assertEquals(BigDecimal.valueOf(54343.74), result.getLastPayment());
        assertEquals(120, result.getActualMonths().intValue());
        assertEquals(BigDecimal.valueOf(6521190.49), result.getOverallAmount());
        assertEquals(BigDecimal.valueOf(3021190.49), result.getOverallPercents());
    }

    public void testInjections() throws CalculationException {
        calc.setCreditSum(3500000);
        calc.setPercent(BigDecimal.valueOf(14.0));
        calc.setEstimatedMonths(120);
        calc.injectPayment(BigDecimal.valueOf(200000),12);
        calc.injectPayment(BigDecimal.valueOf(200000),24);
        calc.injectPayment(BigDecimal.valueOf(200000),36);
        calc.injectPayment(BigDecimal.valueOf(200000),48);

        CalculationResult result = calc.calculateDistributions();
        
        assertEquals(BigDecimal.valueOf(54343.25),result.getMonthlyPayment());
        assertEquals(BigDecimal.valueOf(22009.58),result.getLastPayment());
        assertEquals(86,result.getActualMonths().intValue());
        assertEquals(BigDecimal.valueOf(5441185.83),result.getOverallAmount());
        assertEquals(BigDecimal.valueOf(1941185.83), result.getOverallPercents());
    }

    public void testSmallSumBigPercents() {
        calc.setCreditSum(120);
        calc.setPercent(BigDecimal.valueOf(80.0));
        calc.setEstimatedMonths(120);

        try {
            calc.calculateDistributions();
            fail("Must throw a CalculationException");
        } catch (CalculationException e) {
            assertSame(ErrorType.ZERO_REPAY, e.getType());
        }
    }

    public void testZeroPercents() {
        calc.setCreditSum(3500000);
        calc.setPercent(BigDecimal.valueOf(0.0));
        calc.setEstimatedMonths(120);

        try {
            CalculationResult result = calc.calculateDistributions();
            fail("Must throw a CalculationException");
        } catch (CalculationException e) {
            assertSame(ErrorType.INVALID_PERCENT, e.getType());
        }
    }

    public void testSmallPercents() throws CalculationException {
        calc.setCreditSum(3500000);
        calc.setPercent(BigDecimal.valueOf(0.1));
        calc.setEstimatedMonths(120);

        CalculationResult result = calc.calculateDistributions();

        assertEquals(BigDecimal.valueOf(29313.96), result.getMonthlyPayment());
        assertEquals(BigDecimal.valueOf(29313.79), result.getLastPayment());
        assertEquals(120, result.getActualMonths().intValue());
        assertEquals(BigDecimal.valueOf(3517675.03), result.getOverallAmount());
        assertEquals(BigDecimal.valueOf(17675.03), result.getOverallPercents());
    }


}
