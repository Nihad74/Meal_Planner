package mealplanner;

public enum Weekdays {
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday"),
    SUNDAY("Sunday");

    private String name;

    Weekdays(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
