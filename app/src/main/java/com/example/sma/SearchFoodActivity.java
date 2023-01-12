package com.example.sma;

import static com.example.sma.ProfileActivity.user;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
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
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
//import android.widget.RecyclerView;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import Adapters.ProductAdapter;
import Models.Meal;
import Models.Product;
import Models.User;

public class SearchFoodActivity extends AppCompatActivity {

    SearchView searchView;
    RecyclerView recyclerView;
    ArrayList<String> list;
    //ArrayAdapter<String> adapter;
    //RecyclerView.Adapter  adapter;
    RecyclerView.Adapter  adapter;

    User user;
    Meal meal;

    ArrayList<Product> products = new ArrayList<Product>();;
    Product product, clickedProduct;
    ArrayList<String> productNames = new ArrayList<>();

    Button addFood;
    Button scanBarcodeButton;
    String searchProductsURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food);

    user = (User) getIntent().getSerializableExtra("CurrentUser");
    meal = (Meal) getIntent().getSerializableExtra("Meal");

    searchView = (SearchView) findViewById(R.id.searchView);
    recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    addFood = findViewById(R.id.addButton);
    scanBarcodeButton = findViewById(R.id.scanBarcodeButton);
    RecyclerView.LayoutManager recyclerViewlayoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(recyclerViewlayoutManager);
    recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
            DividerItemDecoration.VERTICAL));

    list = new ArrayList<>();
    list.add("Apple");
    list.add("Banana");
    list.add("Pineapple");
    list.add("Orange");
    list.add("Lychee");
    list.add("Gavava");
    list.add("Peech");
    list.add("Melon");
    list.add("Watermelon");
    list.add("Papaya");



    String localhost =  "192.168.43.51:8090";//"192.168.1.7:8090";
    String server = "csh-nodejs-api.azurewebsites.net";
    String login_url_server = "https://csh-nodejs-api.azurewebsites.net/api/users";
    String login_url_local = "http://" + server + "/api";

    String searchForProduct = login_url_local + "/products" + "?ProductId=11";
    String searchForProducts = login_url_local + "/products";

    searchProductsURL = searchForProducts;
    searchForProducts(searchForProducts);
    System.out.println(products);
    System.out.println(productNames);

    //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list);
    adapter = new ProductAdapter(this, products);
//        System.out.println("ok!");
    recyclerView.setAdapter((RecyclerView.Adapter) adapter);
    adapter.notifyItemChanged(3);
//        System.out.println("ok 2!");

    LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("get-selected-product"));

    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {

            if(list.contains(query)){
                //adapter.getFilter().filter(query);
            }else{
                Toast.makeText(SearchFoodActivity.this, "No Match found",Toast.LENGTH_LONG).show();
            }
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
                //adapter.getFilter().filter(newText);
            filter(newText);
            return false;
        }
    });

    addFood.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int REQUEST_CODE = 6;
            Intent intent = new Intent(SearchFoodActivity.this, AddFoodActivity.class);
            intent.putExtra("CurrentUser", (Serializable) user);
            intent.putExtra("Meal", (Serializable) meal);
            startActivity(intent);
        }
    });
    scanBarcodeButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int REQUEST_CODE = 8;
            Intent intent = new Intent(SearchFoodActivity.this, ScanBarcodeActivity.class);
            intent.putExtra("CurrentUser", (Serializable) user);
            intent.putExtra("Meal", (Serializable) meal);
            startActivityForResult(intent, REQUEST_CODE);
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

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            clickedProduct = (Product) intent.getSerializableExtra("clickedProduct");
            System.out.println("From Search activity -> " + clickedProduct);
            //Toast.makeText(getApplicationContext(),"Product returned !" ,Toast.LENGTH_SHORT).show();

            int REQUEST_CODE = 7;
            Intent newIntent = new Intent(SearchFoodActivity.this, AddFoodActivity.class);
            newIntent.putExtra("CurrentUser", (Serializable) user);
            newIntent.putExtra("Meal", (Serializable) meal);
            newIntent.putExtra("CurrentProduct", (Serializable) clickedProduct);
            startActivityForResult(newIntent, REQUEST_CODE);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 6) {
            if (resultCode == RESULT_OK) {
                user = (User) data.getSerializableExtra("UpdatedUser");
                products.clear();
                searchForProducts(searchProductsURL);
                adapter.notifyDataSetChanged();
                System.out.println("Product added !");
            }
            if (resultCode == RESULT_CANCELED) {
                System.out.println("Nothing selected !");

            }
        }
        if (requestCode == 7) {
            if (resultCode == RESULT_OK) {
                //user = (User) data.getSerializableExtra("UpdatedUser");
                //System.out.println("" + user);
                System.out.println("$$$$appened");
                //adapter.notifyDataSetChanged();
                //recyclerView.invalidate();
                //adapter = new ProductAdapter(this,products);
                products.clear();
                searchForProducts(searchProductsURL);

                //adapter = new ProductAdapter(this, products);
                adapter.notifyDataSetChanged();
                //adapter.notifyItemInserted(products.size());
                //recyclerView.invalidate();
                //recyclerView.setAdapter(adapter);

            }
            if (resultCode == RESULT_CANCELED) {
                System.out.println("Nothing happened");

            }
        }
        if (requestCode == 8) {
            if (resultCode == RESULT_OK) {
                //user = (User) data.getSerializableExtra("UpdatedUser");
                //System.out.println("" + user);
                products.clear();
                searchForProducts(searchProductsURL);

                //adapter = new ProductAdapter(this, products);
                adapter.notifyDataSetChanged();


            }
            if (resultCode == RESULT_CANCELED) {
                System.out.println("Nothing happened");

            }
        }
    }

    protected boolean searchForProducts(String stringUrl){
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
            JSONArray jsonArray = new JSONObject("" + response).getJSONArray("Product");

            //productNames = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
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
                productNames.add(i, product.getName());
                products.add(product);
                System.out.println(product);
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

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<Product> filteredList = new ArrayList<Product>();

        // running a for loop to compare elements.
        for (Product focusedProduct : products) {
            // checking if the entered string matched with any item of our recycler view.
            if (focusedProduct.getName().toLowerCase().contains(text.toLowerCase()) || focusedProduct.getBrand().toLowerCase().contains(text.toLowerCase()))
            {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredList.add(focusedProduct);
            }
        }
        if (filteredList.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            ProductAdapter tempAdapter = (ProductAdapter) adapter;
           // adapter = new ProductAdapter(this, products).filterList(filteredList);
            tempAdapter.filterList(filteredList);
            adapter = tempAdapter;

        }
    }


}