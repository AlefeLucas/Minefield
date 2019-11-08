package alefelucas.minefield.controller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import alefelucas.minefield.R;

/**
 * The game screen
 */
public class MainActivity extends AppCompatActivity {

    private MenuItem visibilityMenu;
    private MenuItem refreshMenu;

    private boolean isFieldVisible;

    private RecyclerView recyclerView;
    private static final int SPAN_COUNT = 10;

    private TextView statusTextView;
    private Button tryAgainButton;

    /**
     * From activity's lifecycle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initViews();
        this.setUpRecyclerView();
    }

    /**
     * Sets the layout manager and adapter for the recycler view
     */
    private void setUpRecyclerView() {
        this.recyclerView.setLayoutManager(new GridLayoutManager(this, SPAN_COUNT));
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

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *  Toggles the visibility on/off - changes the visibility icon and displays
     *  the hidden positions in the game.
     */
    private void onToggleVisibility() {
        if(!this.isFieldVisible) {
            this.visibilityMenu.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_visibility_off_black_24dp));
            this.isFieldVisible = true;
        } else {
            this.visibilityMenu.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_visibility_black_24dp));
            this.isFieldVisible = false;
        }
    }
}
