package com.example.chiakiiwamoto.countme;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Toolbar navbar;

    //coffee
    private String[] TEXTS;
    private int[] IMAGES;

    private int mPosition = 0;
    private TextSwitcher mTextSwitcher;
    private ImageSwitcher mImageSwitcher;

    DBHandler mydatabase;

    //sugar
    private ImageButton SubtractSugar, AddSugar;
    private TextView CountSugar;
    int count=0;

    int currentlyshownsugar;
    int coffeeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mydatabase = new DBHandler(this);

        //get coffee name from db
        ArrayList<CoffeeModel> coffees = mydatabase.getAllCoffee();
        TEXTS = new String[coffees.size()];

        for(int i=0; i< coffees.size(); i++)
        {
            CoffeeModel tmpModel = coffees.get(i);
            TEXTS[i] = tmpModel.get_coffeeName();
        }

        //get coffee img from db
        IMAGES = new int[coffees.size()];

        for(int i= 0; i< coffees.size(); i++)
        {
            CoffeeModel tmpModel = coffees.get(i);
            IMAGES[i] = getResources().getIdentifier(tmpModel.get_coffeeImage(),"drawable",getPackageName());

        }

        //create custom top bar
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        //create nav bar at the bottom
        navbar = (Toolbar) findViewById(R.id.nav_bar);
        setSupportActionBar(navbar);
        getSupportActionBar().setTitle("");

        //**** nav buttons ***//

        findViewById(R.id.nav_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, MainActivity.class));
                finish();
            }
        });
        findViewById(R.id.nav_statistics).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, StatisticsActivity.class));
                finish();
            }
        });
        findViewById(R.id.nav_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(SettingActivity.this, SettingActivity.class));
            }
        });
        findViewById(R.id.credit_page_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, CreditActivity.class));
                finish();
            }
        });


        //**** nav buttons END ***//

        //***** content starts from here *****//

        //textSwitcher
        mTextSwitcher = (TextSwitcher) findViewById(R.id.textSwitcher);
        mTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(SettingActivity.this);
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(18);
                textView.setTextColor(Color.parseColor("#28200b"));
                textView.setTypeface(null, Typeface.BOLD);
                return textView;
            }

        });

        //text in out animation
        mTextSwitcher.setInAnimation(this, android.R.anim.fade_in);
        mTextSwitcher.setOutAnimation(this, android.R.anim.fade_out);

        //imgSwitcher
        mImageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
        mImageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(SettingActivity.this);
                return imageView;
            }
        });
        mImageSwitcher.setInAnimation(this, android.R.anim.slide_in_left);
        mImageSwitcher.setOutAnimation(this, android.R.anim.slide_out_right);

        updateScreen();

        //adding sugar button
        AddSugar = (ImageButton) findViewById(R.id.add_sugar_btn);
        AddSugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                CountSugar.setText(String.valueOf("Sugar X "+count));
            }
        });

        //subtract sugar button
        SubtractSugar = (ImageButton) findViewById(R.id.subtract_sugar_btn);
        SubtractSugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count > 0) {
                    count--;
                }
                CountSugar.setText(String.valueOf("Sugar X "+count));
            }
        });


        //Sugar counter
        CountSugar = (TextView) findViewById(R.id.count_sugar);
        CountSugar.setText("Sugar X " + count);

        SettingActivity adapter = new SettingActivity();
        adapter.setTextView(CountSugar);


    }

    //Choose coffee Button right
    public void showNext(View view) {

        mPosition++;// = (mPosition + 1) % TEXTS.length;
        if(mPosition > TEXTS.length-1)
            mPosition = 0;

        updateScreen();

    }

    private void updateScreen() {
        mTextSwitcher.setText(TEXTS[mPosition]);
        mImageSwitcher.setBackgroundResource(IMAGES[mPosition]);

    }

    //Choose coffee Button left
    public void showPrevious(View view) {

        mPosition--;// = (mPosition - 1) % TEXTS.length;
        if(mPosition < 0)
            mPosition = TEXTS.length-1;

        updateScreen();
    }

    //Sugar text view counter
    public void setTextView(TextView textViewFromActivity){
        this.CountSugar = textViewFromActivity;
    }

    // Save chosen coffee
    public void TapToSave(View view) {

        //get coffee id
        mTextSwitcher.setText(TEXTS[mPosition]);
        String currentlyshowncoffee = TEXTS[mPosition];

        CoffeeModel tmpCoffee = mydatabase.getCoffeeIdByName(currentlyshowncoffee);//new CoffeeModel();

        coffeeId = tmpCoffee.get_coffeeId();

        //get sugars
        currentlyshownsugar = count;

        mydatabase.insertFavouriteCoffee(coffeeId, currentlyshownsugar);

        Toast t = Toast.makeText(SettingActivity.this, "Saved!", Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();

        startActivity(new Intent(SettingActivity.this, MainActivity.class));
        finish();
    }
}
