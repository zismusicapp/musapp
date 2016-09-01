package com.zis.musapp.gh.model.users;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zis.musapp.base.model.provider.GsonProviderExposure;
import com.zis.musapp.base.test.BaseThreeTenBPTest;
import com.zis.musapp.base.test.MockProvider;
import junit.framework.Assert;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

/**
 * Created by Zis{github.com/Zis} on 15/8/11.
 */
public class ZisUserSearchResultTest extends BaseThreeTenBPTest {

  private Gson mGson;

  @Before
  public void setUp() {
    initThreeTenBP();
    mGson = GsonProviderExposure.exposeGson();
  }

  @Test
  public void testSerialize() {
    final ZisUserSearchResult converted =
        mGson.fromJson(MockProvider.provideSimplifiedGithubUserSearchResultStr(),
            new TypeToken<ZisUserSearchResult>() {
            }.getType());
    try {
      JSONAssert.assertEquals(MockProvider.provideSimplifiedGithubUserSearchResultStr(),
          mGson.toJson(converted), false);
      Assert.assertTrue(true);
    } catch (JSONException e) {
      Assert.assertTrue(false);
    }
  }
}
