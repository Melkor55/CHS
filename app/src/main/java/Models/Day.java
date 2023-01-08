package Models;

import java.io.Serializable;
import java.util.Date;

public class Day implements Serializable {

    private int  DayId;
    private int  UserId;
	private String Date;
    private int  Goal;
    private int  Consumed;
    private int  Remaining;
    private int  Above;
	private int  Meal1Id;
	private int  Meal2Id;
	private int  Meal3Id;
	private int  Meal4Id;

    public Day(int dayId, int userId, String date, int goal, int consumed, int remaining, int above, int meal1Id, int meal2Id, int meal3Id, int meal4Id) {
        DayId = dayId;
        UserId = userId;
        Date = date;
        Goal = goal;
        Consumed = consumed;
        Remaining = remaining;
        Above = above;
        Meal1Id = meal1Id;
        Meal2Id = meal2Id;
        Meal3Id = meal3Id;
        Meal4Id = meal4Id;
    }

    public int getDayId() {
        return DayId;
    }

    public void setDayId(int dayId) {
        DayId = dayId;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getGoal() {
        return Goal;
    }

    public void setGoal(int goal) {
        Goal = goal;
    }

    public int getConsumed() {
        return Consumed;
    }

    public void setConsumed(int consumed) {
        Consumed = consumed;
    }

    public int getRemaining() {
        return Remaining;
    }

    public void setRemaining(int remaining) {
        Remaining = remaining;
    }

    public int getAbove() {
        return Above;
    }

    public void setAbove(int above) {
        Above = above;
    }

    public int getMeal1Id() {
        return Meal1Id;
    }

    public void setMeal1Id(int meal1Id) {
        Meal1Id = meal1Id;
    }

    public int getMeal2Id() {
        return Meal2Id;
    }

    public void setMeal2Id(int meal2Id) {
        Meal2Id = meal2Id;
    }

    public int getMeal3Id() {
        return Meal3Id;
    }

    public void setMeal3Id(int meal3Id) {
        Meal3Id = meal3Id;
    }

    public int getMeal4Id() {
        return Meal4Id;
    }

    public void setMeal4Id(int meal4Id) {
        Meal4Id = meal4Id;
    }

    @Override
    public String toString() {
        return "Day{" +
                "DayId=" + DayId +
                ", UserId=" + UserId +
                ", Date='" + Date + '\'' +
                ", Goal=" + Goal +
                ", Consumed=" + Consumed +
                ", Remaining=" + Remaining +
                ", Above=" + Above +
                ", Meal1Id=" + Meal1Id +
                ", Meal2Id=" + Meal2Id +
                ", Meal3Id=" + Meal3Id +
                ", Meal4Id=" + Meal4Id +
                '}';
    }

}
