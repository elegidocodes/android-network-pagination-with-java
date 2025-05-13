package com.elegidocodes.networkpagination.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.elegidocodes.networkpagination.R;
import com.elegidocodes.networkpagination.databinding.MovieItemBinding;
import com.elegidocodes.networkpagination.model.Movie;

/**
 * Adapter for displaying a list of {@link Movie} items in a {@link RecyclerView}.
 * This adapter is used in conjunction with Paging to load data efficiently in a RecyclerView.
 * It supports pagination and uses Glide for image loading.
 */
public class MovieAdapter extends PagingDataAdapter<Movie, MovieAdapter.ViewHolder> {

    /**
     * Constants representing different view types for the RecyclerView.
     * Used to distinguish between movie items and the loading item.
     */
    public static final int LOADING_ITEM = 0;
    public static final int MOVIE_ITEM = 1;

    private final RequestManager requestManager;

    /**
     * Constructor for the MovieAdapter.
     *
     * @param diffCallback   DiffUtil callback to handle comparisons of {@link Movie} objects.
     * @param requestManager Glide request manager used to load images.
     */
    public MovieAdapter(@NonNull DiffUtil.ItemCallback<Movie> diffCallback, RequestManager requestManager) {
        super(diffCallback);
        this.requestManager = requestManager;
    }

    /**
     * Creates a new ViewHolder for a movie item.
     * This method inflates the {@link MovieItemBinding} layout and binds it to a ViewHolder.
     *
     * @param parent   The parent ViewGroup where the new item view will be added.
     * @param viewType The view type of the item to create.
     * @return A new instance of {@link ViewHolder}.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        MovieItemBinding movieItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.movie_item, parent, false);
        return new ViewHolder(movieItemBinding);
    }

    /**
     * Binds a {@link Movie} object to the specified ViewHolder.
     * It also loads the movie poster using Glide into the ImageView.
     *
     * @param holder   The ViewHolder to bind data to.
     * @param position The position of the movie in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = getItem(position);
        if (movie != null) {
            holder.bind(movie, requestManager);
        }
    }

    /**
     * Determines the view type of the item at the specified position.
     *
     * @param position The position of the item.
     * @return {@link #LOADING_ITEM} if it's a loading item, otherwise {@link #MOVIE_ITEM}.
     */
    @Override
    public int getItemViewType(int position) {
        return position == getItemCount() ? LOADING_ITEM : MOVIE_ITEM;
    }

    /**
     * ViewHolder for movie items. Binds a {@link Movie} object to its associated view.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        private final MovieItemBinding binding;
        private final ImageView poster;

        /**
         * Constructor for the ViewHolder.
         *
         * @param binding The binding object for the movie item layout.
         */
        public ViewHolder(MovieItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.poster = binding.poster;
        }

        /**
         * Binds a {@link Movie} object to the view, including setting the movie's title
         * and loading its poster image into the ImageView using Glide.
         *
         * @param movie          The {@link Movie} object to bind to the view.
         * @param requestManager The Glide request manager used to load images.
         */
        private void bind(Movie movie, RequestManager requestManager) {
            binding.setMovie(movie);
            // Load the movie poster image into the ImageView using Glide.
            requestManager.load("https://image.tmdb.org/t/p/w500" + movie.getPosterPath())
                    .into(poster);
        }

    }

}
