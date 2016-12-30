package de.dotwee.rgb.canteen.view.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import butterknife.BindString;
import butterknife.ButterKnife;
import de.dotwee.rgb.canteen.R;

/**
 * Created by lukas on 27.12.16.
 */

public class CanteenClosedDialog extends AlertDialog implements AlertDialog.OnClickListener {
    private static final String TAG = CanteenClosedDialog.class.getSimpleName();

    @BindString(R.string.dialog_canteenclosed_message)
    String message;

    @BindString(R.string.dialog_canteenclosed_action_refresh)
    String actionRefresh;

    @BindString(R.string.dialog_canteenclosed_action_exit)
    String actionExit;

    public CanteenClosedDialog(Context context) {
        super(context);

        ButterKnife.bind(this);

        setHeader();
        setContent();
        setFooter();
    }

    private void setHeader() {
        setTitle(R.string.dialog_canteenclosed_title);
    }

    private void setContent() {
        setMessage(message);
    }

    private void setFooter() {
        setRefreshButton();
        setExitButton();
    }

    private void setRefreshButton() {
        setButton(AlertDialog.BUTTON_POSITIVE, actionRefresh, this);
    }

    private void setExitButton() {
        setButton(AlertDialog.BUTTON_NEGATIVE, actionExit, this);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {

            case AlertDialog.BUTTON_POSITIVE:
                // Refresh
                break;

            case AlertDialog.BUTTON_NEGATIVE:
                // Exit
                break;
        }
    }
}
