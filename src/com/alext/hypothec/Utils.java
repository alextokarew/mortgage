package com.alext.hypothec;

import android.view.View;
import android.widget.EditText;

import java.math.BigDecimal;

public class Utils {
    private Utils() {
    }

    public static int editTextToInt(EditText editText) {
        return Integer.valueOf(editText.getText().toString());
    }

    public static BigDecimal editTextToBigDecimal(EditText editText) {
        return new BigDecimal(editText.getText().toString());
    }

    public static boolean isEmptyField(EditText editText) {
        return editText.getText().toString().length()==0;
    }
}