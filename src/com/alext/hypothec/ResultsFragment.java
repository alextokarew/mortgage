package com.alext.hypothec;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created with IntelliJ IDEA.
 * User: Admin
 * Date: 25.11.12
 * Time: 11:22
 * To change this template use File | Settings | File Templates.
 */
public class ResultsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.results,container,false);
    }
}
