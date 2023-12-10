package mealplanner;

import mealplanner.DB.DbClient;
import mealplanner.DB.DbIngredients;
import mealplanner.DB.DbMeals;
import mealplanner.DB.DbPlan;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Application {
    private Scanner scanner;
    private DbMeals dbMeals;
    private DbIngredients dbIngredients;
    private DbPlan dbPlan;


    public Application(){
        this.scanner = new Scanner(System.in);
        dbMeals = new DbMeals(new DbClient());
        dbIngredients = new DbIngredients(new DbClient());
        dbPlan = new DbPlan(new DbClient());
    }

    public void add(){
        System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
        String category = "";
        while(category.isEmpty()){
            String input = scanner.nextLine();
            switch (input) {
                case "breakfast":
                case "lunch":
                case "dinner":
                    category = input;
                    break;
                default:
                    System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
                    break;
            }
        }

        System.out.println("Input the meal's name:");
        String name ="";
        while(name.isEmpty()){
            String input = scanner.nextLine();
            if(isOnlyLettersInIngredient(input)){
                name = input;
            } else {
                System.out.println("Wrong format. Use letters only!");
            }
        }

        boolean containsLetters = true;
        System.out.println("Input the ingredients:");
        String ingredientsInput ="";
        while(ingredientsInput.isEmpty()){
            containsLetters = true;
            String input = scanner.nextLine();
            String [] inputIngredients = input.split(",");
            for(String ingredient : inputIngredients){
                if(!isOnlyLettersInIngredient(ingredient)){
                    System.out.println("Wrong format. Use letters only!");
                    containsLetters = false;
                    break;
                }
            }
            if(containsLetters){
                ingredientsInput = input;
            }
        }
        List<String> ingredients = List.of(ingredientsInput.split(","));
        Meal meal = new Meal(name, category, ingredients);
        dbMeals.insertMeal(meal);
        dbIngredients.add(meal);
        System.out.println("The meal has been added!");
    }


    public void show(){
        System.out.println("Which category do you want to print (breakfast, lunch, dinner)?");
        String category = "";
        while(category.isEmpty()){
            String input = scanner.nextLine();
            switch (input) {
                case "breakfast":
                case "lunch":
                case "dinner":
                    category = input;
                    break;
                default:
                    System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
                    break;
            }
        }
        List<Meal> meals = dbMeals.selectMealsByCategoryWithIngredients(category);
        if(meals.isEmpty()){
            System.out.println("No meals found.");
        }else {
            System.out.println("Category: "+ category);
            for (Meal meal : meals) {
                System.out.println(meal);
            }
        }
    }

    public void start(){
        String input = "";
        while(!input.equals("exit")){
            System.out.println("What would you like to do (add, show, plan, save, exit)?");
            input = scanner.nextLine();
            switch (input){
                case "add":
                    add();
                    break;
                case "show":
                    show();
                    break;
                case "plan":
                    plan();
                    break;
                case "save":
                    save();
                    break;
                default:
                    break;
            }
        }
        System.out.println("Bye!");
    }

    private void save() {
       if(dbPlan.isEmpty()) {
           System.out.println("Unable to save. Plan your meals first.");
       }else{
           try {
               System.out.println("Input a filename:");
               String filename = scanner.nextLine();
               PrintWriter printWriter = new PrintWriter(filename);
               Map<String, Integer> ingredients = dbPlan.getAllIngredients();
               for(String ingredient : ingredients.keySet()){
                   if(ingredients.get(ingredient) > 1){
                       printWriter.println(ingredient+ " x"+ingredients.get(ingredient));
                   }else {
                       printWriter.println(ingredient);
                   }
               }
               printWriter.close();
               System.out.println("Saved!");
           }catch(Exception e){
               System.out.println("Error while saving the file.");
           }
       }
    }

    private void plan() {
        dbPlan.dropDB();
        dbPlan.createDB();
        for (Weekdays weekday : Weekdays.values()) {
            System.out.println(weekday.getName());
            String breakfast = chooseBreakfast(weekday);
            String lunch = chooseLunch(weekday);
            String dinner = chooseDinner(weekday);
            System.out.println("Yeah! We planned the meals for "+ weekday.getName()+".");
            System.out.println();
            dbPlan.add(weekday, breakfast.split(",")[0],"breakfast", Integer.parseInt(breakfast.split(",")[1]));
            dbPlan.add(weekday, lunch.split(",")[0],"lunch", Integer.parseInt(lunch.split(",")[1]));
            dbPlan.add(weekday, dinner.split(",")[0],"dinner", Integer.parseInt(dinner.split(",")[1]));
        }
        dbPlan.printEverything();
    }

    private String chooseDinner(Weekdays weekday) {
        Map<String, Integer> dinnerMeals = dbMeals.selectMealsByCategory("dinner");
        dinnerMeals.keySet().forEach(System.out::println);
        System.out.println("Choose the dinner for "+ weekday.getName()+" from the list above:");
        String dinner = scanner.nextLine();
        while(!dinnerMeals.containsKey(dinner)){
            System.out.println("This meal doesn’t exist. Choose a meal from the list above.");
            dinner = scanner.nextLine();
        }
        return dinner+","+dinnerMeals.get(dinner);
    }

    private String chooseLunch(Weekdays weekday) {
        Map<String,Integer> lunchMeals = dbMeals.selectMealsByCategory("lunch");
        lunchMeals.keySet().forEach(System.out::println);
        System.out.println("Choose the lunch for "+ weekday.getName()+" from the list above:");
        String lunch = scanner.nextLine();
        while(!lunchMeals.containsKey(lunch)) {
            System.out.println("This meal doesn’t exist. Choose a meal from the list above.");
            lunch = scanner.nextLine();
        }
        return lunch+","+lunchMeals.get(lunch);
    }

    private String chooseBreakfast(Weekdays weekday) {
        Map<String, Integer > breakfastMeals = dbMeals.selectMealsByCategory("breakfast");
        breakfastMeals.keySet().forEach(System.out::println);
        System.out.println("Choose the breakfast for "+ weekday.getName()+" from the list above:");
        String breakfast = scanner.nextLine();
        while(!breakfastMeals.containsKey(breakfast)){
            System.out.println("This meal doesn’t exist. Choose a meal from the list above.");
            breakfast = scanner.nextLine();
        }
        return breakfast+","+breakfastMeals.get(breakfast);
    }

    public boolean isOnlyLettersInIngredient(String str) {
        if(str == null || str.isEmpty() || str.isBlank()){
            return false;
        }
        return str.matches("^[a-zA-Z\\s]*$");
    }

}
