package com.zis.musapp.gh.features.login;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding.view.RxView;
import com.zis.musapp.base.utils.RxUtil;
import com.zis.musapp.gh.BootstrapActivity;
import com.zis.musapp.gh.R;
import com.zis.musapp.gh.features.choosesong.ChooseSongActivtity;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mikhailz on 24/09/2016.
 */
public class SignupActivity extends BootstrapActivity {

  @BindView(R.id.digitsLogin)
  ImageButton mDigits;

  @BindView(R.id.phoneDigits)
  EditText mPhoneDigits;

  RxLoginManager mRxLoginManager = new RxLoginManager();

  //https://developers.facebook.com/docs/facebook-login/permissions
  List<String> facebookPermissions = new ArrayList<String>() {{
    add("public_profile");
    add("user_friends");
    add("email");
    add("user_about_me");
  }};

  private final PhoneNumberFormattingTextWatcher mPhoneWatcher =
      new PhoneNumberFormattingTextWatcher();
  private boolean mIsLoginStarted;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.singup_activity);
    ButterKnife.bind(this);

    mPhoneDigits.addTextChangedListener(mPhoneWatcher);
    mPhoneDigits.setRawInputType(Configuration.KEYBOARD_QWERTY);
    mPhoneDigits.setOnEditorActionListener((v, actionId, event) -> {
      if (actionId == EditorInfo.IME_ACTION_DONE) {
        mIsLoginStarted = true;
        doLogin();
        return true;
      }
      return false;
    });

    mRxLoginManager.loginDigitsObservable(SignupActivity.this)
        .map(digitLoginResult -> digitLoginResult.getSession().isValidUser())
        .subscribe(success -> {
          if (success) {
            startActivity(new Intent(SignupActivity.this, ChooseSongActivtity.class));
          }
        }, RxUtil.ON_ERROR_LOGGER);

    RxView.clicks(mDigits)
        .subscribe(aVoid -> {
          doLogin();
        });
  }

  private void doLogin() {
    mRxLoginManager.startDigitsLogining(mPhoneDigits.getText().toString());
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    mRxLoginManager.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  protected void initializeInjector() {
  }
}
