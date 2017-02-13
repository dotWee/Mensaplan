package de.dotwee.rgb.canteen.view.dialogs;

import android.support.annotation.NonNull;
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
public class MessageDialog extends AppCompatDialog implements MessageView {
    private static final String TAG = MessageDialog.class.getSimpleName();
    private final AppCompatActivity appCompatActivity;
    @BindView(R.id.textViewTitle)
    TextView textViewTitle;
    @BindView(R.id.textViewMessage)
    TextView textViewMessage;
    @BindView(R.id.buttonExit)
    Button buttonExit;
    @BindView(R.id.buttonRefresh)
    Button buttonRefresh;

    public MessageDialog(@NonNull AppCompatActivity appCompatActivity) {
        this(appCompatActivity, DialogMessage.ISSUE_UNKNOWN);
    }

    private MessageDialog(@NonNull AppCompatActivity appCompatActivity, @NonNull DialogMessage dialogMessage) {
        super(appCompatActivity, R.style.AppTheme_Dialog_Closed);
        this.appCompatActivity = appCompatActivity;

        setContentView(R.layout.dialog_closed);
        ButterKnife.bind(this, getWindow().getDecorView());
        setDialogMessage(dialogMessage);
    }

    @Override
    public void setDialogMessage(@NonNull DialogMessage dialogMessage) {
        Timber.i("setDialogMessage=%s", dialogMessage.name());

        textViewTitle.setText(dialogMessage.getTitleId());
        textViewMessage.setText(dialogMessage.getMessageId());
    }

    @OnClick(R.id.buttonRefresh)
    @Override
    public void recreateDialog() {
        appCompatActivity.recreate();
    }

    @OnClick(R.id.buttonExit)
    @Override
    public void recreateAffinity() {
        appCompatActivity.finishAffinity();
    }
}
