package com.alext.hypothec;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.initial_data,container,false);
        return mainView;
    }

    private void calculate() {
        calculator.setCreditSum(Utils.editTextToInt((EditText)mainView.findViewById(R.id.credit_sum)));
        calculator.setPercent(Utils.editTextToBigDecimal((EditText)mainView.findViewById(R.id.percentage)));
        calculator.setEstimatedMonths(Utils.editTextToInt((EditText)mainView.findViewById(R.id.credit_duration_month)));
        if (Utils.isEmptyField((EditText)mainView.findViewById(R.id.monthly_payment))) {
            calculator.setMonthlyPayment(null);
        } else {
            calculator.setMonthlyPayment(Utils.editTextToBigDecimal((EditText)mainView.findViewById(R.id.monthly_payment)));
        }
    }

    void injectPayment(BigDecimal injectPayment, int month) {
        calculator.injectPayment(injectPayment,month);
    }
}
