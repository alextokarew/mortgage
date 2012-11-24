package com.alext.hypothec;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created with IntelliJ IDEA.
 * User: Admin
 * Date: 24.11.12
 * Time: 14:10
 * To change this template use File | Settings | File Templates.
 */
public class ResultsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.results,container);
    }
}
