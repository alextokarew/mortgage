package com.alext.hypothec;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.alext.hypothec.model.MortgageCalculator;

import java.math.BigDecimal;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void calculate(View button) {
        MortgageCalculator calculator = new MortgageCalculator(
                editTextToInt(R.id.credit_sum),
                editTextToDoubleBigDecimal(R.id.percentage),
                editTextToInt(R.id.credit_duration_month)
        );

        EditText monthlyPayment = (EditText) findViewById(R.id.monthly_payment);
        monthlyPayment.setText(calculator.getMonthlyPayment().toString());
    }

    private int editTextToInt(int id) {
        return Integer.valueOf(((EditText) findViewById(id)).getText().toString());
    }

    private BigDecimal editTextToDoubleBigDecimal(int id) {
        return new BigDecimal(((EditText) findViewById(id)).getText().toString());
    }
}