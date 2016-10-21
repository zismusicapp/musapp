package com.zis.musapp.gh.features.profile;

import android.os.Bundle;
import android.widget.Toast;
import com.yatatsu.autobundle.AutoBundleField;
import com.zis.musapp.base.android.BaseActivity;
import com.zis.musapp.gh.model.users.ZisUser;

public class ProfileActivity extends BaseActivity {

  @AutoBundleField
  ZisUser mUser;

  @Override
  protected boolean hasArgs() {
    return true;
  }

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Toast.makeText(this, mUser.login(), Toast.LENGTH_SHORT).show();
  }

  @Override
  protected void initializeInjector() {
  }

  @Override
  public void onBackPressed() {
    setResult(RESULT_OK);
    super.onBackPressed();
  }
}
