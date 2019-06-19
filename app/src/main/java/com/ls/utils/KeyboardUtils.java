package com.ls.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;

import java.util.Objects;

public class KeyboardUtils {
    public static void showKeyboard(View theView) {
        Context context = theView.getContext();
        Object service = context.getSystemService(Context.INPUT_METHOD_SERVICE);

        InputMethodManager imm = (InputMethodManager) service;
        if (imm != null) {
            imm.toggleSoftInput(
                    InputMethodManager.SHOW_FORCED,
                    InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    public static void hideKeyboard(@Nullable View theView) {
        if (theView != null) {
            Context context = theView.getContext();
            Object service = context.getSystemService(Context.INPUT_METHOD_SERVICE);

            InputMethodManager imm = (InputMethodManager) service;
            if (imm != null) {
                imm.hideSoftInputFromWindow(theView.getWindowToken(), 0);
            }
        }
    }

    public static void showKeyboardFromDialog(View theView, Dialog dialog) {
        theView.requestFocus();
        Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}
