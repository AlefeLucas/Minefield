package alefelucas.minesweeper.controller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import alefelucas.minesweeper.R;
import alefelucas.minesweeper.controller.adapter.MinesweeperAdapter;
import alefelucas.minesweeper.model.*;

/**
 * ============================================= FUNCIONAMENTO ============================================= *
 * *
 * Esta implementação de campo minado usa as seguintes estruturas de dados:
 * {@link Cell} - Classe que representa um quadrado do jogo.
 * {@link Minesweeper} - Classe que representa o jogo em sí, possuindo uma matriz de {@link Cell}, métodos
 * para revelar uma posição, obter status do jogo, obter largura/altura, obter rótulo de um quadrado pela
 * posição, dizer se um quadrado está revelado, etc.
 * {@link CellType} - Enumeração que define os tipos de célula.
 * {@link GameStatus} - Enumeração que define o status do jogo.
 * <p>
 * O campo minado é implementado com uma {@link RecyclerView}, cujo Adapter é o {@link MinesweeperAdapter}.
 * Cada classe possui sua própria documentação nela, com classes mais extensas possuindo documentação de
 * cada método. Os métodos estão refatorados. Os nomes de variáveis e métodos estão todos em inglês. As
 * string são escritas em inglês no strings.xml, possuindo tradução para o português (pt). Os ícones usados
 * são do Material Design. As cores usadas foram obtidas do exemplos no PDF da prova.
 * <p>
 * ========================================================================================================= *
 * <p>
 * Esta classe é a Activity com a tela do jogo
 */
public class MainActivity extends AppCompatActivity {

    private MenuItem visibilityMenu;

    private boolean isFieldVisible;

    private RecyclerView recyclerView;
    private static final int SPAN_COUNT = 10;

    private TextView statusTextView;
    private Button tryAgainButton;

    private Minesweeper minesweeper;
    private MinesweeperAdapter adapter;

    private static final int[] MINE_OPTIONS = {18, 15, 12};
    private static final int[] HEIGHT_OPTIONS = {10, 13, 16};
    private static final int DEFAULT_OPTION = 1;
    private int option;


    /**
     * Do ciclo de vida da Activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initViews();
        this.statusTextView.setVisibility(View.GONE);
        this.tryAgainButton.setVisibility(View.GONE);
        this.setUpRecyclerView();
        this.option = DEFAULT_OPTION;
        this.tryAgainButton.setOnClickListener(v -> restart());
    }

    /**
     * Método chamado pelo {@link #adapter} quando o jogo é vencido.
     * Exibe texto de vitória e o botão de jogar novamente.
     */
    public void won() {
        this.statusTextView.setText(R.string.you_win);
        this.statusTextView.setVisibility(View.VISIBLE);
        this.tryAgainButton.setVisibility(View.VISIBLE);

    }

    /**
     * Método chamado pelo {@link #adapter} quando o jogo é perdido.
     * Exibe texto de vitória e o botão de jogar novamente.
     */
    public void lost() {
        this.statusTextView.setText(R.string.you_lose);
        this.statusTextView.setVisibility(View.VISIBLE);
        this.tryAgainButton.setVisibility(View.VISIBLE);
    }

    /**
     * Reinicia o jogo, esconde o botão de jogar novamente e a mensagem de texto.
     * Remove a visibilidade de campo minado.
     */
    private void restart() {
        this.statusTextView.setVisibility(View.GONE);
        this.tryAgainButton.setVisibility(View.GONE);
        this.minesweeper = new Minesweeper(HEIGHT_OPTIONS[option], SPAN_COUNT, MINE_OPTIONS[option]);
        this.adapter.restart(minesweeper);
        if (this.isFieldVisible) {
            this.adapter.toggleVisibility();
            this.isFieldVisible = false;
        }
    }

    /**
     * Configura a {@link RecyclerView} e o jogo inicial.
     */
    private void setUpRecyclerView() {
        this.recyclerView.setLayoutManager(new GridLayoutManager(this, SPAN_COUNT, RecyclerView.VERTICAL, false) {

            /**
             * Solução para um defeito ainda não resolvido dos LayoutManagers.
             */
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                try {
                    super.onLayoutChildren(recycler, state);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        });
        this.minesweeper = new Minesweeper(HEIGHT_OPTIONS[DEFAULT_OPTION], SPAN_COUNT, MINE_OPTIONS[DEFAULT_OPTION]);
        this.adapter = new MinesweeperAdapter(this, minesweeper);
        this.recyclerView.setAdapter(adapter);
    }

    /**
     * Inicializa as views da activity.
     */
    private void initViews() {
        this.statusTextView = findViewById(R.id.status_text_view);
        this.tryAgainButton = findViewById(R.id.try_again_button);
        this.recyclerView = findViewById(R.id.field_recycler_view);
    }

    /**
     * Do ciclo de vida da Activity.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        this.visibilityMenu = menu.findItem(R.id.action_visibility);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Do ciclo de vida da Activity.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_visibility:
                this.onToggleVisibility();

                break;
            case R.id.action_refresh:
                restart();
                break;
            case R.id.action_size:
                String[] sizes = new String[HEIGHT_OPTIONS.length];
                for (int i = 0; i < HEIGHT_OPTIONS.length; i++) {
                    sizes[i] = String.format(Locale.getDefault(), "%dx%d (%d %s)", HEIGHT_OPTIONS[i], SPAN_COUNT, MINE_OPTIONS[i], getString(R.string.mines));
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.change_size)
                        .setItems(sizes, (dialog, which) -> {
                            this.option = which;
                            restart();
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Alterna a visibilidade - muda o ícone de visibilidade e alterna a exibição
     * das posições escondidas no jogo.
     */
    private void onToggleVisibility() {
        this.adapter.toggleVisibility();
        if (!this.isFieldVisible) {
            this.visibilityMenu.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_visibility_off_black_24dp));
            this.isFieldVisible = true;
        } else {
            this.visibilityMenu.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_visibility_black_24dp));
            this.isFieldVisible = false;
        }
    }


}
