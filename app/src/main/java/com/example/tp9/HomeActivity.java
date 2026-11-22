package com.example.tp9;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView lvHomeRecipes;
    private TextView tvEmptyHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dbHelper = new DatabaseHelper(this);

        lvHomeRecipes = findViewById(R.id.lvHomeRecipes);
        tvEmptyHome = findViewById(R.id.tvEmptyHome);

        Button btnAdd = findViewById(R.id.btnAddRecipe);
        Button btnSearch = findViewById(R.id.btnSearchRecipe);
        Button btnDelete = findViewById(R.id.btnDeleteRecipe);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, DeleteActivity.class);
                startActivity(intent);
            }
        });

        lvHomeRecipes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recipe selectedRecipe = (Recipe) parent.getItemAtPosition(position);
                if (selectedRecipe != null) {
                    Intent intent = new Intent(HomeActivity.this, RecipeActivity.class);
                    intent.putExtra("RECIPE_ID", selectedRecipe.getId());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRecipes();
    }

    private void loadRecipes() {
        List<Recipe> recipes = dbHelper.getAllRecipes();
        if (recipes.isEmpty()) {
            tvEmptyHome.setVisibility(View.VISIBLE);
            lvHomeRecipes.setVisibility(View.GONE);
        } else {
            tvEmptyHome.setVisibility(View.GONE);
            lvHomeRecipes.setVisibility(View.VISIBLE);
            RecipeAdapter adapter = new RecipeAdapter(this, recipes);
            lvHomeRecipes.setAdapter(adapter);
        }
    }
}