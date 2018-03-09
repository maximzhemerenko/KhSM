package com.khsm.app.presentation.ui.utils.maskedittext;

import android.widget.EditText;

public class EditTextMask {
    public static void setup(EditText editText, String mask) {
        MaskEditTextChangedListener maskEditTextChangedListener = new MaskEditTextChangedListener(mask, editText);
        editText.addTextChangedListener(maskEditTextChangedListener);
    }
}
