package Models;

import java.io.Serializable;

public class Meal implements Serializable {

	private int MealId;
	private int Calories;
	private int Fats;
	private int SaturatedFats;
	private int Carbohydrates;
	private int Polyols;
	private int Sugars;
	private int Fiber;
	private int Protein;
	private int Salt;

	public Meal()
	{

	}

	public Meal(int mealId, int calories, int fats, int saturatedFats, int carbohydrates, int polyols, int sugars, int fiber, int protein, int salt) {
		MealId = mealId;
		Calories = calories;
		Fats = fats;
		SaturatedFats = saturatedFats;
		Carbohydrates = carbohydrates;
		Polyols = polyols;
		Sugars = sugars;
		Fiber = fiber;
		Protein = protein;
		Salt = salt;
	}

	public int getMealId() {
		return MealId;
	}

	public void setMealId(int mealId) {
		MealId = mealId;
	}

	public int getCalories() {
		return Calories;
	}

	public void setCalories(int calories) {
		Calories = calories;
	}

	public int getFats() {
		return Fats;
	}

	public void setFats(int fats) {
		Fats = fats;
	}

	public int getSaturatedFats() {
		return SaturatedFats;
	}

	public void setSaturatedFats(int saturatedFats) {
		SaturatedFats = saturatedFats;
	}

	public int getCarbohydrates() {
		return Carbohydrates;
	}

	public void setCarbohydrates(int carbohydrates) {
		Carbohydrates = carbohydrates;
	}

	public int getPolyols() {
		return Polyols;
	}

	public void setPolyols(int polyols) {
		Polyols = polyols;
	}

	public int getSugars() {
		return Sugars;
	}

	public void setSugars(int sugars) {
		Sugars = sugars;
	}

	public int getFiber() {
		return Fiber;
	}

	public void setFiber(int fiber) {
		Fiber = fiber;
	}

	public int getProtein() {
		return Protein;
	}

	public void setProtein(int protein) {
		Protein = protein;
	}

	public int getSalt() {
		return Salt;
	}

	public void setSalt(int salt) {
		Salt = salt;
	}

	@Override
	public String toString() {
		return "Meal{" +
				"MealId=" + MealId +
				", Calories=" + Calories +
				", Fats=" + Fats +
				", SaturatedFats=" + SaturatedFats +
				", Carbohydrates=" + Carbohydrates +
				", Polyols=" + Polyols +
				", Sugars=" + Sugars +
				", Fiber=" + Fiber +
				", Protein=" + Protein +
				", Salt=" + Salt +
				'}';
	}
}
