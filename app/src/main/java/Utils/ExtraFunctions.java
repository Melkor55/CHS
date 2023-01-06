package Utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.sma.LoginActivity;
import com.example.sma.R;

public class ExtraFunctions {
    public void setHintTextAndColor(TextView textView, String hintMessage, int color)
    {
        textView.setHint(hintMessage);
        //textView.setHintTextColor(ContextCompat.getColor(getApplicationContext(), color));
        textView.setHintTextColor(color);
        textView.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    public void checkForEmptyData(String[] data, String[] dataName, TextView[] textViews, Context context, Resources resource,  int color)
    {
//        if ( data[0].isEmpty() && data[1].isEmpty())
//        {
//            setHintTextAndColor(emailField,"Empty Email Field", color);
//            setHintTextAndColor(passwordField,"Empty Email Field", color);
//            Toast.makeText(context, "The Email and Password Fields are Empty !", Toast.LENGTH_LONG).show();
//        }
//        else if( data[0].isEmpty() )
//        {
////                        emailField.setHint("Empty Email Field");
////                        emailField.setHintTextColor(ContextCompat.getColor(v.getContext(), color));
//            setHintTextAndColor(emailField,"Empty Email Field", color);
//            Toast.makeText(context, "The Email Field is Empty !", Toast.LENGTH_LONG).show();
//        }
//        else if( data[1].isEmpty() )
//        {
//            setHintTextAndColor(passwordField,"Empty Password Field", color);
//            Toast.makeText(context, "The Password Field is Empty !", Toast.LENGTH_LONG).show();
//        }
        boolean[] emptyFields = new boolean[data.length] ;
        String textForEmptyFields = "";
        for (int i = 0; i < data.length; i++) {
            if(data[i].isEmpty())
            {
                emptyFields[i] = true;
                if(textViews[i] != null)
                    setHintTextAndColor(textViews[i],"Empty " + dataName[i] +" Field", resource.getColor(color));
                //Toast.makeText(context, "Empty " + dataName[i] +" Field is Empty !", Toast.LENGTH_LONG).show();
            }
            else
            {

                if(textViews[i] != null)
                {
                    textViews[i].setBackgroundTintList(null);
                }
            }

        }
        for (int i = 0; i < emptyFields.length; i++) {
            if(emptyFields[i])
            {
                //setHintTextAndColor(textViews[i],"Empty " + dataName[i] +" Field", color);
                //Toast.makeText(context, "Empty " + dataName[i] +" Field is Empty !", Toast.LENGTH_LONG).show();
                if(i != emptyFields.length-1)
                    textForEmptyFields += dataName[i] + ", ";
                else
                    textForEmptyFields += dataName[i];
            }
        }
        if (!textForEmptyFields.equals(""))
            Toast.makeText(context, "Empty : " + textForEmptyFields + " !", Toast.LENGTH_LONG).show();
    }
}
