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

package com.zis.musapp.base.test;

/**
 * Created by Zis{github.com/Zis} on 15/8/9.
 */
public final class MockProvider {

  private MockProvider() {
    // no instance
  }

  public static String provideGithubAPIErrorStr() {
    return "{\"message\":\"Validation Failed\",\"errors\":[{\"resource\":\"Issue\"," +
        "\"field\":\"title\",\"code\":\"missing_field\"}]}";
  }

  public static String provideGithubUserStr() {
    return "{\"login\":\"Zis\",\"id\":3098704,\"avatar_url\":\"https://avatars" +
        ".githubusercontent.com/u/3098704?v=3\",\"gravatar_id\":\"\"," +
        "\"url\":\"https://api.github.com/users/Zis\",\"html_url\":\"https://github" +
        ".com/Zis\",\"followers_url\":\"https://api.github.com/users/Zis/followers\"," +
        "\"following_url\":\"https://api.github.com/users/Zis/following{/other_user}\"," +
        "\"gists_url\":\"https://api.github.com/users/Zis/gists{/gist_id}\"," +
        "\"starred_url\":\"https://api.github.com/users/Zis/starred{/owner}{/repo}\"," +
        "\"subscriptions_url\":\"https://api.github.com/users/Zis/subscriptions\"," +
        "\"organizations_url\":\"https://api.github.com/users/Zis/orgs\"," +
        "\"repos_url\":\"https://api.github.com/users/Zis/repos\"," +
        "\"events_url\":\"https://api.github.com/users/Zis/events{/privacy}\"," +
        "\"received_events_url\":\"https://api.github.com/users/Zis/received_events\"," +
        "\"type\":\"User\",\"site_admin\":false,\"name\":\"Xu Jianlin\"," +
        "\"company\":\"YOLO\",\"blog\":\"http://Zis.github.io/\"," +
        "\"location\":\"Beijing, China\",\"email\":\"xz4215@gmail.com\"," +
        "\"hireable\":true,\"bio\":null,\"public_repos\":21,\"public_gists\":3," +
        "\"followers\":3,\"following\":25,\"created_at\":\"2012-12-21T14:23:30Z\"," +
        "\"updated_at\":\"2015-07-23T07:27:29Z\"}";
  }

  public static String provideSimplifiedGithubUserStr() {
    return "{\"login\":\"Zis\",\"avatar_url\":\"https://avatars.githubusercontent" +
        ".com/u/3098704?v=3\",\"type\":\"User\",\"created_at\":\"2012-12-21T14:23:30Z\"}";
  }

  public static String provideEmptyGithubSearchResult() {
    return "{\"total_count\":0,\"incomplete_results\":false,\"items\":[]}";
  }

  public static String provideSimplifiedGithubUserSearchResultStr() {
    return "{\"total_count\":1,\"incomplete_results\":false,\"items\":[{\"login\":\"Zis\"," +
        "\"avatar_url\":\"https://avatars.githubusercontent.com/u/3098704?v=3\"," +
        "\"type\":\"User\"}]}";
  }

  public static String provideLGMetaV1() {
    return "{\"version\":1,\"parts\":[\"ligui-pics-part-1.json\"]}";
  }

  public static String provideLGMetaV2() {
    return "{\"version\":2,\"parts\":[\"ligui-pics-part-1.json\"]}";
  }

  public static String provideLGAlbumsV1() {
    return "[{\"name\":\"[Ligui] 丽柜 2016.04.04 Model 妮可 [30P]\",\"cover\":\"http://i7.umei" +
        ".cc//img2012/2016/04/01/005LIGUI20160404/cover.jpg\"," +
        "\"firstPageUrl\":\"http://www.umei.cc/p/gaoqing/cn/20160405185812.htm\"," +
        "\"pics\":[\"http://i7.umei.cc//img2012/2016/04/01/005LIGUI20160404/000_7590" +
        ".jpg\",\"http://i7.umei.cc//img2012/2016/04/01/005LIGUI20160404/cover.jpg\"]}]";
  }

  public static String provideLGAlbumsV2() {
    return "[{\"name\":\"[Ligui] 丽柜 2016.04.04 Model 妮可 [30P]\",\"cover\":\"http://i7.umei" +
        ".cc//img2012/2016/04/01/005LIGUI20160404/cover.jpg\"," +
        "\"firstPageUrl\":\"http://www.umei.cc/p/gaoqing/cn/20160405185812.htm\"," +
        "\"pics\":[\"http://i7.umei.cc//img2012/2016/04/01/005LIGUI20160404/000_7590" +
        ".jpg\",\"http://i7.umei.cc//img2012/2016/04/01/005LIGUI20160404/cover.jpg\"]}," +
        "{\"name\":\"[Ligui] 丽柜 2016.03.31 Model 妮可 [25P]\",\"cover\":\"http://i7.umei" +
        ".cc//img2012/2016/04/01/001LIGUI20160331/cover.jpg\"," +
        "\"firstPageUrl\":\"http://www.umei.cc/p/gaoqing/cn/20160401195701.htm\"," +
        "\"pics\":[\"http://i7.umei.cc//img2012/2016/04/01/001LIGUI20160331/000_7513" +
        ".jpg\",\"http://i7.umei.cc//img2012/2016/04/01/001LIGUI20160331/cover.jpg\"]}]";
  }
}
