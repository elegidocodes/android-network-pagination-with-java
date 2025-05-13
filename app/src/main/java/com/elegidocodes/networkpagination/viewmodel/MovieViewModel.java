package com.elegidocodes.networkpagination.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;

import com.elegidocodes.networkpagination.model.Movie;
import com.elegidocodes.networkpagination.repository.MoviePagingSource;

import io.reactivex.rxjava3.core.Flowable;
import kotlinx.coroutines.CoroutineScope;

/**
 * ViewModel for managing paginated movie data using Paging 3 and RxJava3.
 *
 * <p>This class exposes a {@link Flowable} of {@link PagingData} that the UI can observe
 * to efficiently load and display a large list of movies with built-in support for
 * pagination, caching, and lifecycle awareness.</p>
 */
public class MovieViewModel extends ViewModel {

    // Paging configuration constants
    private static final int PAGE_SIZE = 20;
    private static final int PREFETCH_DISTANCE = PAGE_SIZE / 2;
    private static final int INITIAL_LOAD_SIZE = PAGE_SIZE * 3;
    private static final int MAX_CACHE_SIZE = PAGE_SIZE * 5;

    /**
     * The stream of paginated movie data observed by the UI.
     */
    private final Flowable<PagingData<Movie>> pagingDataFlowable;

    /**
     * Coroutine scope tied to this ViewModel's lifecycle for caching.
     */
    private final CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);

    /**
     * Constructor that initializes the paging flow using {@link Pager}.
     * The paging data is cached in the ViewModel's coroutine scope.
     */
    public MovieViewModel() {
        Pager<Integer, Movie> pager = createPager();
        pagingDataFlowable = PagingRx.getFlowable(pager)
                .compose(flowable -> PagingRx.cachedIn(flowable, viewModelScope));
    }

    /**
     * Creates and configures the {@link Pager} used to load movie data.
     *
     * @return a configured Pager instance
     */
    private Pager<Integer, Movie> createPager() {
        return new Pager<>(
                new PagingConfig(
                        PAGE_SIZE,          // Items per page
                        PREFETCH_DISTANCE,  // When to start loading the next page
                        false,              // Placeholders disabled (optional)
                        INITIAL_LOAD_SIZE,  // Items to load initially
                        MAX_CACHE_SIZE      // Max items cached in memory
                ),
                this::createPagingSource
        );
    }

    /**
     * Creates a new instance of the {@link MoviePagingSource}.
     * Replace with DI-based creation if using Hilt or other frameworks.
     *
     * @return a new MoviePagingSource
     */
    private MoviePagingSource createPagingSource() {
        return new MoviePagingSource(/* pass dependencies here if needed */);
    }

    /**
     * Returns a {@link Flowable} that emits paginated movie data to be observed by the UI.
     *
     * @return a reactive stream of paginated movie data
     */
    public Flowable<PagingData<Movie>> getPagingDataFlowable() {
        return pagingDataFlowable;
    }

    /**
     * Returns a {@link Flowable} with optional global error handling for UI consumption.
     *
     * <p>This method provides a fallback mechanism in case of upstream errors
     * by switching to an empty list rather than crashing the stream.</p>
     *
     * @return a Flowable of PagingData with error resilience
     */
    public Flowable<PagingData<Movie>> getPagingDataWithErrorHandling() {
        return pagingDataFlowable
                .onErrorResumeNext(throwable -> {
                    // Log or handle errors globally here if needed
                    return Flowable.empty();
                });
    }
}
