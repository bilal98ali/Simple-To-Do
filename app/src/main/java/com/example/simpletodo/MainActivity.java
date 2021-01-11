package com.example.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 1;

    List<String> items;

    // The variables are for each view (found in activity_main.xml).
    // They serve as a reference/handle in this class (MainActivity.java) ...
    // in order for logic to be implemented (i.e. 'Add' button will add item to list when tapped).

    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Define what each of the member variables are.

        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);

        loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener()  {
            @Override
            public void onItemLongClicked(int position) {
                // 1. Delete item from model.
                items.remove(position);
                // 2. Notify adapter.
                itemsAdapter.notifyItemRemoved(position);
                // 3. Feedback for item removal.
                //   (NOTE: No feedback being displayed when running app via emulator!)
                Toast.makeText(getApplicationContext(), "Item removed.", Toast.LENGTH_SHORT).show();
            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity", "Single click at position " + position);
                // 1. Create new activity (first parameter is context/current instance; second is class of activity to launch!).
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                // 2. Pass data being edited.
                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);
                // Display activity.
                startActivityForResult(i, EDIT_TEXT_CODE);

            }
        };

        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        // Adding logic on button-press.
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = etItem.getText().toString();
                // 1. Add item to the model.
                items.add(todoItem);
                // 2. Notify adapter that item is inserted.
                itemsAdapter.notifyItemInserted(items.size()-1);
                // 3. Clear 'etItem' once submitted.
                etItem.setText("");
                // 4. Feedback after item has been added successfully.
                //   (NOTE: No feedback being displayed when running app via emulator!)
                Toast.makeText(getApplicationContext(), "Item added!", Toast.LENGTH_SHORT).show();
                // 5. Save items.
                saveItems();
            }
        });
    }

    // Handles the result of the 'EditActivity'.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            // Retrieve updated text value.
            String itemText = data.getStringExtra((KEY_ITEM_TEXT));
            // Extract the original position of the edited item from the position key.
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);
            // Updater model at right position with new item text.
            items.set(position, itemText);
            // Notify the adapter.
            itemsAdapter.notifyItemChanged(position);
            // Persist changes.
            saveItems();
            Toast.makeText(getApplicationContext(), "Item updated!", Toast.LENGTH_SHORT).show();
        } else {
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }

    private File getDataFile()  {
        return new File(getFilesDir(), "data.text");
    }

    // This function will load items by reading every line of data file.
    private void loadItems()    {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items.", e);
            items = new ArrayList<>();
        }
    }
    // This function saves items by writing them into the data file.
    private void saveItems()    {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity","Error writing items.", e);
        }
    }

}