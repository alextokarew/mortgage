package com.alext.hypothec;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.alext.hypothec.model.CalculationException;
import com.alext.hypothec.model.CalculationResult;
import com.alext.hypothec.model.MortgageCalculator;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Admin
 * Date: 24.11.12
 * Time: 14:10
 * To change this template use File | Settings | File Templates.
 */
public class InitialDataFragment extends Fragment {

    private final MortgageCalculator calculator = new MortgageCalculator();
    private View mainView;
    private ResultsWrapperFragment resultsWrapperFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.initial_data,container,false);
        return mainView;
    }

    public CalculationResult calculate() {
        if (Utils.isEmptyField((EditText)mainView.findViewById(R.id.credit_sum)) ||
            Utils.isEmptyField((EditText)mainView.findViewById(R.id.percentage)) ||
            Utils.isEmptyField((EditText)mainView.findViewById(R.id.credit_duration_month))) {
            return null;
        }

        int creditSum = Utils.editTextToInt((EditText) mainView.findViewById(R.id.credit_sum));
        BigDecimal percent = Utils.editTextToBigDecimal((EditText) mainView.findViewById(R.id.percentage));
        int months = Utils.editTextToInt((EditText) mainView.findViewById(R.id.credit_duration_month));

        calculator.setCreditSum(creditSum);
        calculator.setPercent(percent);
        calculator.setEstimatedMonths(months);
        if (Utils.isEmptyField((EditText)mainView.findViewById(R.id.monthly_payment))) {
            calculator.setMonthlyPayment(null);
        } else {
            calculator.setMonthlyPayment(Utils.editTextToBigDecimal((EditText)mainView.findViewById(R.id.monthly_payment)));
        }
        try {
            return calculator.calculateDistributions();
        } catch (CalculationException e) {
            return null;
        }
    }

    void injectPayment(BigDecimal injectPayment, int month) {
        calculator.injectPayment(injectPayment,month);
    }
}
