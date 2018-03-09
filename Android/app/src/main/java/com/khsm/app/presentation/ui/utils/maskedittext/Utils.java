package com.khsm.app.presentation.ui.utils.maskedittext;

import java.util.Set;

abstract class Utils {
    static String unmask(String s, Set<String> replaceSymbols) {
        for (String symbol : replaceSymbols)
            s = s.replaceAll("[" + symbol + "]", "");

        return s;
    }

    static String mask(String format, String text) {
        String maskedText = "";
        int i = 0;
        for (char m : format.toCharArray()) {
            if (m != '#') {
                maskedText += m;
                continue;
            }
            try {
                maskedText += text.charAt(i);
            } catch (Exception e) {
                break;
            }
            i++;
        }
        return maskedText;
    }
}