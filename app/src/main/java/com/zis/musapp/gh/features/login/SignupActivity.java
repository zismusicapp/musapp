package com.zis.musapp.gh.features.login;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding.view.RxView;
import com.joanzapata.iconify.widget.IconTextView;
import com.parse.ParseFacebookUtils;
import com.zis.musapp.base.utils.RxUtil;
import com.zis.musapp.gh.BootstrapActivity;
import com.zis.musapp.gh.R;
import com.zis.musapp.gh.features.choosesong.ChooseSongActivtity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import rx.parse.ParseFacebookObservable;

/**
 * Created by mikhailz on 24/09/2016.
 */
public class SignupActivity extends BootstrapActivity {

  @BindView(R.id.digitsLogin)
  IconTextView mDigits;

  @BindView(R.id.phoneDigits)
  EditText mPhoneDigits;

  @BindView(R.id.facebook)
  IconTextView mFacebookBtn;

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
        doDigitsLogin();
        return true;
      }
      return false;
    });

    RxView.clicks(mFacebookBtn)
        .subscribe(aVoid -> {
          ParseFacebookObservable.logInWithReadPermissions(SignupActivity.this,
              Arrays.asList("public_profile", "email"))
              .subscribe(parseUser -> {
                startChooseSongsActivity();
              },RxUtil.ON_ERROR_LOGGER);
        });

    mRxLoginManager.loginDigitsObservable()
        .map(digitLoginResult -> digitLoginResult.getSession().isValidUser())
        .subscribe(success -> {
          if (success) {
            startChooseSongsActivity();
          }
        }, RxUtil.ON_ERROR_LOGGER);

    RxView.clicks(mDigits)
        .subscribe(aVoid -> {
          doDigitsLogin();
        });
  }

  private void startChooseSongsActivity() {
    startActivity(new Intent(SignupActivity.this, ChooseSongActivtity.class));
  }

  private void doDigitsLogin() {
    mRxLoginManager.startDigitsLogining(mPhoneDigits.getText().toString());
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    //mRxLoginManager.onActivityResult(requestCode, resultCode, data);
    ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  protected void initializeInjector() {
  }
}
