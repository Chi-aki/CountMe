package com.example.chiakiiwamoto.countme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class StatisticsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Toolbar navbar;

    DBHandler mydatabase;
    private TextView CupDayTotal,CaffeineDayTotal,SugarDayTotal,chosenCoffeeText;
    private ImageView chosenCoffeeImg;
    private Button resetBtn;


    String setCoffeeText;
    int setSugarNumber;
    int setCoffeeImg;

    private int cupcount=0;
    private int caffeinecount=0;
    private int sugarcount=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        //calling database
        mydatabase = new DBHandler(this);

        //go to settings for the first time
        CoffeeModel test = mydatabase.numberOfRows();
        if(test.get_configTablenumRows() == 0)
        {
            startActivity(new Intent(StatisticsActivity.this, SettingActivity.class));
            finish();
            return;
        }

        //create custom top bar
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        //create nav bar at the bottom
        navbar = (Toolbar) findViewById(R.id.nav_bar);
        setSupportActionBar(navbar);
        getSupportActionBar().setTitle("");

        //get chosenCoffee name and sugars from coffee table
        chosenCoffeeText = (TextView) findViewById(R.id.chosenCoffee_text_statistics);
        CoffeeModel tmpCoffee = mydatabase.getFavouriteCoffee();
        setCoffeeText = tmpCoffee.get_coffeeName();
        setSugarNumber = tmpCoffee.get_sugars();
        chosenCoffeeText.setText(setCoffeeText + " | " + setSugarNumber + " sugar(s)");

        //get chosenCoffee img from coffee table
        chosenCoffeeImg = (ImageView) findViewById(R.id.chosenCoffee_img_statistics);
        setCoffeeImg = getResources().getIdentifier(tmpCoffee.get_coffeeImage(), "drawable", getPackageName());
        chosenCoffeeImg.setImageResource(setCoffeeImg);

        //get coffee total
        CupDayTotal = (TextView) findViewById(R.id.cup_total_statistics);
        CoffeeModel tmpCoffeeTotal = mydatabase.numberOfRows();
        cupcount = tmpCoffeeTotal.get_chosenTablenumRows();
        CupDayTotal.setText(cupcount + "");

        CoffeeModel tmpSugarCaffeineTotal = mydatabase.sum();
        //get caffeine total
        CaffeineDayTotal = (TextView) findViewById(R.id.caffeine_total);
        caffeinecount = tmpSugarCaffeineTotal.get_caffeineSum();
        CaffeineDayTotal.setText(caffeinecount + "mg");

        //get sugar total
        SugarDayTotal = (TextView) findViewById(R.id.sugar_total);
        sugarcount = tmpSugarCaffeineTotal.get_sugarSum();
        SugarDayTotal.setText(sugarcount * 4 + "g");

        //**** nav buttons ***//

        findViewById(R.id.nav_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StatisticsActivity.this, MainActivity.class));
                finish();
            }
        });
        findViewById(R.id.nav_statistics).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(StatisticsActivity.this, StatisticsActivity.class));
            }
        });
        findViewById(R.id.nav_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StatisticsActivity.this, SettingActivity.class));
                finish();
            }
        });
        findViewById(R.id.credit_page_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StatisticsActivity.this, CreditActivity.class));
                finish();
            }
        });

        //**** nav buttons END ***//

    }

    public void tapToReset(View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure, you want to reset all data?");

        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                mydatabase.deleteRows();
                startActivity(new Intent(StatisticsActivity.this, SettingActivity.class));
                finish();

            }
        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
