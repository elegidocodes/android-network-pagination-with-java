package com.elegidocodes.networkpagination.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.LoadState;
import androidx.paging.LoadStateAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.elegidocodes.networkpagination.R;
import com.elegidocodes.networkpagination.databinding.LoadStateItemBinding;

/**
 * Adapter for displaying the loading and error state of items in a RecyclerView.
 * This adapter is used in conjunction with Paging to handle and display load states (loading, error, etc.).
 */
public class MovieLoadStateAdapter extends LoadStateAdapter<MovieLoadStateAdapter.ViewModel> {

    private final View.OnClickListener retry;

    /**
     * Constructor for the MovieLoadStateAdapter.
     *
     * @param retry The click listener that is triggered when the retry button is clicked.
     */
    public MovieLoadStateAdapter(View.OnClickListener retry) {
        this.retry = retry;
    }

    /**
     * Binds a {@link LoadState} to the ViewModel. This method is used to display
     * the loading, error, or idle states of the paging process.
     *
     * @param viewModel The ViewModel that holds the views for displaying the load state.
     * @param loadState The current load state (loading, error, or idle).
     */
    @Override
    public void onBindViewHolder(@NonNull ViewModel viewModel, @NonNull LoadState loadState) {
        viewModel.bind(loadState);
    }

    /**
     * Creates a new instance of the ViewModel that handles the load state item view.
     *
     * @param viewGroup The parent ViewGroup to attach the load state item view.
     * @param loadState The current load state (loading, error, or idle).
     * @return A new ViewModel for the load state item.
     */
    @NonNull
    @Override
    public ViewModel onCreateViewHolder(@NonNull ViewGroup viewGroup, @NonNull LoadState loadState) {
        return new ViewModel(viewGroup, retry);
    }

    /**
     * ViewModel for the load state item. It binds the load state (loading or error)
     * to the corresponding views and manages the visibility of loading indicators and error messages.
     */
    static class ViewModel extends RecyclerView.ViewHolder {

        private final LoadStateItemBinding binding;

        /**
         * Constructor for the ViewModel.
         *
         * @param parent The parent ViewGroup in which the load state item is created.
         * @param retry  The click listener for retrying the load operation.
         */
        public ViewModel(ViewGroup parent, View.OnClickListener retry) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.load_state_item, parent, false));
            this.binding = LoadStateItemBinding.bind(itemView);
            binding.btnRetry.setOnClickListener(retry);
        }

        /**
         * Binds the load state (loading, error, or idle) to the UI views.
         * It manages the visibility of the loading spinner, error message, and retry button.
         *
         * @param loadState The current load state.
         */
        public void bind(LoadState loadState) {
            if (loadState instanceof LoadState.Error) {
                LoadState.Error error = (LoadState.Error) loadState;
                binding.message.setText(error.getError().getMessage());
            }

            // Show the loading spinner while data is being loaded
            binding.circularProgressIndicator.setVisibility(loadState instanceof LoadState.Loading ? View.VISIBLE : View.GONE);

            // Show retry button and error message if there is an error
            binding.btnRetry.setVisibility(loadState instanceof LoadState.Error ? View.VISIBLE : View.GONE);
            binding.message.setVisibility(loadState instanceof LoadState.Error ? View.VISIBLE : View.GONE);
        }

    }

}
