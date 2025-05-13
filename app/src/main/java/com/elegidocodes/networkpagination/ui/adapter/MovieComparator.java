package com.elegidocodes.networkpagination.ui.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.elegidocodes.networkpagination.model.Movie;

import java.util.Objects;

/**
 * A comparator for comparing two {@link Movie} objects in a RecyclerView Adapter.
 * Used by {@link androidx.recyclerview.widget.ListAdapter} to determine whether items and their contents have changed.
 * This class is designed to optimize the performance of the RecyclerView by minimizing unnecessary updates.
 */
public class MovieComparator extends DiffUtil.ItemCallback<Movie> {

    /**
     * Compares whether two {@link Movie} items have the same unique identifier.
     * This method checks if the movie IDs are the same, which is a fast comparison to determine
     * if two items are essentially the same in the list.
     *
     * @param oldItem The old {@link Movie} item.
     * @param newItem The new {@link Movie} item.
     * @return {@code true} if the items represent the same movie, {@code false} otherwise.
     */
    @Override
    public boolean areItemsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
        // Use UNIQUE IDENTIFIER (ID) to check if the items are the same
        return oldItem.getId().equals(newItem.getId());
    }

    /**
     * Compares whether the contents of two {@link Movie} items are the same.
     * This method checks if the properties that affect the visual display of the movie have changed.
     * Only properties that are visually stable, such as the movie title and poster, are compared here.
     *
     * @param oldItem The old {@link Movie} item.
     * @param newItem The new {@link Movie} item.
     * @return {@code true} if the contents (like title and poster) are the same, {@code false} otherwise.
     */
    @Override
    public boolean areContentsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
        // Compare only VISUALLY STABLE properties such as title and poster
        return oldItem.getTitle().equals(newItem.getTitle()) &&
                Objects.equals(oldItem.getPosterPath(), newItem.getPosterPath());
    }

}
