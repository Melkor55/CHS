package Models;

import java.io.Serializable;
import java.util.Objects;

public class Product implements Serializable {
	private int ProductId;

    private String Barcode;

    private String Name;
    private String Brand;
    private int Calories;
    private int Weight;
    private String Unit;
    private int Fats;
    private int SaturatedFats;
    private int Carbohydrates;
    private int Polyols;
    private int Sugars;
    private int Fiber;
    private int Protein;
    private int Salt;

    public Product() {
    }

    public Product(String name, String brand, int calories, int weight, String unit, int fats, int saturatedFats, int carbohydrates, int polyols, int sugars, int fiber, int protein, int salt) {
        Name = name;
        Brand = brand;
        Calories = calories;
        Weight = weight;
        Unit = unit;
        Fats = fats;
        SaturatedFats = saturatedFats;
        Carbohydrates = carbohydrates;
        Polyols = polyols;
        Sugars = sugars;
        Fiber = fiber;
        Protein = protein;
        Salt = salt;
    }

    public Product(String barcode, String name, String brand, int calories, int weight, String unit, int fats, int saturatedFats, int carbohydrates, int polyols, int sugars, int fiber, int protein, int salt) {
        Barcode = barcode;
        Name = name;
        Brand = brand;
        Calories = calories;
        Weight = weight;
        Unit = unit;
        Fats = fats;
        SaturatedFats = saturatedFats;
        Carbohydrates = carbohydrates;
        Polyols = polyols;
        Sugars = sugars;
        Fiber = fiber;
        Protein = protein;
        Salt = salt;
    }

    public Product(int productId, String barcode, String name, String brand, int calories, int weight, String unit, int fats, int saturatedFats, int carbohydrates, int polyols, int sugars, int fiber, int protein, int salt) {
        ProductId = productId;
        Barcode = barcode;
        Name = name;
        Brand = brand;
        Calories = calories;
        Weight = weight;
        Unit = unit;
        Fats = fats;
        SaturatedFats = saturatedFats;
        Carbohydrates = carbohydrates;
        Polyols = polyols;
        Sugars = sugars;
        Fiber = fiber;
        Protein = protein;
        Salt = salt;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public int getCalories() {
        return Calories;
    }

    public void setCalories(int calories) {
        Calories = calories;
    }

    public int getWeight() {
        return Weight;
    }

    public void setWeight(int weight) {
        Weight = weight;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
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
        return "Product{" +
                "ProductId=" + ProductId +
                ", Barcode='" + Barcode + '\'' +
                ", Name='" + Name + '\'' +
                ", Brand='" + Brand + '\'' +
                ", Calories=" + Calories +
                ", Weight=" + Weight +
                ", Unit='" + Unit + '\'' +
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

    public String[] getFieldNameString() {
        return new String[]{
                "Brand",
                "Name",
                "Calories",
                "Weight",
                "Unit",
                "Fats",
                "SaturatedFats",
                "Carbohydrates",
                "Polyols",
                "Sugars",
                "Fiber",
                "Protein",
                "Salt"
        };
    }
    
    public boolean isEqualTo(Product product)
    {
        if(
            this.ProductId == product.ProductId &&
            this.Barcode.equals(product.Barcode) &&
            this.Name.equals(product.Name) &&
            this.Brand.equals(product.Brand) &&
            this.Calories == product.Calories &&
            this.Weight == product.Weight &&
            this.Unit.equals(product.Unit) &&
            this.Fats == product.Fats &&
            this.SaturatedFats == product.SaturatedFats &&
            this.Carbohydrates == product.Carbohydrates &&
            this.Polyols == product.Polyols &&
            this.Sugars == product.Sugars &&
            this.Fiber == product.Fiber &&
            this.Protein == product.Protein &&
            this.Salt == product.Salt
        )
            return true;
        return false;
    }
}
