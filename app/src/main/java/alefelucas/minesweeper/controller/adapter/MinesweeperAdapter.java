package alefelucas.minesweeper.controller.adapter;


import android.graphics.Color;

import android.util.DisplayMetrics;
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

/**
 * Classe adapter para a {@link RecyclerView} que exibe os quadrados do jogo.
 *
 * @author Álefe Lucas
 */
public class MinesweeperAdapter extends RecyclerView.Adapter<MinesweeperAdapter.MinesweeperViewHolder> {

    private static final int CELL = 1;
    private Minesweeper minesweeper;
    private MainActivity context;
    private boolean showing;

    /**
     * Constrói o objeto {@link MinesweeperAdapter} dada a referência da {@link MainActivity} e o jogo {@link Minesweeper}.
     */
    public MinesweeperAdapter(MainActivity context, Minesweeper minesweeper) {
        this.context = context;
        this.minesweeper = minesweeper;

    }

    /**
     * Infla o item com o layout correspondente ao viewType. Como neste adapter só existe um tipo de item,
     * é sempre retornado o {@link MinesweeperViewHolder}
     *
     * @param viewType tipo do item retornado pelo {@link #getItemViewType(int)}
     * @return o {@link androidx.recyclerview.widget.RecyclerView.ViewHolder} correspondente.
     */
    @NonNull
    @Override
    public MinesweeperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == CELL) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
            final float scale = context.getResources().getDisplayMetrics().density;
            int height = (int) (dpHeight * scale + 0.5f);
            int width = (int) (dpWidth * scale + 0.5f);
            return new MinesweeperViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_item, parent, false), height, width);
        }
        throw new IllegalStateException();
    }

    /**
     * Retorna o tipo de item dada a posição. Para este adapter, o único tipo é {@link #CELL}.
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

    /**
     * Do ciclo de vida do adapter. Configura o quadrado de acordo com seu status de
     * revelado e rótulo exibido. Define a ação para o clique/toque no quadrado.
     */
    @Override
    public void onBindViewHolder(@NonNull MinesweeperViewHolder holder, int position) {
        int y = position / this.minesweeper.getWidth();
        int x = position % this.minesweeper.getWidth();

        if (showing || this.minesweeper.isRevealed(x, y)) {
            holder.cellButton.setBackgroundColor(ContextCompat.getColor(context, R.color.open_field));
            char cellLabel = this.minesweeper.getCellLabel(x, y);
            setLabelColor(holder.cellButton, cellLabel);
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

                if (!this.minesweeper.isRevealed(i, j)) {

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

    /**
     * Define a cor do rótulo dos quadrados.
     */
    private void setLabelColor(Button cellButton, char cellLabel) {
        int[] colorIds = {R.color.one, R.color.two, R.color.three, R.color.four, R.color.five, R.color.six, R.color.seven, R.color.eight};
        if (cellLabel == 'N' || cellLabel == '*') {
            cellButton.setTextColor(Color.BLACK);
        } else if (cellLabel != ' ') {
            cellButton.setTextColor(ContextCompat.getColor(context, colorIds[cellLabel - '1']));
        }
    }

    /**
     * Reinicia o jogo com um novo campo minado.
     */
    public void restart(Minesweeper minesweeper) {
        this.minesweeper = minesweeper;
        this.notifyDataSetChanged();
    }

    /**
     * Obtém o número de ítens na {@link RecyclerView}
     */
    @Override
    public int getItemCount() {
        return this.minesweeper.getHeight() * this.minesweeper.getWidth();
    }


    /**
     * ViewHolder - armazena as referências para as views de um ítem.
     */
    class MinesweeperViewHolder extends RecyclerView.ViewHolder {

        Button cellButton;

        MinesweeperViewHolder(@NonNull View itemView, float height, float width) {
            super(itemView);

            cellButton = itemView.findViewById(R.id.cell_button);

            cellButton.getLayoutParams().width = (int) (width / 13);
            cellButton.getLayoutParams().height = (int) (width / 13);

        }
    }


}
