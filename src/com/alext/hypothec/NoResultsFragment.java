package com.alext.hypothec;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.alext.hypothec.model.ErrorType;

import java.util.HashMap;
import java.util.Map;

public class NoResultsFragment extends Fragment {

    private View mainView;
    private static final int[] ITEM_IDS = new int[] {R.id.insufficient_parameters,R.id.zero_repay,R.id.invalid_percent};
    private static final Map<ErrorType,Integer> ERRORS_MAP;
    static {
        ERRORS_MAP = new HashMap<ErrorType, Integer>();
        ERRORS_MAP.put(ErrorType.ZERO_REPAY,R.id.zero_repay);
        ERRORS_MAP.put(ErrorType.INVALID_PERCENT,R.id.invalid_percent);
        ERRORS_MAP.put(ErrorType.INSUFFICIENT_PARAMETERS,R.id.insufficient_parameters);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.no_results,container,false);
        return mainView;
    }


    public void setErrorType(ErrorType errorType) {
        hideAll();
        mainView.findViewById(ERRORS_MAP.get(errorType)).setVisibility(View.VISIBLE);
    }

    private void hideAll() {
        for (int id : ITEM_IDS) {
            mainView.findViewById(id).setVisibility(View.GONE);
        }
    }
}
