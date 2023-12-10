package mealplanner.DB;

import mealplanner.DB.DbClient;
import mealplanner.Meal;

public class DbIngredients {
    private static final String createDB ="create table if not exists ingredients "+
            "(ingredient varchar(255) not null,"+
            "ingredient_id integer,"+
            "meal_id integer,"+
            "foreign key (meal_id) references meals (meal_id));";

    private static final String insertIngredient = "insert into ingredients (ingredient,ingredient_id, meal_id) " +
            "values ('%s',%d, %d);";
    public static final String selectIngredientsForMeal = "select ingredient from ingredients where meal_id = %d;";

    private static int id = 1;

    private DbClient dbClient;
    public DbIngredients(DbClient dbClient) {
        this.dbClient = dbClient;
        dbClient.run(createDB);
    }
    public void terminate() {
        dbClient.terminate();
    }
    public void add(Meal meal) {
        for (String ingredient : meal.getIngredients()) {
            dbClient.run(String.format(insertIngredient, ingredient,id++,  meal.getId()));
        }
    }
}
