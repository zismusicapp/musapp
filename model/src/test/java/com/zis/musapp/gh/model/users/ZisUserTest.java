package com.zis.musapp.gh.model.users;

import com.google.gson.Gson;
import com.zis.musapp.base.model.provider.GsonProviderExposure;
import com.zis.musapp.base.test.BaseThreeTenBPTest;
import com.zis.musapp.base.test.MockProvider;
import junit.framework.Assert;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

/**
 * Created by Zis on 15/8/11.
 */
public class ZisUserTest extends BaseThreeTenBPTest {

  private Gson mGson;

  @Before
  public void setUp() {
    initThreeTenBP();
    mGson = GsonProviderExposure.exposeGson();
  }

  @Test
  public void testAutoParcelAutoGson() {
    final String mock = MockProvider.provideGithubUserStr();
    final ZisUser converted = mGson.fromJson(mock, ZisUser.class);
    Assert.assertNotNull(converted);
    try {
      JSONAssert.assertEquals(MockProvider.provideSimplifiedGithubUserStr(),
          mGson.toJson(converted), false);
    } catch (JSONException e) {
      Assert.assertTrue(false);
    }

    final ZisUser built = ZisUser.builder()
        .id(converted.id())
        .login(converted.login())
        .avatar_url(converted.avatar_url())
        .type(converted.type())
        .created_at(converted.created_at())
        .build();
    Assert.assertEquals(converted, built);
  }
}
