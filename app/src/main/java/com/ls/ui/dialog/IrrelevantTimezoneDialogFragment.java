package com.ls.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ls.drupalcon.R;
import com.ls.drupalcon.model.PreferencesManager;
import com.ls.utils.DateUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.TimeZone;

public class IrrelevantTimezoneDialogFragment extends DialogFragment {

    private final static String PREF_FILE_NAME = "timezone.dialog.preference";
    private final static String PREF_DONT_SHOW_AGAIN = "timezone.dialog.dontshow.key";

    public static final String TAG = IrrelevantTimezoneDialogFragment.class.getName();

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        TimeZone eventTimeZone = PreferencesManager.getInstance().getServerTimeZoneObject();
        String timezoneNotificationData = String.format(Objects.requireNonNull(getActivity()).getString(R.string.irrelevant_timezone_notificaiton), eventTimeZone.getDisplayName(), eventTimeZone.getID());

        ViewGroup contentView = (ViewGroup) LayoutInflater.from(getActivity()).inflate(R.layout.dialog_timezone_warning,null);
        TextView messageView = contentView.findViewById(R.id.messageView);
        messageView.setText(timezoneNotificationData);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(R.string.Attention);
        alertDialogBuilder.setView(contentView);
        alertDialogBuilder.setPositiveButton(getActivity().getString(android.R.string.ok), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                CheckBox dontShowBox = ((Dialog) dialog).findViewById(R.id.chk_dont_ask_again);

                if (dontShowBox.isChecked()) {
                    setCanPresentMessage(dontShowBox.getContext(), false);
                }
            }
        });

        Dialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(true);

        return alertDialog;
    }

    public static boolean isCurrentTimezoneRelevant()
    {
        TimeZone eventTimezone = DateUtils.getInstance().getTimeZone();
        TimeZone curentZone = TimeZone.getDefault();
        return curentZone.getID().equals(eventTimezone.getID());
    }

    public static void setCanPresentMessage(Context context,boolean canPresent)
    {
        SharedPreferences prefs = getPreferences(context);
         prefs.edit().putBoolean(PREF_DONT_SHOW_AGAIN, !canPresent).apply();
    }

    public static boolean canPresentMessage(Context context)
    {
        SharedPreferences prefs = getPreferences(context);
        return !prefs.getBoolean(PREF_DONT_SHOW_AGAIN,false);
    }

    private static SharedPreferences getPreferences(Context context)
    {
        return context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }
}

