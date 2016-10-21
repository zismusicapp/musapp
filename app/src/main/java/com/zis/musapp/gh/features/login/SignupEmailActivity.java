package com.zis.musapp.gh.features.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.zis.musapp.gh.BootstrapActivity;
import com.zis.musapp.gh.R;

public class SignupEmailActivity extends BootstrapActivity {
  private static final String TAG = "SignupEmailActivity";

  @BindView(R.id.input_name)
  EditText mNameText;
  @BindView(R.id.input_address)
  EditText mAddressText;
  @BindView(R.id.input_email)
  EditText mEmailText;
  @BindView(R.id.input_mobile)
  EditText mMobileText;
  @BindView(R.id.input_password)
  EditText mPasswordText;
  @BindView(R.id.input_reEnterPassword)
  EditText mReEnterPasswordText;
  @BindView(R.id.btn_signup)
  Button mSignupButton;
  @BindView(R.id.link_login)
  TextView mLoginLink;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_signup);
    ButterKnife.bind(this);

    mSignupButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        signup();
      }
    });

    mLoginLink.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // Finish the registration screen and return to the Login activity
        Intent intent = new Intent(getApplicationContext(), LoginEmailActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
      }
    });
  }

  @Override
  protected void initializeInjector() {

  }

  public void signup() {
    Log.d(TAG, "Signup");

    if (!validate()) {
      onSignupFailed();
      return;
    }

    mSignupButton.setEnabled(false);

    final ProgressDialog progressDialog = new ProgressDialog(SignupEmailActivity.this);
    progressDialog.setIndeterminate(true);
    progressDialog.setMessage("Creating Account...");
    progressDialog.show();

    String name = mNameText.getText().toString();
    String address = mAddressText.getText().toString();
    String email = mEmailText.getText().toString();
    String mobile = mMobileText.getText().toString();
    String password = mPasswordText.getText().toString();
    String reEnterPassword = mReEnterPasswordText.getText().toString();

    // TODO: Implement your own signup logic here.

    new android.os.Handler().postDelayed(
        new Runnable() {
          public void run() {
            // On complete call either onSignupSuccess or onSignupFailed
            // depending on success
            onSignupSuccess();
            // onSignupFailed();
            progressDialog.dismiss();
          }
        }, 3000);
  }

  public void onSignupSuccess() {
    mSignupButton.setEnabled(true);
    setResult(RESULT_OK, null);
    finish();
  }

  public void onSignupFailed() {
    Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

    mSignupButton.setEnabled(true);
  }

  public boolean validate() {
    boolean valid = true;

    String name = mNameText.getText().toString();
    String address = mAddressText.getText().toString();
    String email = mEmailText.getText().toString();
    String mobile = mMobileText.getText().toString();
    String password = mPasswordText.getText().toString();
    String reEnterPassword = mReEnterPasswordText.getText().toString();

    if (name.isEmpty() || name.length() < 3) {
      mNameText.setError("at least 3 characters");
      valid = false;
    } else {
      mNameText.setError(null);
    }

    if (address.isEmpty()) {
      mAddressText.setError("Enter Valid Address");
      valid = false;
    } else {
      mAddressText.setError(null);
    }

    if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
      mEmailText.setError("enter a valid email address");
      valid = false;
    } else {
      mEmailText.setError(null);
    }

    if (mobile.isEmpty() || mobile.length() != 10) {
      mMobileText.setError("Enter Valid Mobile Number");
      valid = false;
    } else {
      mMobileText.setError(null);
    }

    if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
      mPasswordText.setError("between 4 and 10 alphanumeric characters");
      valid = false;
    } else {
      mPasswordText.setError(null);
    }

    if (reEnterPassword.isEmpty()
        || reEnterPassword.length() < 4
        || reEnterPassword.length() > 10
        || !(reEnterPassword.equals(password))) {
      mReEnterPasswordText.setError("Password Do not match");
      valid = false;
    } else {
      mReEnterPasswordText.setError(null);
    }

    return valid;
  }
}