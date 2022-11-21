package com.example.sma;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

import kotlin.Suppress;

public class ConnectionHelper {
    Connection connection;
    String ip,port,db,user,pass;

    @SuppressLint("NewApi")
    public Connection conclass(){
        ip="";
        port="";
        db="CHS1";
        user="";
        pass="";

        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        Connection con = null;
        String ConnectionURL = null;
        try{
            //Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL= "jdbc:sqlserver://chs-sma.database.windows.net:1433;database=CHS1;user=CloudSA384e7c0f@chs-sma;password=#Quer.presario55;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            con = DriverManager.getConnection(ConnectionURL);
        }
        catch (Exception exception)
        {
            Log.e("Error :", exception.getMessage());
        }
        return con;
    }
}
