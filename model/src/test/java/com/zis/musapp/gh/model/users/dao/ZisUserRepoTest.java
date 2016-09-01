package com.zis.musapp.gh.model.users.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zis.musapp.base.model.provider.GsonProviderExposure;
import com.zis.musapp.base.test.BaseThreeTenBPTest;
import com.zis.musapp.base.test.MockProvider;
import com.zis.musapp.gh.model.ApiErrorUtil;
import com.zis.musapp.gh.model.ServerApi;
import com.zis.musapp.gh.model.users.DbUserDelegate;
import com.zis.musapp.gh.model.users.ZisUserRepo;
import com.zis.musapp.gh.model.users.ZisUserSearchResult;
import com.zis.musapp.gh.model.users.ZisUser;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.observers.TestSubscriber;

import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Created by Zis{github.com/Zis} on 15/8/12.
 */
public class ZisUserRepoTest extends BaseThreeTenBPTest {

  @Rule
  public MockitoRule mMockitoRule = MockitoJUnit.rule();

  @Mock
  private DbUserDelegate mDbUserDelegate;
  @Mock
  private ServerApi mServerApi;
  private ZisUserRepo mZisUserRepo;

  private ZisUserSearchResult mEmptyResult;

  @Before
  public void setUp() {
    initThreeTenBP();
    final Gson gson = GsonProviderExposure.exposeGson();
    mEmptyResult = gson.fromJson(MockProvider.provideEmptyGithubSearchResult(),
        new TypeToken<ZisUserSearchResult>() {
        }.getType());

    mZisUserRepo = new ZisUserRepo(mDbUserDelegate, mServerApi);
  }

  @Test
  public void testSearchUserSuccess() {
    // given
    willReturn(Observable.create(new Observable.OnSubscribe<ZisUserSearchResult>() {
      @Override
      public void call(final Subscriber<? super ZisUserSearchResult> subscriber) {
        subscriber.onNext(mEmptyResult);
        subscriber.onCompleted();
      }
    })).given(mServerApi).searchUsers(anyString(), anyString(), anyString());

    // when
    final TestSubscriber<List<ZisUser>> subscriber = new TestSubscriber<>();
    mZisUserRepo.searchUser("Zis").subscribe(subscriber);
    subscriber.awaitTerminalEvent();

    // then
    then(mDbUserDelegate).should(timeout(100)).putAllUsers(anyListOf(ZisUser.class));
    verifyNoMoreInteractions(mDbUserDelegate);
    subscriber.assertNoErrors();

    then(mServerApi).should(timeout(100).only())
        .searchUsers(anyString(), anyString(), anyString());
  }

  @Test
  public void testSearchUserApiError() {
    // given
    willReturn(Observable.create(
        (Observable.OnSubscribe<ZisUserSearchResult>) subscriber -> {
          subscriber.onError(ApiErrorUtil.apiError());
        })).given(mServerApi).searchUsers(anyString(), anyString(), anyString());

    // when
    final TestSubscriber<List<ZisUser>> subscriber = new TestSubscriber<>();
    mZisUserRepo.searchUser("Zis").subscribe(subscriber);
    subscriber.awaitTerminalEvent();

    // then
    verifyZeroInteractions(mDbUserDelegate);
    subscriber.assertNoValues();
    subscriber.assertError(HttpException.class);

    then(mServerApi).should(timeout(100).only())
        .searchUsers(anyString(), anyString(), anyString());
  }
}
