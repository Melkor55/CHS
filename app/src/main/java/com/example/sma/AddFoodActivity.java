package com.example.sma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import Models.Meal;
import Models.Product;
import Models.User;

public class AddFoodActivity extends AppCompatActivity {

    Meal meal;
    User user;
    Product product;
    
	Button saveButton;

	EditText  brandField;
	EditText  nameField;
	EditText  carbohydratesField;
	EditText  saturatedFatsField;
	EditText  polyolsField;
	EditText  sugarsField;
	EditText  weightField;
	EditText  caloriesField;
	EditText saltField;
	EditText  proteinField;
	EditText  fiberField;
	EditText  fatsField;
    
	TextView textViewUnit;
	TextView textViewFats;
	TextView textViewBrand;
	TextView textViewSaturatedFats;
	TextView textViewCalories;
	TextView textViewName;
	TextView textViewWeight;
	TextView textViewFiber;
	TextView textViewProtein;
	TextView textViewSugars;
	TextView textViewSalt;
	TextView textViewPolyols;
	TextView textViewCarbohydrates;
	TextView textViewStatus;

    Spinner unitSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        saveButton = findViewById(R.id.saveButton);

        brandField = findViewById(R.id.brandField);
        nameField = findViewById(R.id.nameField);
        fatsField = findViewById(R.id.fatsField);
        carbohydratesField = findViewById(R.id.carbohydratesField);
        saturatedFatsField = findViewById(R.id.saturatedFatsField);
        polyolsField = findViewById(R.id.polyolsField);
        sugarsField = findViewById(R.id.sugarsField);
        weightField = findViewById(R.id.weightField);
        caloriesField = findViewById(R.id.caloriesField);
        saltField = findViewById(R.id.saltField);
        proteinField = findViewById(R.id.proteinField);
        fiberField = findViewById(R.id.fiberField);

        textViewSaturatedFats = findViewById(R.id.textViewSaturatedFats);
        textViewCalories = findViewById(R.id.textViewCalories);
        textViewName = findViewById(R.id.textViewName);
        textViewUnit = findViewById(R.id.textViewUnit);
        textViewFats = findViewById(R.id.textViewFats);
        textViewBrand = findViewById(R.id.textViewBrand);
        textViewWeight = findViewById(R.id.textViewWeight);
        textViewFiber = findViewById(R.id.textViewFiber);
        textViewProtein = findViewById(R.id.textViewProtein);
        textViewSugars = findViewById(R.id.textViewSugars);
        textViewSalt = findViewById(R.id.textViewSalt);
        textViewPolyols = findViewById(R.id.textViewPolyols);
        textViewCarbohydrates = findViewById(R.id.textViewCarbohydrates);
        textViewStatus = findViewById(R.id.textViewStatus);

        unitSpinner = findViewById(R.id.unitSpinner);

        SpinnerAdapter spinnerAdapter ;
        String[] unitNames = {"grams", "lbs", "spoons", "milliliters", "pieces"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, unitNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(arrayAdapter);

        user = (User) getIntent().getSerializableExtra("CurrentUser");
        meal = (Meal) getIntent().getSerializableExtra("Meal");

        String localhost = "192.168.1.7:8090";
        String login_url_server = "https://csh-nodejs-api.azurewebsites.net/api/users";
        String login_url_local = "http://" + localhost + "/api";

        String createMealUrl = login_url_local + "/meals";
        String createDayUrl = login_url_local + "/days";
        String searchForMealUrl = login_url_local + "/meals" + "?MealId=155";
        String searchForProduct = login_url_local + "/products" + "?ProductId=155";

        if( findProduct(searchForProduct) )
        {
            textViewName.setText(product.getName());
            textViewBrand.setText(product.getBrand());
            textViewCalories.setText(product.getCalories());
            textViewWeight.setText(product.getWeight());
            textViewUnit.setText(product.getUnit());
            textViewSaturatedFats.setText(product.getSaturatedFats());
            textViewFats.setText(product.getFats());
            textViewCarbohydrates.setText(product.getCarbohydrates());
            textViewPolyols.setText(product.getPolyols());
            textViewSugars.setText(product.getSugars());
            textViewFiber.setText(product.getFiber());
            textViewProtein.setText(product.getProtein());
            textViewSalt.setText(product.getSalt());
        }
        else
        {
            Product newProduct = new Product(
					String.valueOf(textViewName.getText()),
					String.valueOf(textViewBrand.getText()),
					Integer.parseInt(String.valueOf(textViewCalories.getText())),
					Integer.parseInt(String.valueOf(textViewWeight.getText())),
					String.valueOf(textViewUnit.getText()),
					Integer.parseInt(String.valueOf(textViewSaturatedFats.getText())),
					Integer.parseInt(String.valueOf(textViewFats.getText())),
					Integer.parseInt(String.valueOf(textViewCarbohydrates.getText())),
					Integer.parseInt(String.valueOf(textViewPolyols.getText())),
					Integer.parseInt(String.valueOf(textViewSugars.getText())),
					Integer.parseInt(String.valueOf(textViewFiber.getText())),
					Integer.parseInt(String.valueOf(textViewProtein.getText())),
					Integer.parseInt(String.valueOf(textViewSalt.getText()))
            );
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("Meal", meal);
            resultIntent.putExtra("UpdatedUser", user);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    protected boolean findProduct(String url){
        return false;
    }

}