/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Zis
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.zis.musapp.gh.model.users;

import android.support.annotation.NonNull;
import com.zis.musapp.base.di.ActivityScope;
import com.zis.musapp.gh.model.ServerApi;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

/**
 * Created by Zis{github.com/Zis} on 15/8/5.
 */
@ActivityScope public class ZisUserRepo {
  private final DbUserDelegate mDbUserDelegate;
  private final ServerApi mServerApi;

  @Inject public ZisUserRepo(final DbUserDelegate dbUserDelegate, final ServerApi serverApi) {
    mDbUserDelegate = dbUserDelegate;
    mServerApi = serverApi;
  }

  /**
   * search user.
   *
   * @param query search query.
   * @return search result observable.
   */
  @NonNull public Observable<List<ZisUser>> searchUser(@NonNull final String query) {
    return mServerApi.searchUsers(query, ServerApi.SERVER_API_PARAMS_SEARCH_SORT_JOINED,
        ServerApi.SERVER_API_PARAMS_SEARCH_ORDER_DESC)
        .map(ZisUserSearchResult::items)
        .doOnNext(mDbUserDelegate::putAllUsers);
  }

  /**
   * get user by id
   *
   * @param id user id
   * @return search result observable.
   */
  @NonNull public Observable<ZisUser> getUser(final boolean forceTryNetwork,
      @NonNull final Long id) {

    final Observable<ZisUser> getUserNetwork = mServerApi.getUserById(id)
        .flatMap(zisUserSearchResult -> Observable.from(zisUserSearchResult.items()));

    final Observable<ZisUser> getUserDb = mDbUserDelegate.getUserById(id);

    if (forceTryNetwork) {
      return getUserNetwork.onExceptionResumeNext(getUserDb);
    } else {
      return getUserNetwork.switchMap(zisUserObservable -> getUserDb);
    }
  }

  /**
   * logout user
   */
  public void logout() {
    mServerApi.logout();
  }
}
