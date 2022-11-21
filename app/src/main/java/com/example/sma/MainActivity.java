package com.example.sma;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    Connection connect;
    String connectionResult = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GetDataToTextView();
    }

    public void GetDataToTextView()
    {
        TextView textView1 = (TextView) findViewById(R.id.text1);
        TextView textView2 = (TextView) findViewById(R.id.text2);
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.conclass();
            if(connect != null)
            {
                System.out.println("ok ...");
                String query = "Select * From USERS";
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next())
                {
                    textView1.setText(resultSet.getString(1));
                    textView2.setText(resultSet.getString(2));
                }
            }

        }
        catch (Exception exception)
        {
            Log.e("Eroor :", exception.getMessage());
        }
    }
}