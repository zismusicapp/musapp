# Zis Android app project
Description will be here
 
based on Android TDD bootstrap project

# Pre
JDK 8 must be installed
"java -version" should display >= 1.8
# Installation

On Unix, run the install.sh script from AndroidCodeQualityConfig/ideaConfigs. Windows users should use install.bat instead.
Restart IntelliJ if it's running.
Open IntelliJ Project Settings -> Code Styles, change the code style for the project to the one you want.

# Run
./buildsystem/ci.sh                 - should be runned before push
./buildsystem/installDebug.sh       - to install debug version (device should be connected)
./buildsystem/installRelease.sh     - to install release version (device should be connected)

# Android TDD bootstrap project
[![Master branch build status](https://travis-ci.org/Piasy/AndroidTDDBootStrap.svg?branch=master)](https://travis-ci.org/Piasy/AndroidTDDBootStrap)
[![codecov.io](http://codecov.io/github/Piasy/AndroidTDDBootStrap/coverage.svg?branch=master)](http://codecov.io/github/Piasy/AndroidTDDBootStrap?branch=master) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-AndroidTDDBootStrap-green.svg?style=flat)](https://android-arsenal.com/details/3/2586)  

An Android TDD bootstrap project, use a collection of new technology, obey best practices, inspired from some popular architectures, develop with many handy tools.

## Why another bootstrap project?
From the beginning of this year 2015, our team start a new project, and before we develop functionality in detail, we try to create a well-architecture project from scratch, with well designed network layer, data layer, asynchronous execution, communication between modules, and last but not least: unit test and integrate test support. After several months of developing, we found some drawback of our current architecture, and also found some popular architectures, then I decide to extract our original well designed architecture and open source it, with amendment according to the drawback and features from new popular architectures we found.
Recently I have seen a lot of bootstrap/base Android project, including [JakeWharton's u2020](https://github.com/JakeWharton/u2020), [mobiwiseco's Android-Base-Project](https://github.com/mobiwiseco/Android-Base-Project), etc, but none of these projects cover all features I include in this AndroidTDDBootStrap project. That's why I want more people to see this repo, and I also want get feedback from more people to improve this project.

## Architecture
Based on the project architecture I'm currently work on, [YOLO](https://www.yoloyolo.tv/), and inspired from popular architectures: [Android Clean Architecture](https://github.com/android10/Android-CleanArchitecture), [Against Android Unit Tests: The Square way](http://www.philosophicalhacker.com/2015/04/10/against-android-unit-tests/).

![perfect_android_model_layer](http://blog.piasy.com/img/201605/perfect_android_model_layer.png)

+  MVP: [Mosby](https://github.com/sockeqwe/mosby), A Model-View-Presenter library for modern Android apps.
+  Dependency injection
  +  [Dagger2](https://github.com/google/dagger), A fast dependency injector for Android and Java.
  +  [ButterKnife](https://github.com/JakeWharton/butterknife), View "injection" library for Android.
+  Model layer, [完美的安卓 model 层架构（上）](http://blog.piasy.com/2016/05/06/Perfect-Android-Model-Layer/)
  +  [OkHttp](http://square.github.io/okhttp/), An HTTP+SPDY client for Android and Java applications.
  +  [Retrofit](http://square.github.io/retrofit/), Type-safe HTTP client for Android and Java by Square, Inc.
  +  [Gson](https://github.com/google/gson), A Java library that can be used to convert Java Objects into their JSON representation.
  +  [SQLBrite](https://github.com/square/sqlbrite), A lightweight wrapper around SQLiteOpenHelper which introduces reactive stream semantics to SQL operations..
  +  [SQLDelight](https://github.com/square/sqldelight), Generates Java models from CREATE TABLE statements.
  +  [AutoValue](https://github.com/google/auto/tree/master/value), Generated immutable value classes for Java 1.6+.
  +  [auto-value-gson](https://github.com/Piasy/auto-value-gson/tree/autogson), AutoValue Extension to add Gson De/Serializer support.
  +  [auto-value-parcel](https://github.com/rharter/auto-value-parcel), An Android Parcelable extension for Google's AutoValue.
+  Reactive programming
  +  [RxJava](https://github.com/ReactiveX/RxJava) – Reactive Extensions for the JVM – a library for composing asynchronous and event-based programs using observable sequences for the Java VM.
  +  [RxAndroid](https://github.com/ReactiveX/RxAndroid), RxJava bindings for Android.
  +  [RxBinding](https://github.com/JakeWharton/RxBinding), RxJava binding APIs for Android's UI widgets.
  +  [RxLifecycle](https://github.com/trello/RxLifecycle), Lifecycle handling APIs for Android apps using RxJava.
+  Communication between modules: [EventBus](https://github.com/greenrobot/EventBus), Android optimized event bus that simplifies communication between Activities, Fragments, Threads, Services, etc. Less code, better quality.
+  Image loader: [Fresco](https://github.com/facebook/fresco), An Android library for managing images and the memory they use.
+  Developer tools
  +  [XLog](https://github.com/promeG/XLog), Method call logging based on dexposed.
  +  [Android Lint Summary](https://github.com/passy/android-lint-summary), View your Android lint issues with style.
  +  [LeakCanary](https://github.com/square/leakcanary), A memory leak detection library for Android and Java.
  +  [ANR-WatchDog](https://github.com/SalomonBrys/ANR-WatchDog), A simple watchdog that detects Android ANR (Application Not Responding) error and throws a meaningful exception.
  +  [BlockCanary](https://github.com/moduth/blockcanary), A transparent ui-block detection library for Android.
  +  [RetroLambda](https://github.com/orfjackal/retrolambda), Backport of Java 8's lambda expressions to Java 7, 6 and 5.
  +  [Timber](https://github.com/JakeWharton/timber), A logger with a small, extensible API which provides utility on top of Android's normal Log class.
  +  [OkHttp Logging Interceptor](https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor), An OkHttp interceptor which logs HTTP request and response data.
  +  [Ok2Curl](https://github.com/mrmike/Ok2Curl), Convert OkHttp requests into curl logs.
  +  [Stetho](https://github.com/facebook/stetho), Stetho is a debug bridge for Android applications, enabling the powerful Chrome Developer Tools and much more.
  +  [strictmode-notifier](https://github.com/nshmura/strictmode-notifier), Improving StrictMode's report on Android.
  +  [android-git-sha-plugin](https://github.com/promeG/android-git-sha-plugin), Automatically add current GIT SHA value to your apk. It can rise an error if the current git branch is dirty.
  +  [Codestyle](https://github.com/Piasy/java-code-styles), Customized base on [Square java-code-styles](https://github.com/square/java-code-styles).
+  Others
  +  [Iconfy](https://github.com/JoanZapata/android-iconify), Android integration of multiple icon providers such as FontAwesome, Entypo, Typicons,...
  +  [ThreeTenABP](https://github.com/JakeWharton/ThreeTenABP), An adaptation of the JSR-310 backport for Android.
  +  [AutoBundle](https://github.com/yatatsu/AutoBundle/releases/tag/1.0.2), AutoBundle generates boilerplate code for field binding with android.os.Bundle.
  +  [BugTags](https://www.bugtags.com/), Crash report.
  +  [SafelyAndroid](https://github.com/Piasy/SafelyAndroid), Build safely Android app, no more Activity not found error and Activity state loss error!
  +  [FlexLayout](https://github.com/mmin18/FlexLayout), A powerful Android layout view that use java expression in layout params to describe relative positions.
  +  [Once](https://github.com/jonfinerty/Once), A small Android library to manage one-off operations.
+  Unit test
  +  Junit && Android Junit
  +  Following the [Square Way](http://www.philosophicalhacker.com/2015/04/10/against-android-unit-tests/)
  +  [Android Unmock Gradle Plugin](https://github.com/bjoernQ/unmock-plugin), Gradle plugin to be used in combination with the new unit testing feature of the Gradle Plugin / Android Studio to use real classes for e.g. SparseArray.
  +  [mockito](http://mockito.org/), Tasty mocking framework for unit tests in Java.
  +  [RESTMock](https://github.com/andrzejchm/RESTMock), HTTP Server for Android Instrumentation tests.
  +  [Espresso](https://code.google.com/p/android-test-kit/)
+  Continuous integration
  +  [Travis CI](https://travis-ci.org/)
+  Code quality, customized from [Vincent Brison's vb-android-app-quality repo](https://github.com/vincentbrison/vb-android-app-quality)
  +  [AndroidCodeQualityConfig](https://github.com/Piasy/AndroidCodeQualityConfig)
  +  [Checkstyle](https://github.com/checkstyle/checkstyle), Checkstyle is a development tool to help programmers write Java code that adheres to a coding standard. By default it supports the Google Java Style Guide and Sun Code Conventions, but is highly configurable. It can be invoked with an ANT task and a command line program.
  +  [Find bugs](https://github.com/findbugsproject/findbugs), FindBugs is a defect detection tool for Java that uses static analysis to look for more than 200 bug patterns, such as null pointer dereferences, infinite recursive loops, bad uses of the Java libraries and deadlocks.
  +  [PMD](https://github.com/pmd/pmd), PMD is a source code analyzer.
  +  Lint, [Android Lint Summary](https://github.com/passy/android-lint-summary), View your Android lint issues with style.
+  Code coverage
  +  Jacoco && [Codecov](https://codecov.io)

## Project structure
+  base
  +  The so called architecture part, and base classes, best practice, etc.
+  model
  +  Business related data layer, entities, APIs, DAOs, etc.
+  app
  +  App functionality.
+  package organization
  +  package by layer v.s. package by feature, read more about the [Package organization part of this blog](http://fernandocejas.com/2015/07/18/architecting-android-the-evolution/), and [Package by feature, not layer](http://www.javapractices.com/topic/TopicAction.do?Id=205).
  +  package by layer + package by feature
    +  network API, data object, DAO are organized in the single `model` library module, but inside model module, classes are organized by feature
    +  app functionality are organized by feature, mvp, di, ui code are organized together

## Build tips
+  Sign key config

    Place KeyStore file in some place, and create a TemplateKeyStore.properties, and config the KeyStore in it, include `keystore`, `keystore.password`, `key.password`, `key.alias`.
+  To clone all submodules, please use `git clone --recursive git@github.com:Piasy/AndroidTDDBootStrap.git`
+  use flavor to control server configuration, use build type to control log behavior
  +  `dev` for development server, `prod` for production server
  +  `debug` enable log and dev tools, disable crash and analytics, `release` against it

## Dev tips
+  Create utils
  +  Create util class in common/common_android module
  +  Add @Provides annotated provider method in corresponding Module class(UtilsModule.java/AndroidUtilsModule.java)
  +  ~~Add expose method in AppComponent.java~~
+  Create Activity
+  Unit test
  +  use the check*.sh script in buildsystem dir
+  Run `./buildsystem/ci.sh` before git push.

## Todo
+  ~~CheckStyle~~
+  ~~re-arch the provider package~~
+  ~~Espresso test of presentation module~~
+  ~~refactor AppComponent~~
+  ~~facebook [BUCK](http://buckbuild.com) integration~~ integrate with [OkBuck](https://github.com/Piasy/OkBuck)
+  ~~Update dependencies~~
+  ~~refactor modules~~
+  NDK integrate
+  MVP source generator plugin
+  MVVM branch
+  react native branch
+  kotlin branch

## Coverage
![codecov.io](http://codecov.io/github/Piasy/AndroidTDDBootStrap/branch.svg?branch=master)

## Acknowledgement
+  Thanks for our team, [YOLO](https://www.yoloyolo.tv/).
+  Thanks for my colleague & mentor, [promeG](https://github.com/promeG/).
