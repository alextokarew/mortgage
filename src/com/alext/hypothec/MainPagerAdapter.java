package com.alext.hypothec;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: Admin
 * Date: 25.11.12
 * Time: 7:42
 * To change this template use File | Settings | File Templates.
 */
public class MainPagerAdapter extends FragmentStatePagerAdapter {

    private InitialDataFragment initialDataFragment;
    private ResultsFragment resultsFragment;

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
        this.initialDataFragment = new InitialDataFragment();
        this.resultsFragment = new ResultsFragment();
    }

    @Override
    public Fragment getItem(int i) {
        return i==1?resultsFragment:initialDataFragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
