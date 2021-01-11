package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText etItem2;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etItem2 = findViewById(R.id.etItem2);
        btnSave = findViewById(R.id.btnSave);

        getSupportActionBar().setTitle("Update Item");

        etItem2.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));

        // When user is done editing, they click the 'save' button.
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. Create intent which will contain results (of modification).
                Intent intent = new Intent();

                // 2. Pass the data (results of editing).
                intent.putExtra(MainActivity.KEY_ITEM_TEXT, etItem2.getText().toString());
                intent.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));

                // 3. Set result of intent.
                setResult(RESULT_OK, intent);

                // 4. Finish activity, close screen, and return.
                finish();
            }
        });


    }
}