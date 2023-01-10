package com.example.sma;

import static com.example.sma.ProfileActivity.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import Adapters.ProductAdapter;
import Adapters.ProductMealAdapter;
import Models.Day;
import Models.Meal;
import Models.MealFood;
import Models.Product;
import Models.User;

public class HomeActivity extends AppCompatActivity {

    User user;
    Day today;
    Meal[] meals = new Meal[4];
    Meal meal = new Meal();
    String todayDate = new SimpleDateFormat("YYYY-MM-dd").format(Calendar.getInstance().getTime());
    String searchForMealFoodBase ;
    String searchForProductBase ;

    static int Goal = 2500;
    static int Consumed = 0;
    static int Remaining = Goal - Consumed;
    static int Above = (-1) * Remaining;

    static int caloriesBreakfast = 0 ;
    static int caloriesDinner = 0 ;
    static int caloriesLunch = 0 ;
    static int caloriesSnacks = 0;

    ImageButton profileButton;
    TextView textViewDate;
    ImageButton dateLeftButton;
    ImageButton dateRightButton;

    TextView textViewCaloriesLeft;
    TextView textViewCaloriesGoalValue;
    TextView textViewCaloriesConsumedValue;
    TextView textViewCaloriesLeftValue;

    ProgressBar progressBarCalories;
    ProgressBar progressBarCaloriesOver;

    RecyclerView recyclerViewBreakfast;
    RecyclerView recyclerViewLunch;
    RecyclerView recyclerViewDinner;
    RecyclerView recyclerViewSnacks;

//    RecyclerView.Adapter  adapterBreakfast;
//    RecyclerView.Adapter  adapterLunch;
//    RecyclerView.Adapter  adapterDinner;
//    RecyclerView.Adapter  adapterSnacks;
    RecyclerView.Adapter[] adapters = new ProductMealAdapter[4];

//    ArrayList<Product> productsBreakfast = new ArrayList<Product>();
//    ArrayList<Product> productsLunch = new ArrayList<Product>();
//    ArrayList<Product> productsDinner = new ArrayList<Product>();
//    ArrayList<Product> productsSnacks = new ArrayList<Product>();
    ArrayList<Product>[] products = new ArrayList[4];
    ArrayList<MealFood>[] mealFoods = new ArrayList[4];


    Button addFoodToBreakfastButton;
    Button addFoodToLunchButton;
    Button addFoodToDinnerButton;
    Button addFoodToSnacksButton;

    TextView textViewCaloriesBreakfast;
    TextView textViewCaloriesLunch;
    TextView textViewCaloriesDinner;
    TextView textViewCaloriesSnacks;
    Button addFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        profileButton = findViewById(R.id.profileButton);
        textViewDate = findViewById(R.id.textViewDate);
        dateLeftButton = findViewById(R.id.dateLeftButton);
        dateRightButton = findViewById(R.id.dateRightButton);

        textViewCaloriesLeft = findViewById(R.id.textViewCaloriesLeft);
        textViewCaloriesGoalValue = findViewById(R.id.textViewCaloriesGoalValue);
        textViewCaloriesConsumedValue = findViewById(R.id.textViewCaloriesConsumedValue);
        textViewCaloriesLeftValue = findViewById(R.id.textViewCaloriesLeftValue);

        progressBarCalories = findViewById(R.id.progressBarCalories);
        progressBarCaloriesOver = findViewById(R.id.progressBarCaloriesOver);

        recyclerViewBreakfast = findViewById(R.id.recyclerViewBreakfast);
        recyclerViewLunch = findViewById(R.id.recyclerViewLunch);
        recyclerViewDinner = findViewById(R.id.recyclerViewDinner);
        recyclerViewSnacks = findViewById(R.id.recyclerViewSnacks);

