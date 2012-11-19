package com.alext.hypothec;

import android.view.View;
import android.widget.EditText;

import java.math.BigDecimal;

public class Utils {
    private Utils() {
    }

    public static int editTextToInt(int id, View view) {
        return Integer.valueOf(((EditText) view.findViewById(id)).getText().toString());
    }

    public static BigDecimal editTextToBigDecimal(int id, View view) {
        return new BigDecimal(((EditText) view.findViewById(id)).getText().toString());
    }

    public static boolean isEmptyField(int id, View view) {
        return ((EditText) view.findViewById(id)).length()==0;
    }
}