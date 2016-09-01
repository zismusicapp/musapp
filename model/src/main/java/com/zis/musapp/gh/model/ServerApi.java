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

package com.zis.musapp.gh.model;

import com.zis.musapp.gh.model.users.ZisUserSearchResult;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Zis{github.com/Zis} on 15/7/23.
 *
 * Definition of SERVER API.
 */
public interface ServerApi {

  String SERVER_API_PARAMS_SEARCH_SORT_REPO = "repositories";
  String SERVER_API_PARAMS_SEARCH_SORT_JOINED = "joined";

  String SERVER_API_PARAMS_SEARCH_ORDER_ASC = "asc";
  String SERVER_API_PARAMS_SEARCH_ORDER_DESC = "desc";

  /**
   * search users.
   */
  @GET("search/users") Observable<ZisUserSearchResult> searchUsers(@Query("q") String query,
      @Query("sort") String sort, @Query("order") String order);

  @GET("logout") void logout();

  @GET("users") Observable<ZisUserSearchResult> getUserById(@Query("id") Long id);

}