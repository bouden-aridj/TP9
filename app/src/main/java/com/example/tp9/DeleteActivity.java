package com.example.tp9;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class DeleteActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText etDeleteTitleInput;
    private ListView lvDeleteRecipes;
    private TextView tvEmptyDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        dbHelper = new DatabaseHelper(this);

        etDeleteTitleInput = findViewById(R.id.etDeleteTitleInput);
        lvDeleteRecipes = findViewById(R.id.lvDeleteRecipes);
        tvEmptyDelete = findViewById(R.id.tvEmptyDelete);

        Button btnDeleteByTitle = findViewById(R.id.btnDeleteByTitle);
        Button btnBack = findViewById(R.id.btnBackDelete);

        btnDeleteByTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteByTitle();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lvDeleteRecipes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recipe recipe = (Recipe) parent.getItemAtPosition(position);
                if (recipe != null) {
                    confirmAndDelete(recipe);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDeleteRecipes();
    }

    private void deleteByTitle() {
        String title = etDeleteTitleInput.getText().toString().trim();
        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a recipe title to delete.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!dbHelper.checkTitleExists(title)) {
            Toast.makeText(this, "No recipe found with title: \"" + title + "\"", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean deleted = dbHelper.deleteRecipeByTitle(title);
        if (deleted) {
            Toast.makeText(this, "Recipe \"" + title + "\" deleted successfully!", Toast.LENGTH_SHORT).show();
            etDeleteTitleInput.setText("");
            loadDeleteRecipes();
        } else {
            Toast.makeText(this, "Failed to delete recipe.", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmAndDelete(Recipe recipe) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete the recipe \"" + recipe.getTitle() + "\"?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean deleted = dbHelper.deleteRecipe(recipe.getId());
                        if (deleted) {
                            Toast.makeText(DeleteActivity.this, "Recipe deleted successfully!", Toast.LENGTH_SHORT).show();
                            loadDeleteRecipes();
                        } else {
                            Toast.makeText(DeleteActivity.this, "Failed to delete recipe.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void loadDeleteRecipes() {
        List<Recipe> recipes = dbHelper.getAllRecipes();
        if (recipes.isEmpty()) {
            tvEmptyDelete.setVisibility(View.VISIBLE);
            lvDeleteRecipes.setVisibility(View.GONE);
        } else {
            tvEmptyDelete.setVisibility(View.GONE);
            lvDeleteRecipes.setVisibility(View.VISIBLE);
            RecipeAdapter adapter = new RecipeAdapter(this, recipes);
            lvDeleteRecipes.setAdapter(adapter);
        }
    }
}