package com.alext.hypothec;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragments;

    public MainPagerAdapter(FragmentManager fm, InitialDataFragment initialDataFragment, ResultsWrapperFragment resultsWrapperFragment) {
        super(fm);
        fragments = Collections.unmodifiableList(Arrays.asList(initialDataFragment,resultsWrapperFragment));
    }

    @Override
    public Fragment getItem(int item) {
        return fragments.get(item);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
