package Models;

public class MealFood {

    private int MealId;
    private int ProductId;
    private int Quantity;

    public MealFood(int MealId, int ProductId, int Quantity)
    {
        this.MealId = MealId;
        this.ProductId = ProductId;
        this.Quantity = Quantity;
    }

    public int getMealId() {
        return MealId;
    }

    public void setMealId(int mealId) {
        MealId = mealId;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    @Override
    public String toString() {
        return "MealFood{" +
                "MealId=" + MealId +
                ", ProductId=" + ProductId +
                ", Quantity=" + Quantity +
                '}';
    }
}
