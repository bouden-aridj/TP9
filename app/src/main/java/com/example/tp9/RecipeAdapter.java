package com.example.tp9;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class RecipeAdapter extends ArrayAdapter<Recipe> {

    public RecipeAdapter(@NonNull Context context, @NonNull List<Recipe> recipes) {
        super(context, 0, recipes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.recipe_item, parent, false);
        }

        Recipe recipe = getItem(position);

        TextView tvTitle = convertView.findViewById(R.id.tvItemTitle);
        TextView tvKeywords = convertView.findViewById(R.id.tvItemKeywords);

        if (recipe != null) {
            tvTitle.setText(recipe.getTitle());
            tvKeywords.setText("Keywords: " + recipe.getKeywords());
        }

        return convertView;
    }
}