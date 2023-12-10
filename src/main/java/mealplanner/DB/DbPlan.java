package mealplanner.DB;

import mealplanner.DB.DbClient;
import mealplanner.Weekdays;

import java.util.Map;

public class DbPlan {

    private static final String createDB = "create table if not exists plan(" +
            "weekday varchar(50) not null,"+
            "meal_option varchar(50) not null, "+
            "meal_category varchar(50) not null," +
            "meal_id integer not null,"+
            "foreign key (meal_id) references meals (meal_id));";

    private static final String dropDB = "drop table if exists plan;";

    private static final String insertMeal = "insert into plan (weekday, meal_option, meal_category, meal_id) " +
            "values ('%s','%s', '%s', %d);";

    private static final String selectPlanByWeekday = "select * from plan where weekday = '%s';";
    private static final String selectPlanAll = "select * from plan;";
    private DbClient dbClient;

    public DbPlan(DbClient dbClient) {
        this.dbClient = dbClient;
        dbClient.run(createDB);
    }

    public void add(Weekdays weekday, String meal, String meal_category, int mealId) {
        dbClient.run(String.format(insertMeal, weekday.getName(), meal,meal_category, mealId));

    }

    public void createDB(){
        dbClient.run(createDB);
    }
    public void dropDB(){
        dbClient.run(dropDB);
    }
    public void printEverything(){
        dbClient.selectPlan(selectPlanByWeekday);
    }

    public boolean isEmpty(){
        return dbClient.isEmpty(selectPlanAll);
    }

    public Map<String,Integer> getAllIngredients(){
        return dbClient.getAllIngredients(selectPlanAll);
    }
}
