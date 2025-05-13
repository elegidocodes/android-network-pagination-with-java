package com.elegidocodes.networkpagination.di;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.elegidocodes.networkpagination.R;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

/**
 * Dagger Hilt module that provides application-level dependencies.
 * This module is installed in the {@link SingletonComponent}, meaning
 * all the provided dependencies live as long as the application does.
 */
@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    /**
     * Provides a singleton instance of {@link RequestManager} (Glide)
     * configured with default request options for error and placeholder images.
     *
     * @param context The application context, injected by Hilt using {@link ApplicationContext}.
     * @return A singleton instance of Glide's {@link RequestManager} with default options applied.
     */
    @Provides
    @Singleton
    public RequestManager getGlide(@ApplicationContext Context context) {
        return Glide.with(context)
                .applyDefaultRequestOptions(new RequestOptions()
                        .error(R.drawable.icon_exclamation_24px)        // Image shown on load failure
                        .placeholder(R.drawable.icon_image_24px));      // Image shown while loading
    }

}

