package com.elegidocodes.networkpagination.application;

import android.util.Log;

import com.elegidocodes.networkpagination.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A singleton Retrofit client configured to communicate with The Movie Database (TMDB) API.
 *
 * <p>This class sets up Retrofit with:
 * <ul>
 *     <li>Custom {@link OkHttpClient} including timeouts</li>
 *     <li>Automatic TMDB API key injection via request interceptor</li>
 *     <li>Gson for JSON parsing</li>
 *     <li>RxJava3 support for reactive API calls</li>
 * </ul>
 *
 * <p>Use {@link #getService()} to access the API implementation.</p>
 */
public class MyRetrofit {

    /**
     * Base URL for TMDB API (v3).
     */
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    /**
     * Singleton instance of Retrofit to prevent redundant reinitialization.
     */
    private static Retrofit retrofit;

    /**
     * Private constructor to prevent instantiation.
     */
    private MyRetrofit() {
        // Prevent instantiation - access through getService()
    }

    /**
     * Returns a singleton instance of {@link MyAPI}, the Retrofit service interface for TMDB API.
     * Initializes Retrofit and OkHttpClient on first call with appropriate configuration:
     * <ul>
     *     <li>Timeouts set to 59 seconds</li>
     *     <li>Interceptor that injects the API key into every request</li>
     *     <li>Gson and RxJava3 adapters added</li>
     * </ul>
     *
     * @return an instance of {@link MyAPI} for making network requests
     */
    public static MyAPI getService() {

        String apiKey = BuildConfig.apiKey;

        if (retrofit == null) {
            // Configure OkHttpClient with timeouts and API key interceptor
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(59, TimeUnit.SECONDS) // Time allowed to establish connection
                    .writeTimeout(59, TimeUnit.SECONDS)   // Time allowed to write data
                    .readTimeout(59, TimeUnit.SECONDS)    // Time allowed to receive data

                    // Interceptor to append API key to every request
                    .addInterceptor(chain -> {
                        Request original = chain.request();
                        HttpUrl originalHttpUrl = original.url();

                        // Append API key to query parameters
                        HttpUrl url = originalHttpUrl.newBuilder()
                                .addQueryParameter("api_key", apiKey) //API_KEY
                                .build();

                        // Rebuild the request with updated URL
                        Request.Builder requestBuilder = original.newBuilder().url(url);
                        Request request = requestBuilder.build();

                        return chain.proceed(request);
                    })
                    .build();

            // Build Retrofit instance with RxJava and Gson support
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // Parse JSON using Gson
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) // Support RxJava3
                    .client(client)
                    .build();
        }

        return retrofit.create(MyAPI.class);
    }
}
