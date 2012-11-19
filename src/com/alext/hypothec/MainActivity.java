package com.alext.hypothec;

import android.app.Activity;
import android.os.Bundle;
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
        boolean disable = Utils.isEmptyField(R.id.credit_sum,getWindow().getDecorView()) ||
                Utils.isEmptyField(R.id.percentage, getWindow().getDecorView()) ||
                Utils.isEmptyField(R.id.credit_duration_month, getWindow().getDecorView());

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
        calculator.setCreditSum(Utils.editTextToInt(R.id.credit_sum,getWindow().getDecorView()));
        calculator.setPercent(Utils.editTextToBigDecimal(R.id.percentage,getWindow().getDecorView()));
        calculator.setEstimatedMonths(Utils.editTextToInt(R.id.credit_duration_month,getWindow().getDecorView()));
        if (Utils.isEmptyField(R.id.monthly_payment, getWindow().getDecorView())) {
            calculator.setMonthlyPayment(null);
        } else {
            calculator.setMonthlyPayment(Utils.editTextToBigDecimal(R.id.monthly_payment,getWindow().getDecorView()));
        }
        calculator.calculateDistributions();
    }



    MortgageCalculator getCalculator() {
        return calculator;
    }
}