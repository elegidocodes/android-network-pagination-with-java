package com.elegidocodes.networkpagination.ui;

import static android.view.View.GONE;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.LoadState;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.elegidocodes.networkpagination.R;
import com.elegidocodes.networkpagination.databinding.ActivityMainBinding;
import com.elegidocodes.networkpagination.ui.adapter.MovieAdapter;
import com.elegidocodes.networkpagination.ui.adapter.MovieComparator;
import com.elegidocodes.networkpagination.ui.adapter.MovieLoadStateAdapter;
import com.elegidocodes.networkpagination.viewmodel.MovieViewModel;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.disposables.Disposable;
import kotlin.Unit;

/**
 * Main UI screen that displays a grid of popular movies using Jetpack Paging 3 and RxJava.
 * It integrates with a {@link MovieViewModel} to load paginated data and supports:
 * - Swipe-to-refresh
 * - Load state management
 * - Error handling
 */
@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    // Glide RequestManager for loading images (injected or created manually)
    @Inject
    RequestManager requestManager;

    // View binding object for accessing layout views
    private ActivityMainBinding binding;

    // ViewModel providing paginated movie data
    private MovieViewModel mainViewModel;

    // Paging adapter for displaying movie items
    private MovieAdapter adapter;

    // UI elements
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private CircularProgressIndicator circularProgressIndicator;
    private TextView message;

    // Disposable to manage RxJava subscription
    private Disposable disposable;

    /**
     * Called when the activity is starting. Sets up the UI and subscriptions.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up view binding and ViewModel
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        // Create adapter with image loader and comparator
        adapter = new MovieAdapter(new MovieComparator(), requestManager);

        // Optionally attach a header for retrying failed loads
        adapter.withLoadStateHeader(new MovieLoadStateAdapter(v -> adapter.retry()));

        // Bind layout views to variables
        bindViews();

        // Set up RecyclerView with grid layout (2 columns)
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

        // Subscribe to ViewModel's paging data flow
        disposable = subscribe();

        // Handle swipe-to-refresh
        swipeRefreshLayout.setOnRefreshListener(() -> {
            adapter.refresh();
        });

        // Manage swipe indicator visibility based on adapter load state
        adapter.addLoadStateListener(loadStates -> {
            LoadState refreshState = loadStates.getRefresh();
            if (refreshState instanceof LoadState.NotLoading || refreshState instanceof LoadState.Error) {
                swipeRefreshLayout.setRefreshing(false);
            }
            return Unit.INSTANCE;
        });
    }

    /**
     * Binds the layout views using the data binding object.
     */
    private void bindViews() {
        swipeRefreshLayout = binding.swipeRefreshLayout;
        recyclerView = binding.recyclerView;
        circularProgressIndicator = binding.circularProgressIndicator;
        message = binding.message;
    }

    /**
     * Subscribes to the ViewModel's paging data stream and submits data to the adapter.
     *
     * @return the Disposable that can be used to cancel the subscription
     */
    private Disposable subscribe() {
        return mainViewModel.getPagingDataFlowable().subscribe(
                pagingData -> {
                    adapter.submitData(getLifecycle(), pagingData);
                    circularProgressIndicator.setVisibility(GONE);
                },
                throwable -> {
                    message.setText(throwable.getMessage());
                    message.setVisibility(View.VISIBLE);
                    circularProgressIndicator.setVisibility(GONE);
                }
        );
    }

    /**
     * Disposes the RxJava subscription when the activity is destroyed.
     * Prevents memory leaks.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        super.onDestroy();
    }
}
