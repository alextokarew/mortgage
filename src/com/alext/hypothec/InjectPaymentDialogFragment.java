package com.alext.hypothec;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import com.alext.hypothec.model.MortgageCalculator;

import java.math.BigDecimal;

public class InjectPaymentDialogFragment extends DialogFragment {

    private MortgageCalculator calculator;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View rootView = inflater.inflate(R.layout.inject_payment, null);

        builder
                .setTitle(R.string.inject_payment)
                .setView(rootView)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (Utils.isEmptyField((EditText)rootView.findViewById(R.id.inject_amount)) ||
                                Utils.isEmptyField((EditText)rootView.findViewById(R.id.inject_month))) {
                            return;
                        }
                        calculator.injectPayment(Utils.editTextToBigDecimal((EditText)rootView.findViewById(R.id.inject_amount)),
                                Utils.editTextToInt((EditText)rootView.findViewById(R.id.inject_month)));
                        calculator.calculateDistributions();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        calculator = ((MainActivity)activity).getCalculator();
    }
}