        addFoodToBreakfastButton = findViewById(R.id.addFoodToBreakfastButton);
        addFoodToLunchButton = findViewById(R.id.addFoodToLunchButton);
        addFoodToDinnerButton = findViewById(R.id.addFoodToDinnerButton);
        addFoodToSnacksButton = findViewById(R.id.addFoodToSnackButton);

        textViewCaloriesBreakfast = findViewById(R.id.textViewCaloriesBreakfast);
        textViewCaloriesLunch = findViewById(R.id.textViewCaloriesLunch);
        textViewCaloriesDinner = findViewById(R.id.textViewCaloriesDinner);
        textViewCaloriesSnacks = findViewById(R.id.textViewCaloriesSnacks);
        //addFood = findViewById(R.id.addButton);

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, d LLL");
        String currentDate = dateFormat.format(Calendar.getInstance().getTime());
        System.out.println("Date is : " + currentDate);
        textViewDate.setText(currentDate);

        user = (User) getIntent().getSerializableExtra("CurrentUser");
        System.out.println(user);
        int[] mealIds = new int[4];

        String localhost = "192.168.43.51";//"192.168.1.7:8090";
        String login_url_server = "https://csh-nodejs-api.azurewebsites.net/api/users";
        String login_url_local = "http://" + localhost + ":8090/api";

        //SimpleDateFormat databaseDateFormat = new SimpleDateFormat("YYYY-MM-dd");
        //String todayDate = databaseDateFormat.format(Calendar.getInstance().getTime());

        String searchForDayUrl = login_url_local + "/days" + "?UserId=" + user.getUserId() + "&Date=" + todayDate;
        String createMealUrl = login_url_local + "/meals";
        String createDayUrl = login_url_local + "/days";
        String searchForMealUrl = login_url_local + "/meals" + "?MealId=155";
        searchForMealFoodBase = login_url_local + "/mealFoods" + "?MealId=";
        searchForProductBase = login_url_local + "/products" + "?ProductId=";

        System.out.println(todayDate);
//        try {
//            JSONObject dayJSON = new JSONObject("" + searchForDay(searchForDayUrl));
//            System.out.println(dayJSON);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        //textViewCaloriesBreakfast.setText(String.valueOf(caloriesBreakfast));

        RecyclerView.LayoutManager recyclerViewlayoutManager = new LinearLayoutManager(this);
        recyclerViewBreakfast.setLayoutManager( new LinearLayoutManager(this));
        recyclerViewLunch.setLayoutManager( new LinearLayoutManager(this));
        recyclerViewDinner.setLayoutManager( new LinearLayoutManager(this));
        recyclerViewSnacks.setLayoutManager( new LinearLayoutManager(this));

        recyclerViewBreakfast.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL));
        recyclerViewLunch.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL));
        recyclerViewDinner.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL));
        recyclerViewSnacks.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL));

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("get-selected-product-home-activity"));

        if( !searchForDay(searchForDayUrl) )
        {
            System.out.println("Doesn't exist !");
            for (int i = 0; i < mealIds.length; i++) {
                meals[i] = createMealHandler(createMealUrl);
                calculateCaloriesForDay(meals[i]);
                mealIds[i] = meals[i].getMealId();
                System.out.println("Meal " + i + " - " + meals[i]);
            }
            createDayHandler(createDayUrl,mealIds);

        }
        else
        {
            System.out.println("Exists !!!");
            mealIds[0] = today.getMeal1Id();
            mealIds[1] = today.getMeal2Id();
            mealIds[2] = today.getMeal3Id();
            mealIds[3] = today.getMeal4Id();
            for (int i = 0; i < mealIds.length; i++) {
                meals[i] = searchForMeal(createMealUrl + "?MealId=" + mealIds[i]);
                calculateCaloriesForDay(meals[i]);
                System.out.println("Meal " + i + " - " + meals[i]);
            }
//            Meal newMeal = searchForMeal(createMealUrl + "?MealId=" + "181");
//            //caloriesBreakfast = newMeal.getCalories();
//            System.out.println(newMeal);
//            if (newMeal != null)
//                calculateCaloriesForDay(newMeal);
        }
        System.out.println(today);
