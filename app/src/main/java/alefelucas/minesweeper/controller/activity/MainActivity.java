package alefelucas.minesweeper.controller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import alefelucas.minesweeper.R;
import alefelucas.minesweeper.controller.adapter.MinesweeperAdapter;
import alefelucas.minesweeper.model.Minesweeper;

/**
 * The game screen
 */
public class MainActivity extends AppCompatActivity {

    private MenuItem visibilityMenu;
    private MenuItem refreshMenu;
    private MenuItem changeSizeMenu;

    private boolean isFieldVisible;

    private RecyclerView recyclerView;
    private static final int SPAN_COUNT = 10;

    private TextView statusTextView;
    private Button tryAgainButton;

    private Minesweeper minesweeper;
    private MinesweeperAdapter adapter;

    private static final int[] HEIGHT_OPTIONS = {10, 13, 16};
    private static final int DEFAULT_HEIGHT_OPTION = 1;
    private int heightOption;


    /**
     * From activity's lifecycle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initViews();
        this.statusTextView.setVisibility(View.INVISIBLE);
        this.tryAgainButton.setVisibility(View.INVISIBLE);
        this.setUpRecyclerView();
        this.heightOption = DEFAULT_HEIGHT_OPTION;
        this.tryAgainButton.setOnClickListener(v -> restart());
    }

    public void won() {
        this.statusTextView.setText(R.string.you_win);
        this.statusTextView.setVisibility(View.VISIBLE);
        this.tryAgainButton.setVisibility(View.VISIBLE);

    }

    public void lost() {
        this.statusTextView.setText(R.string.you_lose);
        this.statusTextView.setVisibility(View.VISIBLE);
        this.tryAgainButton.setVisibility(View.VISIBLE);
    }

    private void restart() {
        this.statusTextView.setVisibility(View.INVISIBLE);
        this.tryAgainButton.setVisibility(View.INVISIBLE);
        this.minesweeper = new Minesweeper(HEIGHT_OPTIONS[heightOption], SPAN_COUNT);
        this.adapter.restart(minesweeper);
        if(this.isFieldVisible){
            this.adapter.toggleVisibility();
        }
    }

    /**
     * Sets the layout manager and adapter for the recycler view
     */
    private void setUpRecyclerView() {
        this.recyclerView.setLayoutManager(new GridLayoutManager(this, SPAN_COUNT, RecyclerView.VERTICAL, false) {
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                try {
                    super.onLayoutChildren(recycler, state);
                } catch (IndexOutOfBoundsException e) {
                    Log.e("TAG", "meet a IOOBE in RecyclerView");
                }
            }
        });
        this.minesweeper = new Minesweeper(HEIGHT_OPTIONS[DEFAULT_HEIGHT_OPTION], SPAN_COUNT);
        this.adapter = new MinesweeperAdapter(this, minesweeper);
        this.recyclerView.setAdapter(adapter);
    }

    /**
     * Initializes the views of the activity
     */
    private void initViews() {
        this.statusTextView = findViewById(R.id.status_text_view);
        this.tryAgainButton = findViewById(R.id.try_again_button);
        this.recyclerView = findViewById(R.id.field_recycler_view);
    }

    /**
     * From activity's lifecycle
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        this.visibilityMenu = menu.findItem(R.id.action_visibility);
        this.refreshMenu = menu.findItem(R.id.action_refresh);
        this.changeSizeMenu = menu.findItem(R.id.action_size);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * From activity's lifecycle
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
                    sizes[i] = String.format(Locale.getDefault(), "%dx%d", HEIGHT_OPTIONS[i], SPAN_COUNT);
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.change_size)
                        .setItems(sizes, (dialog, which) -> {
                            this.heightOption = which;
                            restart();
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Toggles the visibility on/off - changes the visibility icon and displays
     * the hidden positions in the game.
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
