package mealplanner.DB;

import mealplanner.Meal;
import mealplanner.Weekdays;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DbClient {

    private static final String DB_URL="jdbc:postgresql://localhost:5432/meals_db";
    private static final String DB_USER = "postgres";
    private static final String PASS ="1111";
    private Connection connection;
    private Statement statement;

    public DbClient() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, PASS);
            connection.setAutoCommit(true);
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void run(String query){
        try {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Meal> selectMealsByCategoryWithIngredients(String query) {
        try {
            ResultSet resultSet =statement.executeQuery(query);
            List<Meal> meals = new ArrayList<>();
            while(resultSet.next()) {
                String category = resultSet.getString("category");
                String name = resultSet.getString("meal");
                int id = resultSet.getInt("meal_id");
                List<String> ingredients = selectIngredientsForMeal(String.format(DbIngredients.selectIngredientsForMeal, id));
                Meal meal = new Meal(name, category, ingredients);
                meals.add(meal);
            }
            return meals;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, Integer> selectMealsByCategory(String query){
        try{
            ResultSet resultSet =statement.executeQuery(query);
            Map<String,Integer> meals = new LinkedHashMap<>();
            while(resultSet.next()) {
                String name = resultSet.getString("meal");
                int id = resultSet.getInt("meal_id");
                meals.put(name,id);
            }
            return meals;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public List<String> selectIngredientsForMeal(String query) {
        try(Statement ingredientStatement = connection.createStatement()){
            ResultSet resultSet =ingredientStatement.executeQuery(query);
            List<String> ingredients = new ArrayList<>();
            while(resultSet.next()) {
                String ingredient = resultSet.getString("ingredient");
                ingredients.add(ingredient);
            }
            return ingredients;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void terminate() {
        try {
            connection.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void selectPlan(String selectPlan) {
        try {
            for(Weekdays weekday : Weekdays.values()) {
                ResultSet resultSet = statement.executeQuery(String.format(selectPlan, weekday.getName()));
                System.out.println(weekday.getName());
                while (resultSet.next()) {
                    String meal = resultSet.getString("meal_option");
                    String mealCategory = resultSet.getString("meal_category");
                    switch(mealCategory){
                        case "breakfast":
                            System.out.println("Breakfast: "+meal);
                            break;
                        case "lunch":
                            System.out.println("Lunch: "+meal);
                            break;
                        case "dinner":
                            System.out.println("Dinner: "+meal);
                            break;
                    }
                    System.out.println();
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isEmpty(String selectPlanAll){
        try{
            ResultSet resultSet = statement.executeQuery(selectPlanAll);
            return !resultSet.next();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public Map<String, Integer> getAllIngredients(String selectPlanAll) {
        try{
            ResultSet resultSet = statement.executeQuery(selectPlanAll);
            Map<String,Integer> ingredients = new LinkedHashMap<>();
            while(resultSet.next()) {
                int id = resultSet.getInt("meal_id");
                List<String> ingredientsForMeal = selectIngredientsForMeal
                        (String.format(DbIngredients.selectIngredientsForMeal, id));
                for(String ingredient : ingredientsForMeal){
                    if(ingredients.containsKey(ingredient.toLowerCase())){
                        ingredients.put(ingredient.toLowerCase(),ingredients.get(ingredient)+1);
                    } else {
                        ingredients.put(ingredient.toLowerCase(),1);
                    }
                }

            }
            return ingredients;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
