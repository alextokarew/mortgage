package com.alext.hypothec;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.alext.hypothec.model.CalculationException;
import com.alext.hypothec.model.CalculationResult;
import com.alext.hypothec.model.ErrorType;

public class ResultsWrapperFragment extends Fragment {

    private CalculationResult result;
    private CalculationException exception;
    private View mainView;
    private NoResultsFragment noResultsFragment;
    private Fragment resultsFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.results_wrapper,container,false);
        noResultsFragment = (NoResultsFragment)getFragmentManager().findFragmentById(R.id.no_results_fragment);
        resultsFragment = getFragmentManager().findFragmentById(R.id.results_fragment);
        return mainView;
    }

    public void setResult(CalculationResult result) {
        this.result = result;
        this.exception = null;
        onResultSet();
    }

    private void onResultSet() {
        if (result==null) {
            swapFragments(resultsFragment,noResultsFragment);
            noResultsFragment.setErrorType(exception==null ? ErrorType.INSUFFICIENT_PARAMETERS : exception.getType());
        } else {
            swapFragments(noResultsFragment, resultsFragment);
            ((TextView)mainView.findViewById(R.id.monthly_payment)).setText(result.getMonthlyPayment().toString());
            ((TextView)mainView.findViewById(R.id.actual_months)).setText(result.getActualMonths().toString());
            ((TextView)mainView.findViewById(R.id.overall_amount)).setText(result.getOverallAmount().toString());
            ((TextView)mainView.findViewById(R.id.overall_percents)).setText(result.getOverallPercents().toString());
        }
    }

    private void swapFragments(Fragment toHide, Fragment toShow) {
        getFragmentManager()
                .beginTransaction()
                .hide(toHide)
                .show(toShow)
                .commit();
    }

    public void setException(CalculationException exception) {
        this.exception = exception;
        this.result = null;
        onResultSet();
    }
}
