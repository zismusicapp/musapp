/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Zis
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

package com.zis.musapp.gh.di;

import com.zis.musapp.base.di.ActivityModule;
import com.zis.musapp.base.model.jsr310.JSR310Module;
import com.zis.musapp.base.model.provider.ProviderModule;
import com.zis.musapp.base.utils.UtilsModule;
import com.zis.musapp.gh.features.kiddashboard.ObserveMapComponent;
import com.zis.musapp.gh.features.kiddashboard.ObserveMapModule;
import com.zis.musapp.gh.features.search.di.SearchComponent;
import com.zis.musapp.gh.features.search.di.SearchModule;
import com.zis.musapp.gh.features.splash.di.SplashComponent;
import com.zis.musapp.gh.model.GsonProviderConfigModule;
import dagger.Component;
import javax.inject.Singleton;

/**
 * Created by Zis{github.com/Zis} on 15/7/23.
 *
 * DI appComponent abstraction for Application scope. Application scope is {@link Singleton} scope
 *
 * We put it inside app module, because {@link ProviderConfigModule} must locate in app module, and
 * there is no other side effect with this manner, besides, we can use {@link dagger.Subcomponent}
 * to define sub component, which need less code.
 */
@SuppressWarnings("checkstyle:")
@Singleton
@Component(
    modules = {
        AppModule.class,

        ProviderModule.class, ProviderConfigModule.class, GsonProviderConfigModule.class,

        JSR310Module.class, UtilsModule.class
    })
public interface AppComponent {
  SplashComponent plus();

  SearchComponent plus(ActivityModule activityModule, SearchModule searchModule);

  ObserveMapComponent plus(ObserveMapModule observeMapModule);
}
