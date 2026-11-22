package com.example.tp9;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText etTitle;
    private EditText etKeywords;
    private EditText etSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        dbHelper = new DatabaseHelper(this);

        etTitle = findViewById(R.id.etAddTitle);
        etKeywords = findViewById(R.id.etAddKeywords);
        etSteps = findViewById(R.id.etAddSteps);

        Button btnSave = findViewById(R.id.btnSaveRecipe);
        Button btnCancel = findViewById(R.id.btnCancelAdd);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRecipe();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveRecipe() {
        String title = etTitle.getText().toString().trim();
        String keywords = etKeywords.getText().toString().trim();
        String steps = etSteps.getText().toString().trim();

        // 1. Validate empty title
        if (title.isEmpty()) {
            etTitle.setError("Title cannot be empty");
            Toast.makeText(this, "Title cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Validate field maximum lengths
        if (title.length() > 30) {
            etTitle.setError("Title must not exceed 30 characters");
            Toast.makeText(this, "Title must be 30 characters or less!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (keywords.length() > 30) {
            etKeywords.setError("Keywords must not exceed 30 characters");
            Toast.makeText(this, "Keywords must be 30 characters or less!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (steps.length() > 300) {
            etSteps.setError("Steps must not exceed 300 characters");
            Toast.makeText(this, "Steps must be 300 characters or less!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Validate duplicate title
        if (dbHelper.checkTitleExists(title)) {
            etTitle.setError("Recipe title already exists");
            Toast.makeText(this, "A recipe with this title already exists!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 4. Insert into database using ContentValues
        boolean success = dbHelper.insertRecipe(title, steps, keywords);

        if (success) {
            Toast.makeText(this, "Recipe saved successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error saving recipe. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}