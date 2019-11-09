package alefelucas.minesweeper.controller.adapter;


import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import alefelucas.minesweeper.R;
import alefelucas.minesweeper.controller.activity.MainActivity;
import alefelucas.minesweeper.model.Minesweeper;

import static alefelucas.minesweeper.model.GameStatus.PLAYING;

public class MinesweeperAdapter extends RecyclerView.Adapter<MinesweeperAdapter.MinefieldViewHolder> {

    private static final int CELL = 1;
    private Minesweeper minesweeper;
    private MainActivity context;
    private boolean showing;

    public MinesweeperAdapter(MainActivity context, Minesweeper minesweeper) {
        this.context = context;
        this.minesweeper = minesweeper;
    }

    /**
     * Inflates the item with the layout corresponding to the viewType.
     *
     * @param viewType type of the item as returned by {@link #getItemViewType(int)}
     * @return the corresponding {@link MinesweeperAdapter}
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

    public void toggleVisibility() {
        this.showing = !this.showing;
        for (int i = 0; i < this.minesweeper.getHeight(); i++) {
            for (int j = 0; j < this.minesweeper.getWidth(); j++) {
                if (!this.minesweeper.isRevealed(j, i)) {
                    int position = i * this.minesweeper.getWidth() + j;
                    this.notifyItemChanged(position);
                }
            }
        }


    }

    @Override
    public void onBindViewHolder(@NonNull MinefieldViewHolder holder, int position) {
        int y = position / this.minesweeper.getWidth();
        int x = position % this.minesweeper.getWidth();

        if (showing || this.minesweeper.isRevealed(x, y)) {
            holder.cellButton.setBackgroundColor(ContextCompat.getColor(context, R.color.open_field));

            char cellLabel = this.minesweeper.getCellLabel(x, y);
            switch (cellLabel) {
                case '1':
                    holder.cellButton.setTextColor(ContextCompat.getColor(context, R.color.one));
                    break;
                case '2':
                    holder.cellButton.setTextColor(ContextCompat.getColor(context, R.color.two));
                    break;
                case '3':
                    holder.cellButton.setTextColor(ContextCompat.getColor(context, R.color.three));
                    break;
                case '4':
                    holder.cellButton.setTextColor(ContextCompat.getColor(context, R.color.four));
                    break;
                case '5':
                    holder.cellButton.setTextColor(ContextCompat.getColor(context, R.color.five));
                    break;
                case '6':
                    holder.cellButton.setTextColor(ContextCompat.getColor(context, R.color.six));
                    break;
                case '7':
                    holder.cellButton.setTextColor(ContextCompat.getColor(context, R.color.seven));
                    break;
                case '8':
                    holder.cellButton.setTextColor(ContextCompat.getColor(context, R.color.eight));
                    break;
                case 'N':
                    holder.cellButton.setTextColor(Color.BLACK);
                    break;
            }
            holder.cellButton.setText(Character.toString(cellLabel));
        } else {
            holder.cellButton.setText("");
            holder.cellButton.setBackgroundColor(ContextCompat.getColor(context, R.color.field));
        }

        holder.cellButton.setOnClickListener(v -> {
            if (this.minesweeper.getStatus() == PLAYING && !this.showing) {
                int adapterPosition = holder.getAdapterPosition();
                int j = adapterPosition / this.minesweeper.getWidth();
                int i = adapterPosition % this.minesweeper.getWidth();

                if(!this.minesweeper.isRevealed(i, j)) {

                    ArrayList<Integer> reveal = this.minesweeper.reveal(i, j);
                    for (Integer pos : reveal) {
                        this.notifyItemChanged(pos);
                    }
                    switch (this.minesweeper.getStatus()) {
                        case WON:
                            this.context.won();
                            break;
                        case LOST:
                            this.context.lost();
                            break;
                    }
                }
            }
        });
    }

    public void restart(Minesweeper minesweeper) {
        this.minesweeper = minesweeper;
        this.notifyDataSetChanged();
    }

    /**
     * Gets the number of items in a RecyclerView
     *
     * @return item quantity
     */
    @Override
    public int getItemCount() {
        return this.minesweeper.getHeight() * this.minesweeper.getWidth();
    }


    /**
     * ViewHolder - stores the references to the views of an item
     */
    class MinefieldViewHolder extends RecyclerView.ViewHolder {

        public Button cellButton;

        MinefieldViewHolder(@NonNull View itemView) {
            super(itemView);

            cellButton = itemView.findViewById(R.id.cell_button);
        }
    }


}
