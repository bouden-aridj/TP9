package com.example.tp9;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "RecipeBook.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "recipes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_STEPS = "steps";
    public static final String COLUMN_KEYWORDS = "keywords";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TITLE + " VARCHAR(30), "
                + COLUMN_STEPS + " VARCHAR(300), "
                + COLUMN_KEYWORDS + " VARCHAR(30));";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Insert new recipe using ContentValues
    public boolean insertRecipe(String title, String steps, String keywords) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title.trim());
        values.put(COLUMN_STEPS, steps.trim());
        values.put(COLUMN_KEYWORDS, keywords.trim());

        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    // Check if duplicate recipe title exists (case-insensitive) using parameterized query with ? placeholder
    public boolean checkTitleExists(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_ID + " FROM " + TABLE_NAME + " WHERE LOWER(" + COLUMN_TITLE + ") = LOWER(?)";
        Cursor cursor = db.rawQuery(query, new String[]{title.trim()});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    // Get all saved recipes
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " DESC";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
                String steps = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STEPS));
                String keywords = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KEYWORDS));
                recipeList.add(new Recipe(id, title, steps, keywords));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return recipeList;
    }

    // Search recipes by title or keywords using parameterized query with ? placeholders
    public List<Recipe> searchRecipes(String queryText) {
        List<Recipe> recipeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String searchPattern = "%" + queryText.trim() + "%";
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_TITLE + " LIKE ? OR " + COLUMN_KEYWORDS + " LIKE ? ORDER BY " + COLUMN_ID + " DESC";
        Cursor cursor = db.rawQuery(query, new String[]{searchPattern, searchPattern});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
                String steps = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STEPS));
                String keywords = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KEYWORDS));
                recipeList.add(new Recipe(id, title, steps, keywords));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return recipeList;
    }

    // Get single recipe by ID
    public Recipe getRecipeById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});
        Recipe recipe = null;
        if (cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
            String steps = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STEPS));
            String keywords = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KEYWORDS));
            recipe = new Recipe(id, title, steps, keywords);
        }
        cursor.close();
        return recipe;
    }

    // Delete recipe by ID
    public boolean deleteRecipe(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        return rowsDeleted > 0;
    }

    // Delete recipe by Title
    public boolean deleteRecipeByTitle(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_NAME, "LOWER(" + COLUMN_TITLE + ") = LOWER(?)", new String[]{title.trim()});
        return rowsDeleted > 0;
    }
}