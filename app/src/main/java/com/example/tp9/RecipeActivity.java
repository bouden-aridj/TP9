package com.example.tp9;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        TextView tvTitle = findViewById(R.id.tvRecipeTitle);
        TextView tvKeywords = findViewById(R.id.tvRecipeKeywords);
        TextView tvSteps = findViewById(R.id.tvRecipeSteps);
        Button btnBack = findViewById(R.id.btnBackRecipeDetails);

        int recipeId = getIntent().getIntExtra("RECIPE_ID", -1);

        if (recipeId != -1) {
            Recipe recipe = dbHelper.getRecipeById(recipeId);
            if (recipe != null) {
                tvTitle.setText(recipe.getTitle());
                tvKeywords.setText("Keywords: " + recipe.getKeywords());
                tvSteps.setText(recipe.getSteps());
            } else {
                Toast.makeText(this, "Recipe not found", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Invalid Recipe ID", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}