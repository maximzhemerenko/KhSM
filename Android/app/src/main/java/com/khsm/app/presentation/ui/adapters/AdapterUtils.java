package com.khsm.app.presentation.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.khsm.app.R;
import com.khsm.app.data.entities.Result;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class AdapterUtils {
    private static final DecimalFormat decimalFormat;

    static {
        decimalFormat = new DecimalFormat("00.00");
        DecimalFormatSymbols decimalFormatSymbols = decimalFormat.getDecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
    }

    @Nullable
    public static Float findSingle(@NonNull Result result) {
        List<Float> attempts = result.attempts;

        Float best = null;

        for (int i = 0; i < attempts.size(); i++) {
            Float attempt = attempts.get(i);
            if (best == null || (attempt != null && best > attempt)) {
                best = attempt;
            }
        }

        return best;
    }

    @NonNull
    public static String formatResultTime(@NonNull Context context, @Nullable Float time) {
        if (time == null)
            return context.getString(R.string.DNF);

        String result = "";

        if (time >= 60) {
            int minutes = (int)(time / 60);
            result += minutes + ":";
            time -= minutes * 60;
        }

        result += decimalFormat.format(time);

        return result;
    }
}