//        meals[0] = searchForMeal(searchForMealUrl);
//        System.out.println("Meal 0 - " + meals[0]);

        System.out.println("Meal IDs are :" + Arrays.toString(mealIds));
        addProductsToRecyclerViews(searchForMealFoodBase, searchForProductBase);
        for (Meal meal: meals ) {
            calculateCaloriesForDay(meal);
        }

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int REQUEST_CODE = 5;
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                intent.putExtra("CurrentUser", (Serializable) user);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        addFoodToBreakfastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int REQUEST_CODE = 0;
                Intent intent = new Intent(HomeActivity.this, SearchFoodActivity.class);
                intent.putExtra("Meal", (Serializable) meals[0]);
                intent.putExtra("CurrentUser", (Serializable) user);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        addFoodToLunchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int REQUEST_CODE = 1;
                Intent intent = new Intent(HomeActivity.this, SearchFoodActivity.class);
                intent.putExtra("Meal", (Serializable) meals[1]);
                intent.putExtra("CurrentUser", (Serializable) user);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        addFoodToDinnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int REQUEST_CODE = 2;
                Intent intent = new Intent(HomeActivity.this, SearchFoodActivity.class);
                intent.putExtra("Meal", (Serializable) meals[2]);
                intent.putExtra("CurrentUser", (Serializable) user);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        addFoodToSnacksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int REQUEST_CODE = 3;
                Intent intent = new Intent(HomeActivity.this, SearchFoodActivity.class);
                intent.putExtra("Meal", (Serializable) meals[3]);
                intent.putExtra("CurrentUser", (Serializable) user);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            Product clickedProduct = (Product) intent.getSerializableExtra("clickedProduct");
            Meal clickedMeal = (Meal) intent.getSerializableExtra("Meal");
            System.out.println("From Search activity -> " + clickedProduct);
            //Toast.makeText(getApplicationContext(),"Product returned !" ,Toast.LENGTH_SHORT).show();

            int REQUEST_CODE = 7;
            Intent newIntent = new Intent(HomeActivity.this, AddFoodActivity.class);
            newIntent.putExtra("CurrentUser", (Serializable) user);
            newIntent.putExtra("Meal", (Serializable) clickedMeal);
            newIntent.putExtra("CurrentProduct", (Serializable) clickedProduct);
            startActivityForResult(newIntent, REQUEST_CODE);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5) {
            if (resultCode == RESULT_OK) {
                user = (User)  data.getSerializableExtra("UpdatedUser");
                System.out.println("" + user);
            }
            if (resultCode == RESULT_CANCELED) {
                System.out.println("Nothing selected");
            }
        }
        else if (requestCode == 6) {
            if (resultCode == RESULT_OK) {
                user = (User)  data.getSerializableExtra("UpdatedUser");
                System.out.println("" + user);
            }
            if (resultCode == RESULT_CANCELED) {
                System.out.println("Nothing selected");
            }
        }
        else if (requestCode == 7) {
            if (resultCode == RESULT_OK) {
                //user = (User) data.getSerializableExtra("UpdatedUser");
                //System.out.println("" + user);
                System.out.println("$$$$appened");
                //adapter.notifyDataSetChanged();
                //recyclerView.invalidate();
                //adapter = new ProductMealAdapter(this,products);
//                products.clear();
//                searchForProducts(searchProductsURL);
                user = (User) data.getSerializableExtra("UpdatedUser");
                meal = (Meal) data.getSerializableExtra("Meal");
                for (int i = 0; i < 4; i++)
                {
                        adapters[i].notifyDataSetChanged();
                        addProductsToRecyclerViews(searchForMealFoodBase, searchForProductBase);
                        for (Meal meal: meals )
                        {
                            calculateCaloriesForDay(meal);
                        }
                        System.out.println("" + meals[i]);
                }
                //adapter = new ProductMealAdapter(this, products);
//                adapter.notifyDataSetChanged();
//                adapter.notifyItemInserted(products.size());
//                recyclerView.invalidate();
                //recyclerView.setAdapter(adapter);

            }
            if (resultCode == RESULT_CANCELED) {
                System.out.println("Nothing happened");

            }
        }
        else {
            for (int i = 0; i < 4; i++) {
                if (requestCode == i) {
                    if (resultCode == RESULT_OK) {
                        meals[i] = (Meal) data.getSerializableExtra("Meal");
                        user = (User) data.getSerializableExtra("UpdatedUser");
                        adapters[i].notifyDataSetChanged();
                        addProductsToRecyclerViews(searchForMealFoodBase, searchForProductBase);
                        for (Meal meal: meals ) {
                            calculateCaloriesForDay(meal);
                        }
                        System.out.println("" + meals[i]);
                    }
                    if (resultCode == RESULT_CANCELED) {
                        System.out.println("Nothing updated ?");
                    }
                }
            }
        }
    }

    private boolean searchForDay(String stringUrl){

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
            JSONArray jsonArray = new JSONObject("" + response).getJSONArray("Day");
            //JSONObject jsonObject = new JSONObject("" + response).getJSONArray("Day").getJSONObject(0);
            //return jsonObject.getString("Day");
            //System.out.println(jsonObject);
            if (jsonArray.length() >= 1) {
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                today = new Day( jsonObject.getInt("DayId"),
                                jsonObject.getInt("UserId"),
                                jsonObject.getString("Date"),
                                jsonObject.getInt("Goal"),
                                jsonObject.getInt("Consumed"),
                                jsonObject.getInt("Remaining"),
                                jsonObject.getInt("Above"),
                                jsonObject.getInt("Meal1Id"),
                                jsonObject.getInt("Meal2Id"),
                                jsonObject.getInt("Meal3Id"),
                                jsonObject.getInt("Meal4Id")
                                );
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

    private boolean createDayHandler(String stringUrl, int[] mealIds) {

        try {
            URL url = new URL(stringUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(outputStream, "UTF-8"));

            //System.out.println("*** User : " + user);

            String data = URLEncoder.encode("UserId", "UTF-8") + "=" + user.getUserId() + "&"
                    + URLEncoder.encode("Date", "UTF-8") + "=" + URLEncoder.encode(todayDate, "UTF-8")+  "&"
                    + URLEncoder.encode("Goal", "UTF-8") + "=" + Goal + "&"
                    + URLEncoder.encode("Consumed", "UTF-8") + "=" + Consumed + "&"
                    + URLEncoder.encode("Remaining", "UTF-8") + "=" + Remaining + "&"
                    + URLEncoder.encode("Above", "UTF-8") + "=" + Above + "&"
                    + URLEncoder.encode("Meal1Id", "UTF-8") + "=" + mealIds[0] + "&"
                    + URLEncoder.encode("Meal2Id", "UTF-8") + "=" + mealIds[1] + "&"
                    + URLEncoder.encode("Meal3Id", "UTF-8") + "=" + mealIds[2] + "&"
                    + URLEncoder.encode("Meal4Id", "UTF-8") + "=" + mealIds[3];

            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            int status = connection.getResponseCode();
            System.out.println("***Response Status :" + status);

            InputStream inputStream;
            if (status >= 400)
                inputStream = connection.getErrorStream();
            else
                inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream, "iso-8859-1"));

            String response = "";
            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                response += line;
            }

            bufferedReader.close();
            inputStream.close();
            connection.disconnect();

            System.out.println(response);

            JSONArray jsonArray = new JSONObject("" + response).getJSONArray("Day");
            if (jsonArray.length() >= 1) {
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                today = new Day(jsonObject.getInt("DayId"),
                        jsonObject.getInt("UserId"),
                        jsonObject.getString("Date"),
                        jsonObject.getInt("Goal"),
                        jsonObject.getInt("Consumed"),
                        jsonObject.getInt("Remaining"),
                        jsonObject.getInt("Above"),
                        jsonObject.getInt("Meal1Id"),
                        jsonObject.getInt("Meal2Id"),
                        jsonObject.getInt("Meal3Id"),
                        jsonObject.getInt("Meal4Id")
                );
            }
            //return jsonObject.getInt("MealId");
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

    private Meal searchForMeal(String stringUrl){

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

            JSONArray jsonArray = new JSONObject("" + response).getJSONArray("Meal");
            Meal meal;
            if (jsonArray.length() >= 1) {
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                meal = new Meal( jsonObject.getInt("MealId"),
                        jsonObject.getInt("Calories"),
                        jsonObject.getInt("Fats"),
                        jsonObject.getInt("SaturatedFats"),
                        jsonObject.getInt("Carbohydrates"),
                        jsonObject.getInt("Polyols"),
                        jsonObject.getInt("Sugars"),
                        jsonObject.getInt("Fiber"),
                        jsonObject.getInt("Protein"),
                        jsonObject.getInt("Salt")
                );
                //return jsonArray.getJSONObject(0).toString();
                System.out.println(meal);
            }
            else
            {
                return null;
            }
            return meal;
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

    private Meal createMealHandler(String stringUrl) {

        try {
            URL url = new URL(stringUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(outputStream, "UTF-8"));

            //System.out.println("*** User : " + user);

            String data = URLEncoder.encode("Calories", "UTF-8") + "=" + 0 + "&"
                        + URLEncoder.encode("Fats", "UTF-8") + "=" + 0 + "&"
                        + URLEncoder.encode("SaturatedFats", "UTF-8") + "=" + 0 + "&"
                        + URLEncoder.encode("Carbohydrates", "UTF-8") + "=" + 0 + "&"
                        + URLEncoder.encode("Polyols", "UTF-8") + "=" + 0 + "&"
                        + URLEncoder.encode("Sugars", "UTF-8") + "=" + 0 + "&"
                        + URLEncoder.encode("Fiber", "UTF-8") + "=" + 0 + "&"
                        + URLEncoder.encode("Protein", "UTF-8") + "=" + 0 + "&"
                        + URLEncoder.encode("Salt", "UTF-8") + "=" + 0;

            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            int status = connection.getResponseCode();
            System.out.println("***Response Status :" + status);

            InputStream inputStream;
            if (status >= 400)
                inputStream = connection.getErrorStream();
            else
                inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream, "iso-8859-1"));

            String response = "";
            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                response += line;
            }

            bufferedReader.close();
            inputStream.close();
            connection.disconnect();

            System.out.println(response);

            JSONArray jsonArray = new JSONObject("" + response).getJSONArray("Meal");
            //JSONObject jsonObject = new JSONObject("" + response).getJSONArray("Day").getJSONObject(0);
            //return jsonObject.getString("Day");
            //System.out.println(jsonObject);
            Meal meal;
            if (jsonArray.length() >= 1) {
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                meal = new Meal( jsonObject.getInt("MealId"),
                        jsonObject.getInt("Calories"),
                        jsonObject.getInt("Fats"),
                        jsonObject.getInt("SaturatedFats"),
                        jsonObject.getInt("Carbohydrates"),
                        jsonObject.getInt("Polyols"),
                        jsonObject.getInt("Sugars"),
                        jsonObject.getInt("Fiber"),
                        jsonObject.getInt("Protein"),
                        jsonObject.getInt("Salt")
                );
                //return jsonArray.getJSONObject(0).toString();
                System.out.println(meal);
            }
            else
                return null;

            //return jsonObject.getInt("MealId");
            return meal;
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

    protected boolean searchForMealFoods(String stringUrl, int index){
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
            bufferedReader.close();
            inputStream.close();
            connection.disconnect();

            System.out.println("Response  :" + response);
            JSONArray jsonArray = new JSONObject("" + response).getJSONArray("mealFood");

            //productNames = new String[jsonArray.length()];
            mealFoods[index] = new ArrayList<MealFood>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                MealFood mealFood = new MealFood( jsonObject.getInt("MealId"),
                                                    jsonObject.getInt("ProductId"),
                                                    jsonObject.getInt("Quantity")
                );
                System.out.println(mealFood);
                //productNames.add(i, product.getName());
                mealFoods[index].add(mealFood);
                //return jsonArray.getJSONObject(0).toString();
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

    protected Product findProduct(String stringUrl){
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
            Product product;
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
                return null;
            }
            return product;
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

    private void updateUI ()
    {
        textViewCaloriesBreakfast.setText(String.valueOf(caloriesBreakfast));
        textViewCaloriesLunch.setText(String.valueOf(caloriesLunch));
        textViewCaloriesDinner.setText(String.valueOf(caloriesDinner));
        textViewCaloriesSnacks.setText(String.valueOf(caloriesSnacks));

        textViewCaloriesConsumedValue.setText(String.valueOf(Consumed));
        progressBarCalories.setProgress((int) (((float)Consumed/Goal)*100));

        //progressBarCalories.setProgress(20);
        if( Remaining >= 0 )
        {
            progressBarCaloriesOver.setProgress(0);
            textViewCaloriesLeftValue.setText(String.valueOf(Remaining));
            textViewCaloriesLeft.setText("Remaining");
            //System.out.println("Progress" + (Above/(0.2*Goal))*100);
        }
        else
        {
            progressBarCaloriesOver.setProgress((int) ((Above/(0.2*Goal))*100));  //practic maximul de calorii depasite
            System.out.println("Progress" + progressBarCaloriesOver.getProgress());
            textViewCaloriesLeftValue.setText(String.valueOf(Above));       //este 20% din Goal (pentru mai mult, ramane tot inrosita)
            textViewCaloriesLeft.setText("Over");
        }

    }
    private void calculateCaloriesForDay (Meal meal)
    {
        if(meal.getMealId() == meals[0].getMealId())
        {
            caloriesBreakfast = meal.getCalories();
        }
        else if(meal.getMealId() == meals[1].getMealId())
        {
            caloriesLunch = meal.getCalories();
        }
        else if(meal.getMealId() == meals[2].getMealId())
        {
            caloriesDinner = meal.getCalories();
        }
        else if(meal.getMealId() == meals[3].getMealId())
        {
            caloriesSnacks = meal.getCalories();
        }
        Consumed = caloriesBreakfast + caloriesLunch + caloriesDinner + caloriesSnacks;
        Remaining = Goal - Consumed;
        Above = (-1) * Remaining;
        updateUI();
    }

    private void calculateCaloriesForMeals (Meal[] meals)
    {

    }


    private void addProductsToRecyclerViews(String baseUrlForMealFoods, String baseUrlForProducts) {
        for(int j = 0 ; j < meals.length ; j ++) {
            if (searchForMealFoods(baseUrlForMealFoods + meals[j].getMealId(), j)) {
                System.out.println(mealFoods[j]);
                products[j] = new ArrayList<Product>();
                meals[j].setCalories(0);
                for (int i = 0; i < mealFoods[j].size(); i++) {
                    products[j].add(findProduct(baseUrlForProducts + mealFoods[j].get(i).getProductId()));
                    meals[j].setCalories(meals[j].getCalories()+products[j].get(i).getCalories());
                }
            }
            adapters[j] = new ProductMealAdapter(this, products[j],meals[j]);
            System.out.println(Arrays.toString(products));
        }
        recyclerViewBreakfast.setAdapter(adapters[0]);
        recyclerViewLunch.setAdapter(adapters[1]);
        recyclerViewDinner.setAdapter(adapters[2]);
        recyclerViewSnacks.setAdapter(adapters[3]);
    }


}