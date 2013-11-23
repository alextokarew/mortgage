package com.alext.hypothec;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.alext.hypothec.model.CalculationException;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    private static final int INITIAL_DATA_ITEM = 0;
    private static final int RESULTS_ITEM = 1;

    private ViewPager viewPager;
    private InitialDataFragment initialDataFragment;
    private ResultsWrapperFragment resultsWrapperFragment;
    private MainPagerAdapter mainPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initialDataFragment = new InitialDataFragment();
        resultsWrapperFragment = new ResultsWrapperFragment();

        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(),initialDataFragment,resultsWrapperFragment);
        viewPager = (ViewPager)findViewById(R.id.pager);
        viewPager.setAdapter(mainPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                getActionBar().setSelectedNavigationItem(position);
                if (position==RESULTS_ITEM) {
                    try {
                        resultsWrapperFragment.setResult(initialDataFragment.calculate());
                    }catch (CalculationException e) {
                        resultsWrapperFragment.setException(e);
                    }
                }
            }
        });

        ActionBar bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        bar.addTab(bar.newTab().setText(R.string.tab_initial_data).setTabListener(this));
        bar.addTab(bar.newTab().setText(R.string.tab_results).setTabListener(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.inject_item:
                new InjectPaymentDialogFragment().show(getFragmentManager(),"inject_payment_dialog");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {}

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {}

    public void injectPaymentClick(View view) {
        EditText injectPaymentAmount = (EditText) findViewById(R.id.inject_payment_amount);
        EditText injectPaymentMonth = (EditText) findViewById(R.id.inject_payment_month);
        if (Utils.isEmptyField(injectPaymentAmount) || Utils.isEmptyField(injectPaymentMonth)) {
            return;
        }
        initialDataFragment.injectPayment(Utils.editTextToBigDecimal(injectPaymentAmount), Utils.editTextToInt(injectPaymentMonth));

        TextView textView = new TextView(this);
        textView.setText("olololo");
        ListView injectedPayments = (ListView)findViewById(R.id.injected_payments);
        injectedPayments.addFooterView(textView);

        injectPaymentAmount.setText("");
        injectPaymentMonth.setText("");
    }
}