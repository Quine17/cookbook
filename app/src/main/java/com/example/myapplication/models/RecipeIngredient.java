package com.example.myapplication.models;

public class RecipeIngredient {
    private int id;
    private int recipeId;
    private int ingredientId;
    private String quantity;
    private String unit;
    private String ingredientName;

    public RecipeIngredient() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getRecipeId() { return recipeId; }
    public void setRecipeId(int recipeId) { this.recipeId = recipeId; }

    public int getIngredientId() { return ingredientId; }
    public void setIngredientId(int ingredientId) { this.ingredientId = ingredientId; }

    public String getQuantity() { return quantity; }
    public void setQuantity(String quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public String getIngredientName() { return ingredientName; }
    public void setIngredientName(String ingredientName) { this.ingredientName = ingredientName; }
}
