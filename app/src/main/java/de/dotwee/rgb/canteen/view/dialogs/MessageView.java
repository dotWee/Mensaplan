package de.dotwee.rgb.canteen.view.dialogs;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import de.dotwee.rgb.canteen.R;

/**
 * Created by lukas on 13.02.17.
 */
public interface MessageView {
    static final String TAG = MessageView.class.getSimpleName();

    void setDialogMessage(@NonNull MessageDialog.DialogMessage dialogMessage);

    void recreateDialog();

    void recreateAffinity();

    enum DialogMessage {
        ISSUE_CLOSED(R.string.dialog_issue_closed_title, R.string.dialog_issue_closed_message),
        ISSUE_CONNECTION(R.string.dialog_issue_connection_title, R.string.dialog_issue_connection_message),
        ISSUE_UNKNOWN(R.string.dialog_issue_unkown_title, R.string.dialog_issue_unkown_message);

        @StringRes
        final int titleId;

        @StringRes
        final int messageId;

        DialogMessage(@StringRes int titleId, @StringRes int messageId) {
            this.titleId = titleId;
            this.messageId = messageId;
        }

        @StringRes
        public int getTitleId() {
            return titleId;
        }

        @StringRes
        public int getMessageId() {
            return messageId;
        }
    }
}
