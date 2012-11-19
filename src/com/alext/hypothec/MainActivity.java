package com.alext.hypothec;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import com.alext.hypothec.model.MortgageCalculator;

import java.math.BigDecimal;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends Activity {

    private final MortgageCalculator calculator = new MortgageCalculator();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        calculator.addObserver(new Observer() {
            @Override
            public void update(Observable observable, Object o) {
                MortgageCalculator calculator = (MortgageCalculator) observable;

                EditText monthlyPayment = (EditText) findViewById(R.id.monthly_payment);
                monthlyPayment.setText(calculator.getMonthlyPayment().toString());
                EditText actualDuration = (EditText) findViewById(R.id.actual_credit_duration);
                actualDuration.setText(calculator.getActualMonths().toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean disable = Utils.isEmptyField((EditText)findViewById(R.id.credit_sum)) ||
                Utils.isEmptyField((EditText)findViewById(R.id.percentage)) ||
                Utils.isEmptyField((EditText)findViewById(R.id.credit_duration_month));

        menu.findItem(R.id.calculate_item).setEnabled(!disable);
        menu.findItem(R.id.inject_item).setEnabled(!disable);
        menu.findItem(R.id.payments_distribution_item).setEnabled(!disable);
        menu.findItem(R.id.reminder_distribution_item).setEnabled(!disable);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.inject_item:
                new InjectPaymentDialogFragment().show(getFragmentManager(),"inject_payment_dialog");
                return true;
            case R.id.calculate_item:
                calculate();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void calculate() {
        calculator.setCreditSum(Utils.editTextToInt((EditText)findViewById(R.id.credit_sum)));
        calculator.setPercent(Utils.editTextToBigDecimal((EditText)findViewById(R.id.percentage)));
        calculator.setEstimatedMonths(Utils.editTextToInt((EditText)findViewById(R.id.credit_duration_month)));
        if (Utils.isEmptyField((EditText)findViewById(R.id.monthly_payment))) {
            calculator.setMonthlyPayment(null);
        } else {
            calculator.setMonthlyPayment(Utils.editTextToBigDecimal((EditText)findViewById(R.id.monthly_payment)));
        }
        calculator.calculateDistributions();
    }



    MortgageCalculator getCalculator() {
        return calculator;
    }
}