package mealplanner.DB;

import mealplanner.DB.DbClient;
import mealplanner.Meal;

import java.util.List;
import java.util.Map;

public class DbMeals {
    private static final String createDB ="create table if not exists meals "+
            "(category varchar(255),"+
            "meal varchar(255),"+
            "meal_id integer primary key);";

    private static final String insertMeal = "insert into meals (category, meal, meal_id) " +
            "values ('%s', '%s', %d);";

    private static final String selectMealsByCategoryWithIngredients = "select * from meals where category = '%s';";

    private static final String selectMealsByCategory = "select meal, meal_id from meals where category = '%s' order by meal;";



    private DbClient dbClient;
    public DbMeals(DbClient dbClient) {
        this.dbClient = dbClient;
        dbClient.run(createDB);
    }
    public void terminate() {
        dbClient.terminate();
    }

    public void insertMeal(Meal meal) {
        String query = String.format(insertMeal, meal.getCategory(), meal.getName(),meal.getId());
        dbClient.run(query);
    }

    public List<Meal> selectMealsByCategoryWithIngredients(String category){
        return dbClient.selectMealsByCategoryWithIngredients(String.format(selectMealsByCategoryWithIngredients, category));
    }

    public Map<String,Integer> selectMealsByCategory(String category){
        return dbClient.selectMealsByCategory(String.format(selectMealsByCategory, category));
    }

}
