package com.elegidocodes.networkpagination.application;

import android.app.Application;

import dagger.hilt.android.HiltAndroidApp;

/**
 * Custom application class required for using Dagger Hilt in the app.
 * <p>
 * Annotated with {@link HiltAndroidApp}, which triggers Hilt's code generation,
 * including a base class for your application that serves as the application-level dependency container.
 * <p>
 * This is the entry point for Hilt's dependency injection system.
 */
@HiltAndroidApp
public class MyApp extends Application {
    // No need to override anything unless you want to do application-wide setup.
}
