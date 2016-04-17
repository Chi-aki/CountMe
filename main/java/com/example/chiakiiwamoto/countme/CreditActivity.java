package com.example.chiakiiwamoto.countme;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;


public class CreditActivity extends AppCompatActivity {

    private ImageView linkedin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);

        findViewById(R.id.backToMain_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreditActivity.this, MainActivity.class));
                finish();
            }
        });

        linkedin = (ImageView) findViewById(R.id.link_linkedin);

        linkedin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.linkedin.com/in/chiaki-iwamoto-43085b87"));
                startActivity(intent);
            }
        });
    }


}
