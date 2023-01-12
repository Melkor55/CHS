package com.example.sma;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

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

import Models.Meal;
import Models.Product;
import Models.User;

public class ScanBarcodeActivity extends AppCompatActivity {

    SurfaceView surfaceView;
    TextView textViewScanBarcode;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    Button searchBarcodeButton;
    String intentData = "";
    String barcode = "";

    User user;
    Meal meal;
    Product product;

    String localhost =  "192.168.43.51:8090";//"192.168.1.7:8090";
    String server = "csh-nodejs-api.azurewebsites.net";
    String login_url_server = "https://csh-nodejs-api.azurewebsites.net/api/users";
    String login_url_local = "http://" + server + "/api";
    String searchForProduct = login_url_local + "/products" + "?Barcode=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);

        user = (User) getIntent().getSerializableExtra("CurrentUser");
        meal = (Meal) getIntent().getSerializableExtra("Meal");

        initViews();
    }

    private void initViews() {
        textViewScanBarcode = findViewById(R.id.textViewScanBarcode);
        surfaceView = findViewById(R.id.surfaceView);
        searchBarcodeButton = findViewById(R.id.searchBarcodeButton);
        searchBarcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean foodFound = false;
                foodFound = searchForProducts(searchForProduct+barcode);
                if( foodFound )
                {
                    Toast.makeText(getApplicationContext(), "Found", Toast.LENGTH_SHORT).show();
                    //int REQUEST_CODE = 9;
                    Intent intent = new Intent(ScanBarcodeActivity.this, AddFoodActivity.class);
                    intent.putExtra("CurrentUser", (Serializable) user);
                    intent.putExtra("Meal", (Serializable) meal);
                    intent.putExtra("CurrentProduct", (Serializable) product);
                    startActivity(intent);
                }

                else
                {
                    Toast.makeText(getApplicationContext(), "Not Found", Toast.LENGTH_SHORT).show();
                    //int REQUEST_CODE = 9;
                    Intent intent = new Intent(ScanBarcodeActivity.this, AddFoodActivity.class);
                    intent.putExtra("CurrentUser", (Serializable) user);
                    intent.putExtra("Meal", (Serializable) meal);
                    product = new Product(barcode,
                            "0",
                            "0",
                            0,
                            0,
                            "0",
                            0,
                            0,
                            0,
                            0,
                            0,
                            0,
                            0,
                            0
                    );
                    intent.putExtra("CurrentProduct", (Serializable) product);
                    startActivity(intent);
                }

            }
        });
//        btnAction.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (intentData.length() > 0) {
//                    if (isEmail)
//                        isEmail=false;//startActivity(new Intent(ScannedBarcodeActivity.this, EmailActivity.class).putExtra("email_address", intentData));
//                    else {
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(intentData)));
//                    }
//                }
//            }
//        });
    }

    private void initialiseDetectorsAndSources() {

        Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScanBarcodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScanBarcodeActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                //Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    textViewScanBarcode.post(new Runnable() {
                        @Override
                        public void run() {

                                searchBarcodeButton.setText("LAUNCH URL");
                                barcode = barcodes.valueAt(0).displayValue;
                                System.out.println(barcode);
                                textViewScanBarcode.setText(barcode);

                        }
                    });
                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }

    protected boolean searchForProducts(String stringUrl) {
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
            if (status >= 400)
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

            JSONArray jsonArray = new JSONObject("" + response).getJSONArray("Product");

            if (jsonArray.length() >= 1) {
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                product = new Product(jsonObject.getInt("ProductId"),
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
            } else {
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
}