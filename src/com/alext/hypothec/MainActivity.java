package com.alext.hypothec;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import com.alext.hypothec.model.CalculationResult;
import com.alext.hypothec.model.MortgageCalculator;

import java.math.BigDecimal;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends Activity implements ActionBar.TabListener {

    private final MortgageCalculator calculator = new MortgageCalculator();
    private Fragment initialDataFragment;
    private Fragment resultsFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initialDataFragment = getFragmentManager().findFragmentById(R.id.initial_data_fragment);
        resultsFragment = getFragmentManager().findFragmentById(R.id.results_fragment);

        ActionBar bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        bar.addTab(bar.newTab().setText(R.string.tab_initial_data).setTabListener(this));
        bar.addTab(bar.newTab().setText(R.string.tab_results).setTabListener(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
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
    }

    void injectPayment(BigDecimal injectPayment, int month) {
        calculator.injectPayment(injectPayment,month);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        int position = tab.getPosition();
        fragmentTransaction.show(position==1?resultsFragment:initialDataFragment);
        fragmentTransaction.hide(position==1?initialDataFragment:resultsFragment);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}