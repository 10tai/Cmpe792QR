package com.cmpe492.attendancesystem;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.cmpe492.attendancesystem.firebase.FireBase;
import com.firebase.client.Firebase;

import java.util.Date;
import java.util.HashMap;

/**
 * Helpers class to put that code which will be reused at couple of places.
 */
public class Helpers {

    public static void showSnackBar(View view, String text) {
        hideKeyBoard(view);
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
    }

    // this method hide the keyboard as its name says.

    private static void hideKeyBoard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) AppGlobals.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
