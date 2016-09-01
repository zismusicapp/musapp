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

import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.BriteDatabase;
import com.zis.musapp.base.di.ActivityScope;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

/**
 * Created by Zis{github.com/Zis} on 15/8/14.
 */
@ActivityScope
public final class DbUserDelegateImpl implements DbUserDelegate {
  private final BriteDatabase mBriteDb;

  @Inject DbUserDelegateImpl(final BriteDatabase briteDb) {
    this.mBriteDb = briteDb;
  }

  @Override
  public void deleteAllUsers() {
    mBriteDb.execute(ZisUser.DELETE_ALL);
  }

  @SuppressWarnings({
      "PMD.OneDeclarationPerLine", "PMD.LocalVariableCouldBeFinal",
      "PMD.DataflowAnomalyAnalysis"
  })
  @Override
  public void putAllUsers(final List<ZisUser> users) {
    final BriteDatabase.Transaction transaction = mBriteDb.newTransaction();
    try {
      for (int i = 0, size = users.size(); i < size; i++) {
        mBriteDb.insert(ZisUser.TABLE_NAME,
            ZisUser.FACTORY.marshal(users.get(i)).asContentValues(),
            SQLiteDatabase.CONFLICT_REPLACE);
      }
      transaction.markSuccessful();
    } finally {
      transaction.end();
    }
  }

  @Override
  public Observable<List<ZisUser>> getAllUsers() {
    return mBriteDb.createQuery(ZisUser.TABLE_NAME, ZisUser.GET_ALL)
        .mapToList(ZisUser.MAPPER::map);
  }

  @Override public Observable<ZisUser> getUserById(final Long id) {
    return mBriteDb.createQuery(ZisUser.TABLE_NAME, ZisUser.GETUSERBYID, Long.toString(id))
            .mapToOne(ZisUser.MAPPER::map);
  }

  @Override public Observable<ZisUser> getUserByLogin(final String login) {
    return mBriteDb.createQuery(ZisUser.TABLE_NAME, ZisUser.GETUSERBYLOGIN, login)
        .mapToOne(ZisUser.MAPPER::map);
  }
}
