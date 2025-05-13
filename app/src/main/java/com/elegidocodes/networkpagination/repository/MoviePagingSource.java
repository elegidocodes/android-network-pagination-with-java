package com.elegidocodes.networkpagination.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;

import com.elegidocodes.networkpagination.application.MyRetrofit;
import com.elegidocodes.networkpagination.model.Movie;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * A {@link RxPagingSource} implementation that loads paginated movie data
 * from the TMDB API using Retrofit and RxJava.
 *
 * <p>This class defines how to load individual pages of data, how to refresh
 * the current paging state, and how to handle errors and empty results.</p>
 */
public class MoviePagingSource extends RxPagingSource<Integer, Movie> {

    private static final String TAG = "MoviePagingSource";

    /**
     * Timeout for API requests to prevent indefinite hangs.
     */
    private static final int CACHE_TIMEOUT_MINUTES = 30;

    // Optional: Inject and monitor network state if needed
    // private final NetworkStateManager networkState;

    // Optional constructor if using dependency injection
    /*public MoviePagingSource(NetworkStateManager networkState) {
        this.networkState = networkState;
    }*/

    /**
     * Loads a single page of movie data from the TMDB API.
     *
     * @param loadParams Parameters including page key and load size
     * @return A {@link Single} emitting the result of the page load (success or error)
     */
    @NonNull
    @Override
    public Single<LoadResult<Integer, Movie>> loadSingle(@NonNull LoadParams<Integer> loadParams) {
        try {
            final int page = loadParams.getKey() != null ? loadParams.getKey() : 1;
            final int pageSize = loadParams.getLoadSize();

            return MyRetrofit.getService()
                    .getPopularMovies(page)
                    .subscribeOn(Schedulers.io()) // Run on background thread
                    .timeout(CACHE_TIMEOUT_MINUTES, TimeUnit.MINUTES) // Prevent hanging
                    .map(result -> {
                        List<Movie> movies = result.getResults();
                        int totalPages = result.getTotalPages();

                        if (movies == null) {
                            throw new IllegalStateException("Invalid API response - null movies list");
                        }

                        return toLoadResult(movies, page, totalPages, pageSize);
                    })
                    .doOnEvent((result, error) -> {
                        // Optional: Hook for UI/network state feedback
                        // if (networkState != null) networkState.setState(NetworkState.LOADED);
                    })
                    .doOnError(throwable ->
                            Log.e(TAG, "Error loading page " + page, throwable))
                    .onErrorReturn(LoadResult.Error::new); // Graceful fallback on failure
        } catch (Exception e) {
            return Single.just(new LoadResult.Error<>(e)); // Catch unexpected errors
        }
    }

    /**
     * Determines the page key to use when refreshing data (e.g. on swipe-to-refresh).
     *
     * @param pagingState The current paging state from the Paging library
     * @return The page key to refresh from, or null if undetermined
     */
    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, Movie> pagingState) {
        Integer anchorPosition = pagingState.getAnchorPosition();
        if (anchorPosition == null) return null;

        LoadResult.Page<Integer, Movie> anchorPage =
                pagingState.closestPageToPosition(anchorPosition);
        if (anchorPage == null) return null;

        Integer prevKey = anchorPage.getPrevKey();
        if (prevKey != null) return prevKey + 1;

        Integer nextKey = anchorPage.getNextKey();
        if (nextKey != null) return nextKey - 1;

        return null;
    }

    /**
     * Converts a raw list of movies into a {@link LoadResult.Page} object,
     * including proper pagination key logic and item count estimation.
     *
     * @param movies      The list of movies fetched from the API
     * @param currentPage The current page number
     * @param totalPages  The total number of pages reported by the API
     * @param pageSize    The number of items requested per page
     * @return A LoadResult.Page representing this page's data and pagination keys
     */
    private LoadResult<Integer, Movie> toLoadResult(
            @NonNull List<Movie> movies,
            int currentPage,
            int totalPages,
            int pageSize
    ) {
        if (movies.isEmpty()) {
            return new LoadResult.Page<>(
                    movies,
                    null,
                    null,
                    LoadResult.Page.COUNT_UNDEFINED,
                    LoadResult.Page.COUNT_UNDEFINED
            );
        }

        Integer prevKey = currentPage > 1 ? currentPage - 1 : null;
        Integer nextKey = currentPage < totalPages ? currentPage + 1 : null;

        int itemsBefore = (currentPage - 1) * pageSize;
        int itemsAfter = (totalPages - currentPage) * pageSize;

        return new LoadResult.Page<>(
                movies,
                prevKey,
                nextKey,
                itemsBefore,
                itemsAfter
        );
    }

}
