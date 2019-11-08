package alefelucas.minefield.controller.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import alefelucas.minefield.R;

public class MinefieldAdapter extends RecyclerView.Adapter<MinefieldAdapter.MinefieldViewHolder>  {

    private static final int CELL = 1;


    /**
     * Inflates the item with the layout corresponding to the viewType.
     *
     * @param viewType type of the item as returned by {@link #getItemViewType(int)}
     * @return the corresponding {@link MinefieldAdapter}
     */
    @NonNull
    @Override
    public MinefieldViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == CELL) {
            return new MinefieldViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_item, parent, false));
        }
        throw new IllegalStateException();
    }

    /**
     * Retuns the item type in the given position. For this adapter the only type is {@link #CELL}.
     *
     * @return item type
     */
    @Override
    public int getItemViewType(int position) {
        return CELL;
    }

    @Override
    public void onBindViewHolder(@NonNull MinefieldViewHolder holder, int position) {

    }

    /**
     * Gets the number of items in a RecyclerView
     *
     * @return item quantity
     */
    @Override
    public int getItemCount() {
        //TODO set data structure
        return -1;
    }

    /**
     * ViewHolder - stores the references to the views of an item
     */
    class MinefieldViewHolder extends RecyclerView.ViewHolder {
        MinefieldViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
