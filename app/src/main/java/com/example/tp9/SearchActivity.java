package com.example.tp9;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText etSearchInput;
    private ListView lvSearchResults;
    private TextView tvEmptySearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        dbHelper = new DatabaseHelper(this);

        etSearchInput = findViewById(R.id.etSearchInput);
        lvSearchResults = findViewById(R.id.lvSearchResults);
        tvEmptySearch = findViewById(R.id.tvEmptySearch);

        Button btnSearch = findViewById(R.id.btnPerformSearch);
        Button btnBack = findViewById(R.id.btnBackSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lvSearchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recipe selectedRecipe = (Recipe) parent.getItemAtPosition(position);
                if (selectedRecipe != null) {
                    Intent intent = new Intent(SearchActivity.this, RecipeActivity.class);
                    intent.putExtra("RECIPE_ID", selectedRecipe.getId());
                    startActivity(intent);
                }
            }
        });

        // Show all recipes initially
        performSearch();
    }

    private void performSearch() {
        String query = etSearchInput.getText().toString().trim();
        List<Recipe> results;

        if (query.isEmpty()) {
            results = dbHelper.getAllRecipes();
        } else {
            results = dbHelper.searchRecipes(query);
        }

        if (results.isEmpty()) {
            tvEmptySearch.setText("No recipes found matching: \"" + query + "\"");
            tvEmptySearch.setVisibility(View.VISIBLE);
            lvSearchResults.setVisibility(View.GONE);
        } else {
            tvEmptySearch.setVisibility(View.GONE);
            lvSearchResults.setVisibility(View.VISIBLE);
            RecipeAdapter adapter = new RecipeAdapter(this, results);
            lvSearchResults.setAdapter(adapter);
        }
    }
}