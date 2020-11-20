package com.example.nikagabisoniajgufi29shualeduri;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Map<String, TextView> cachedTextViews = new HashMap<>();
    String species = "", name = "", ageStr = "";
    int ageInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    String getTextFromTextboxName(String viewName){
        int resID = getResources().getIdentifier(viewName, "id", getPackageName());
        TextView t = (TextView) findViewById(resID);
        cachedTextViews.put(viewName, t);
        return t.getText().toString();
    }
    void registrationImpossible(String prefix){
        Toast.makeText(this, prefix + "დარეგისტრირება შეუძლებელია!!!", Toast.LENGTH_LONG).show();
    }
    public void onAdd(View view){

        species = getTextFromTextboxName("species");
        name = getTextFromTextboxName("name");
        ageStr = getTextFromTextboxName("age");

        //Log.d("air", species + " " + name + " " + age);

        if(species.length() * name.length() * ageStr.length() == 0){
            Toast.makeText(this, "საჭიროა ყველა ველის შევსება!", Toast.LENGTH_SHORT).show();
            return;
        }

        ageInt = Integer.parseInt(ageStr);

        if(ageInt > 10){
            registrationImpossible("10 წელზე უფროსებისთვის ");
            return;
        }

        if(ageInt < 1){
            registrationImpossible("1 წელზე უმცროსებისთვის ");
            return;
        }

        Pet pet = new Pet(species, name, ageInt);

        Toast.makeText(this, "რეგისტრაცია გავლილია!", Toast.LENGTH_SHORT).show();

        for (String key : cachedTextViews.keySet()) {
            cachedTextViews.get(key).setText("");
        }
    }
}