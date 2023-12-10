package mealplanner;

import java.util.List;

public class Meal {
    private static int id = 0;
    private String name;
    private String category;
    private List<String> ingredients;


    public Meal(String name, String category, List<String> ingredients) {
        ++id;
        this.name = name;
        this.category = category;
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }
    public List<String> getIngredients() {
        return ingredients;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(
                "Name: " + name + "\n" +
                "Ingredients:\n");
        for (String ingredient : ingredients) {
            sb.append(ingredient + "\n");
        }
        return sb.toString();
    }
    public int getId() {
        return id;
    }
}
