package com.example.chiakiiwamoto.countme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Toolbar navbar;

    private Button TapToAddCoffee, SubtractCup;
    private TextView CupDayTotal, CaffeineDayTotal, SugarDayTotal,chosenCoffeeText;
    private ImageView chosenCoffeeImg;

    private int cupcount=0;
    private int caffeinecount=0;
    private int sugarcount=0;
    private int sugarcount4;
    DBHandler mydatabase;

    int favouriteCoffeeID;
    int setSugarAmount;

    String setCoffeeText;
    int setSugarNumber;
    int setCoffeeImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //calling database
        mydatabase = new DBHandler(this);

        //go to settings for the first time
        CoffeeModel test = mydatabase.numberOfRows();
        if(test.get_configTablenumRows() == 0)
        {
            startActivity(new Intent(MainActivity.this, SettingActivity.class));
            finish();
            return;
        }

        //SQLiteDatabase notused =  mydatabase.getReadableDatabase(); //just coz
        /*/get current date
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());

        mydatabase.insertFavouriteCoffee(1, 1);
        mydatabase.insertChosenCoffee(1, 1, formattedDate);*/

        //create custom top bar
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        //create nav bar at the bottom
        navbar = (Toolbar) findViewById(R.id.nav_bar);
        setSupportActionBar(navbar);
        getSupportActionBar().setTitle("");

        //get chosenCoffee name and sugars from coffee table
        chosenCoffeeText = (TextView) findViewById(R.id.chosenCoffee_text_main);
        CoffeeModel tmpCoffee = mydatabase.getFavouriteCoffee();
        setCoffeeText = tmpCoffee.get_coffeeName();
        setSugarNumber = tmpCoffee.get_sugars();
        chosenCoffeeText.setText(setCoffeeText + " | " + setSugarNumber + " sugar(s)");

        //get chosenCoffee img from coffee table
        chosenCoffeeImg = (ImageView) findViewById(R.id.chosenCoffee_img_main);
        setCoffeeImg = getResources().getIdentifier(tmpCoffee.get_coffeeImage(), "drawable", getPackageName());
        chosenCoffeeImg.setImageResource(setCoffeeImg);

        //**** nav buttons ***//

        findViewById(R.id.nav_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    startActivity(new Intent(MainActivity.this, StatisticsActivity.class));
            }
        });
        findViewById(R.id.nav_statistics).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, StatisticsActivity.class));
                finish(); //kill the app
            }
        });
        findViewById(R.id.nav_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                finish(); //kill the app
            }
        });
        findViewById(R.id.credit_page_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreditActivity.class));
                finish(); //kill the app
            }
        });

        //**** nav buttons END ***//


        //adding cup / caffeine / sugar button
        TapToAddCoffee = (Button) findViewById(R.id.tapToAdd_btn);


        CupDayTotal = (TextView) findViewById(R.id.cup_day_total);
        CoffeeModel tmpCoffeeTotal = mydatabase.numberOfRows();
        cupcount = tmpCoffeeTotal.get_chosenDailyTablenumRows();
        CupDayTotal.setText(cupcount + "cup(s)");

        CoffeeModel tmpSugarCaffeineTotal = mydatabase.dailySum();

        CaffeineDayTotal = (TextView) findViewById(R.id.caffeine_day_total);
        caffeinecount = tmpSugarCaffeineTotal.get_caffeineDailySum();
        CaffeineDayTotal.setText(caffeinecount + "mg");


        SugarDayTotal = (TextView) findViewById(R.id.sugar_day_total);
        sugarcount = tmpSugarCaffeineTotal.get_sugarDailySum();
        sugarcount4 = sugarcount * 4;
        SugarDayTotal.setText(sugarcount4 + "g");


        TapToAddCoffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get favourite coffee from coffeeConfig
                CoffeeModel tmpCoffee = mydatabase.getFavouriteCoffee();

                favouriteCoffeeID = tmpCoffee.get_coffeeId();
                setSugarAmount = tmpCoffee.get_sugars();

                //get current date
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = df.format(c.getTime());

                mydatabase.insertChosenCoffee(favouriteCoffeeID, setSugarAmount, formattedDate);

                CoffeeModel tmpCoffeeTotal = mydatabase.numberOfRows();
                cupcount = tmpCoffeeTotal.get_chosenDailyTablenumRows();
                CupDayTotal.setText(cupcount + "cup(s)");

                CoffeeModel tmpSugarCaffeineTotal = mydatabase.dailySum();

                CaffeineDayTotal = (TextView) findViewById(R.id.caffeine_day_total);
                caffeinecount = tmpSugarCaffeineTotal.get_caffeineDailySum();
                CaffeineDayTotal.setText(caffeinecount + "mg");

                sugarcount = tmpSugarCaffeineTotal.get_sugarDailySum();
                sugarcount4 = sugarcount * 4;
                SugarDayTotal.setText(sugarcount4 + "g");


                Toast t = Toast.makeText(MainActivity.this, "Added!", Toast.LENGTH_SHORT);
                t.setGravity(Gravity.CENTER, 0, 0);
                t.show();

            }
        });


        MainActivity adapter = new MainActivity();
        adapter.setTextView(CupDayTotal);
        adapter.setTextView(CaffeineDayTotal);
        adapter.setTextView(SugarDayTotal);
    }

    public void setTextView(TextView textViewFromActivity){
        this.CupDayTotal = textViewFromActivity;
        this.CaffeineDayTotal = textViewFromActivity;
        this.SugarDayTotal = textViewFromActivity;
    }

    public void SubtractCup(View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Do you want to delete one coffee?");

        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                mydatabase.deleteOneRow();

                CoffeeModel tmpCoffeeTotal = mydatabase.numberOfRows();
                cupcount = tmpCoffeeTotal.get_chosenDailyTablenumRows();
                CupDayTotal.setText(cupcount + "cup(s)");

                CoffeeModel tmpSugarCaffeineTotal = mydatabase.dailySum();

                caffeinecount = tmpSugarCaffeineTotal.get_caffeineDailySum();
                CaffeineDayTotal.setText(caffeinecount + "mg");


                sugarcount = tmpSugarCaffeineTotal.get_sugarDailySum();
                sugarcount4 = sugarcount * 4;
                SugarDayTotal.setText(sugarcount4 + "g");

                Toast t = Toast.makeText(MainActivity.this, "Removed!", Toast.LENGTH_SHORT);
                t.setGravity(Gravity.CENTER,0,0);
                t.show();

            }
        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}


