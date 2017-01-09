package de.dotwee.rgb.canteen.view.dialogs;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.dotwee.rgb.canteen.R;
import timber.log.Timber;

/**
 * Created by lukas on 30.12.2016.
 */
public class MessageDialog extends AppCompatDialog {
    private static final String TAG = MessageDialog.class.getSimpleName();

    @BindView(R.id.textViewTitle)
    TextView textViewTitle;

    @BindView(R.id.textViewMessage)
    TextView textViewMessage;

    @BindView(R.id.buttonExit)
    Button buttonExit;

    @BindView(R.id.buttonRefresh)
    Button buttonRefresh;

    private AppCompatActivity appCompatActivity;

    public MessageDialog(@NonNull AppCompatActivity appCompatActivity) {
        this(appCompatActivity, DialogMessage.ISSUE_UNKNOWN);
    }

    public MessageDialog(@NonNull AppCompatActivity appCompatActivity, @NonNull DialogMessage dialogMessage) {
        super(appCompatActivity, R.style.AppTheme_Dialog_Closed);
        this.appCompatActivity = appCompatActivity;

        setContentView(R.layout.dialog_closed);
        ButterKnife.bind(this, getWindow().getDecorView());
        setDialog(dialogMessage);
    }

    public void setDialog(@NonNull DialogMessage dialogMessage) {
        Timber.i("setDialog DialogMessage=%s", dialogMessage.name());

        textViewTitle.setText(dialogMessage.getTitleId());
        textViewMessage.setText(dialogMessage.getMessageId());
    }

    @OnClick(R.id.buttonRefresh)
    void onClickRefresh() {
        appCompatActivity.recreate();
    }

    @OnClick(R.id.buttonExit)
    void onClickExit() {
        appCompatActivity.finishAffinity();
    }

    public enum DialogMessage {
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
