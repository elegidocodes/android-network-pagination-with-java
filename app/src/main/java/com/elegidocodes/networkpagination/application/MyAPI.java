package com.elegidocodes.networkpagination.application;

import com.elegidocodes.networkpagination.model.PopularMoviesResult;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit service interface for The Movie Database (TMDB) API.
 *
 * <p>This interface defines the HTTP endpoints used by Retrofit to communicate
 * with the TMDB API. Each method corresponds to an API endpoint and returns
 * an asynchronous {@link Single} from RxJava3.</p>
 */
public interface MyAPI {

    /**
     * Fetches a paginated list of popular movies from the TMDB API.
     *
     * <p>This endpoint hits: {@code GET /movie/popular}</p>
     *
     * @param page The page number to fetch (1-based index)
     * @return A {@link Single} emitting a {@link PopularMoviesResult} containing movie data
     */
    @GET("movie/popular")
    Single<PopularMoviesResult> getPopularMovies(@Query("page") int page);

}

