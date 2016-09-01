package com.zis.musapp.gh.model.errors;

import com.zis.musapp.base.model.provider.GsonProviderExposure;
import com.zis.musapp.base.test.BaseThreeTenBPTest;
import com.zis.musapp.base.test.MockProvider;
import com.zis.musapp.gh.model.ApiErrorUtil;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import rx.functions.Action1;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

/**
 * Created by Zis{github.com/Zis} on 5/5/16.
 */
public class RxNetErrorProcessorTest extends BaseThreeTenBPTest {
  @Rule
  public MockitoRule mMockitoRule = MockitoJUnit.rule();

  @Mock
  private Action1<ApiError> mApiErrorHandler;

  @Before
  public void setUp() {
    initThreeTenBP();
  }

  @Test
  public void testProcessOtherException() {
    RxNetErrorProcessor rxNetErrorProcessor =
        new RxNetErrorProcessor(GsonProviderExposure.exposeGson());
    Throwable throwable = new IllegalArgumentException();
    assertFalse(rxNetErrorProcessor.tryWithApiError(throwable, mApiErrorHandler));
    verify(mApiErrorHandler, never()).call(any());
  }

  @Test
  public void testProcessNonApiNetworkError() {
    RxNetErrorProcessor rxNetErrorProcessor =
        new RxNetErrorProcessor(GsonProviderExposure.exposeGson());
    assertFalse(
        rxNetErrorProcessor.tryWithApiError(ApiErrorUtil.nonApiError(), mApiErrorHandler));
    verify(mApiErrorHandler, never()).call(any());
  }

  @Test
  public void testProcessInvalidApiError() {
    RxNetErrorProcessor rxNetErrorProcessor =
        new RxNetErrorProcessor(GsonProviderExposure.exposeGson());
    assertFalse(rxNetErrorProcessor.tryWithApiError(ApiErrorUtil.invalidApiError(),
        mApiErrorHandler));
    verify(mApiErrorHandler, never()).call(any());
  }

  @Test
  public void testProcessApiError() {
    RxNetErrorProcessor rxNetErrorProcessor =
        new RxNetErrorProcessor(GsonProviderExposure.exposeGson());
    assertTrue(rxNetErrorProcessor.tryWithApiError(ApiErrorUtil.apiError(), mApiErrorHandler));
    ApiError apiError = GsonProviderExposure.exposeGson()
        .fromJson(MockProvider.provideGithubAPIErrorStr(), ApiError.class);
    verify(mApiErrorHandler, only()).call(apiError);
  }
}
