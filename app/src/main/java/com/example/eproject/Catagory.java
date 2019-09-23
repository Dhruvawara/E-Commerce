package com.example.eproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Catagory extends AppCompatActivity {

    ImageView iEar,iHead,iSpeak;
    TextView tEar,tHead,tSpeak;
    LinearLayout lEar,lHead,lSpeak,lC;
    Intent i;
    Bundle bundle;
    String id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catagory);

        iEar=findViewById(R.id.eImage);
        iHead=findViewById(R.id.hImage);
        iSpeak=findViewById(R.id.sImage);
        tEar=findViewById(R.id.eText);
        tHead=findViewById(R.id.hText);
        tSpeak=findViewById(R.id.sText);
        lEar=findViewById(R.id.linear1);
        lHead=findViewById(R.id.linear2);
        lSpeak=findViewById(R.id.linear3);
        lC=findViewById(R.id.linearc);
        bundle = getIntent().getExtras();
        id = bundle.getString("ID");


        Picasso.get().load("https://images-na.ssl-images-amazon.com/images/I/715Be92nTwL._SL1500_.jpg").into(iEar);
        Picasso.get().load("https://brain-images-ssl.cdn.dixons.com/4/4/10161344/u_10161344.jpg").into(iHead);
        Picasso.get().load("https://www.bhphotovideo.com/images/images2000x2000/audioengine_a2_r_a2_2_75_powered_desktop_1208190.jpg").into(iSpeak);

        lEar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i=new Intent(Catagory.this,HomePage.class);
                i.putExtra("cat","Earphone");
                i.putExtra("ID",id);
                startActivity(i);
                finish();
            }
        });

        lHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i=new Intent(Catagory.this,HomePage.class);
                i.putExtra("cat","Headphone");
                i.putExtra("ID",id);
                startActivity(i);
                finish();
            }
        });

        lSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i=new Intent(Catagory.this,HomePage.class);
                i.putExtra("cat","Speaker");
                i.putExtra("ID",id);
                startActivity(i);
                finish();
            }
        });

        lC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i=new Intent(Catagory.this,HomePage.class);
                i.putExtra("cat","Cart");
                i.putExtra("ID",id);
                startActivity(i);
                finish();
            }
        });


    }
}
