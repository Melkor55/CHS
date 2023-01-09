package com.example.sma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import Models.Day;
import Models.Meal;
import Models.Product;
import Models.User;

public class AddFoodActivity extends AppCompatActivity {

    Meal meal;
    User user;
    Product product,clickedProduct;
    
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
        clickedProduct = (Product) getIntent().getSerializableExtra("CurrentProduct");

        String localhost = "192.168.1.7:8090";
        String login_url_server = "https://csh-nodejs-api.azurewebsites.net/api/users";
        String login_url_local = "http://" + localhost + "/api";

        String createMealUrl = login_url_local + "/meals";
        String createDayUrl = login_url_local + "/days";
        String searchForMealUrl = login_url_local + "/meals" + "?MealId=155";
        //String searchForProduct = login_url_local + "/products" + "?ProductId=11";
        int productId = -1;
        if( clickedProduct != null && clickedProduct.getProductId() != productId)
            productId = clickedProduct.getProductId();
        String searchForProduct = login_url_local + "/products" + "?ProductId=" + productId;
        String updateProduct = login_url_local + "/updateProduct";

        if( findProduct(searchForProduct) )
        {
            nameField.setText(product.getName());
            brandField.setText(product.getBrand());
            caloriesField.setText(String.valueOf(product.getCalories()));
            weightField.setText(String.valueOf(product.getWeight()));
            unitSpinner.setSelection(arrayAdapter.getPosition(product.getUnit()));
            saturatedFatsField.setText(String.valueOf(product.getSaturatedFats()));
            fatsField.setText(String.valueOf(product.getFats()));
            carbohydratesField.setText(String.valueOf(product.getCarbohydrates()));
            polyolsField.setText(String.valueOf(product.getPolyols()));
            sugarsField.setText(String.valueOf(product.getSugars()));
            fiberField.setText(String.valueOf(product.getFiber()));
            proteinField.setText(String.valueOf(product.getProtein()));
            saltField.setText(String.valueOf(product.getSalt()));
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String barcode ;
                if (product != null)
                    barcode = product.getBarcode();
                else
                    barcode = "0";
                Product newProduct = new Product(
                        barcode,
                        String.valueOf(nameField.getText()),
                        String.valueOf(brandField.getText()),
                        Integer.parseInt(String.valueOf(caloriesField.getText())),
                        Integer.parseInt(String.valueOf(weightField.getText())),
                        unitSpinner.getSelectedItem().toString(),
                        Integer.parseInt(String.valueOf(saturatedFatsField.getText())),
                        Integer.parseInt(String.valueOf(fatsField.getText())),
                        Integer.parseInt(String.valueOf(carbohydratesField.getText())),
                        Integer.parseInt(String.valueOf(polyolsField.getText())),
                        Integer.parseInt(String.valueOf(sugarsField.getText())),
                        Integer.parseInt(String.valueOf(fiberField.getText())),
                        Integer.parseInt(String.valueOf(proteinField.getText())),
                        Integer.parseInt(String.valueOf(saltField.getText()))
                );
                System.out.println(product);
                product = updateProduct(updateProduct, newProduct);
                System.out.println(product);
                System.out.println(newProduct);
                //Toast.makeText(AddFoodActivity.this, "User Updated !", Toast.LENGTH_SHORT).show();
                if(product.isEqualTo(newProduct))
                {
                    Toast.makeText(AddFoodActivity.this, "No modifications for Product !", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(AddFoodActivity.this, "Product Updated !", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("Meal", meal);
                resultIntent.putExtra("UpdatedUser", user);
                setResult(RESULT_OK, resultIntent);
                this.finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
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


    protected boolean findProduct(String stringUrl){
        try {
            URL url = new URL(stringUrl);
            System.out.println("***Url :" + url);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("GET");
            //connection.setDoOutput(true);
            connection.setDoInput(true);

            int status = connection.getResponseCode();
            System.out.println("***Response Status :" + status);

            InputStream inputStream;
            if ( status >= 400 )
                inputStream = connection.getErrorStream();
            else
                inputStream = connection.getInputStream();
            //int status = connection.getResponseCode();
            //System.out.println("***Response Status :" + status);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream, "iso-8859-1"));

            String response = "";
            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                response += line;
            }
//            for (int i = 0; i < 1000000; i++) {
//
//            }
            bufferedReader.close();
            inputStream.close();
            connection.disconnect();

            System.out.println("Response  :" + response);
//            JSONObject jsonObject = new JSONObject("" + response);
//            JSONArray jsonArray = jsonObject.getJSONArray("Day");
//            JSONObject jsonObject2 = jsonArray.getJSONObject(0);
//  mai jos echivalentul
            JSONArray jsonArray = new JSONObject("" + response).getJSONArray("Product");
            //JSONObject jsonObject = new JSONObject("" + response).getJSONArray("Day").getJSONObject(0);
            //return jsonObject.getString("Day");
            //System.out.println(jsonObject);
            if (jsonArray.length() >= 1) {
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                product = new Product( jsonObject.getInt("ProductId"),
                                        jsonObject.getString("Barcode"),
                                        jsonObject.getString("Name"),
                                        jsonObject.getString("Brand"),
                                        jsonObject.getInt("Calories"),
                                        jsonObject.getInt("Weight"),
                                        jsonObject.getString("Unit"),
                                        jsonObject.getInt("Fats"),
                                        jsonObject.getInt("SaturatedFats"),
                                        jsonObject.getInt("Carbohydrates"),
                                        jsonObject.getInt("Polyols"),
                                        jsonObject.getInt("Sugars"),
                                        jsonObject.getInt("Fiber"),
                                        jsonObject.getInt("Protein"),
                                        jsonObject.getInt("Salt")
                );
                System.out.println(product);
                //return jsonArray.getJSONObject(0).toString();
            }
            else
            {
                return false;
            }
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException ee) {
            ee.printStackTrace();
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected Product updateProduct(String stringUrl, Product newProduct){
        try {
            URL url = new URL(stringUrl);
            System.out.println("***Url :" + url);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("PUT");
            //connection.setDoOutput(true);
            connection.setDoInput(true);

            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(outputStream, "UTF-8"));

            //System.out.println("*** User : " + user);

            String data =   URLEncoder.encode("Barcode", "UTF-8") + "=" + URLEncoder.encode(newProduct.getBarcode(), "UTF-8") +  "&"
                            + URLEncoder.encode("Name", "UTF-8") + "=" + URLEncoder.encode(newProduct.getName(), "UTF-8")  + "&"
                            + URLEncoder.encode("Brand", "UTF-8") + "=" + URLEncoder.encode(newProduct.getBrand(), "UTF-8")  + "&"
                            + URLEncoder.encode("Calories", "UTF-8") + "=" + newProduct.getCalories() + "&"
                            + URLEncoder.encode("Weight", "UTF-8") + "=" + + newProduct.getWeight() + "&"
                            + URLEncoder.encode("Unit", "UTF-8") + "=" + URLEncoder.encode(newProduct.getUnit(), "UTF-8")  + "&"
                            + URLEncoder.encode("Fats", "UTF-8") + "=" + + newProduct.getFats() + "&"
                            + URLEncoder.encode("SaturatedFats", "UTF-8") + "=" + + newProduct.getSaturatedFats() + "&"
                            + URLEncoder.encode("Carbohydrates", "UTF-8") + "=" + + newProduct.getCarbohydrates() + "&"
                            + URLEncoder.encode("Polyols", "UTF-8") + "=" + + newProduct.getPolyols() + "&"
                            + URLEncoder.encode("Sugars", "UTF-8") + "=" + + newProduct.getSugars() + "&"
                            + URLEncoder.encode("Fiber", "UTF-8") + "=" + + newProduct.getFiber() + "&"
                            + URLEncoder.encode("Protein", "UTF-8") + "=" + + newProduct.getProtein() + "&"
                            + URLEncoder.encode("Salt", "UTF-8") + "=" + + newProduct.getSalt();

            if( product != null && product.getProductId() >= 1)
                data = URLEncoder.encode("ProductId", "UTF-8") + "=" + product.getProductId() +  "&" + data;

            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            int status = connection.getResponseCode();
            System.out.println("***Response Status :" + status);

            InputStream inputStream;
            if ( status >= 400 )
                inputStream = connection.getErrorStream();
            else
                inputStream = connection.getInputStream();
            //int status = connection.getResponseCode();
            //System.out.println("***Response Status :" + status);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream, "iso-8859-1"));

            String response = "";
            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                response += line;
            }
//            for (int i = 0; i < 1000000; i++) {
//
//            }
            bufferedReader.close();
            inputStream.close();
            connection.disconnect();

            System.out.println("Response  :" + response);
//            JSONObject jsonObject = new JSONObject("" + response);
//            JSONArray jsonArray = jsonObject.getJSONArray("Day");
//            JSONObject jsonObject2 = jsonArray.getJSONObject(0);
//  mai jos echivalentul
            JSONArray jsonArray = new JSONObject("" + response).getJSONArray("Product");
            //JSONObject jsonObject = new JSONObject("" + response).getJSONArray("Day").getJSONObject(0);
            //return jsonObject.getString("Day");
            //System.out.println(jsonObject);
            if (jsonArray.length() >= 1) {
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                newProduct = new Product(
                                    jsonObject.getInt("ProductId"),
                                    jsonObject.getString("Barcode"),
                                    jsonObject.getString("Name"),
                                    jsonObject.getString("Brand"),
                                    jsonObject.getInt("Calories"),
                                    jsonObject.getInt("Weight"),
                                    jsonObject.getString("Unit"),
                                    jsonObject.getInt("Fats"),
                                    jsonObject.getInt("SaturatedFats"),
                                    jsonObject.getInt("Carbohydrates"),
                                    jsonObject.getInt("Polyols"),
                                    jsonObject.getInt("Sugars"),
                                    jsonObject.getInt("Fiber"),
                                    jsonObject.getInt("Protein"),
                                    jsonObject.getInt("Salt")
                );
                System.out.println(product);
                //return jsonArray.getJSONObject(0).toString();
            }

            return newProduct;
            
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException ee) {
            ee.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}