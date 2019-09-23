package com.example.eproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {
    private ArrayList<ExampleItem> mExampleList;

    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Bundle bundle;
    String id;
    String cat;
    String cat1;
    String icount;
    String pID;
    String url;
    long ccount;
    long counti;
    long count;
    String name;
    String price;
    DatabaseReference databaseReference;
    RecyclerGetValue recyclerGetValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        recyclerGetValue=new RecyclerGetValue();
        bundle = getIntent().getExtras();
        id = bundle.getString("ID");
        createExampleList();
        buildRecyclerView();
    }


    public void createExampleList() {

        databaseReference=FirebaseDatabase.getInstance().getReference("PRODUCT");
        mExampleList = new ArrayList<>();
        bundle=getIntent().getExtras();
        cat = bundle.getString("cat").trim();


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String ID= bundle.getString("ID");

                    if(bundle.getString("cat").trim().equals("Cart")) {
                        ccount = (long) dataSnapshot.child("Users").child(String.valueOf(ID)).child("Cart").child("ccount").getValue();
                        for(int z = 0;z < ccount ; z++) {
                            cat1 = String.valueOf(dataSnapshot.child("Users").child(ID).child("Cart").child(String.valueOf(z)).child("cat").getValue()).trim();
                            icount = String.valueOf(dataSnapshot.child("Users").child(ID).child("Cart").child(String.valueOf(z)).child("icount").getValue()).trim();
                            pID = String.valueOf(dataSnapshot.child("Users").child(ID).child("Cart").child(String.valueOf(z)).child("pID").getValue()).trim();
                            url = (String) dataSnapshot.child(cat1).child(pID).child("Url").getValue();
                            name = (String) dataSnapshot.child(cat1).child(pID).child("name").getValue();
                            icount = "No Items: ".concat(icount);
                            mExampleList.add(new ExampleItem(url, name, icount));
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                    else {
                        count = Long.parseLong( dataSnapshot.child(cat).child("count").getValue().toString().trim()) ;
                        for(int z = 0;z < count ; z++) {

                            url = (String) dataSnapshot.child(cat).child(String.valueOf(z)).child("Url").getValue();
                            name = (String) dataSnapshot.child(cat).child(String.valueOf(z)).child("name").getValue();
                            price = (String) dataSnapshot.child(cat).child(String.valueOf(z)).child("Price").getValue();

                            mExampleList.add(new ExampleItem(url, name, price));
                            mAdapter.notifyDataSetChanged();
                        }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter(mExampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (bundle.getString("cat").trim().equals("Cart")) {
                    return;
                } else {
                    Intent i = new Intent(HomePage.this, ItemView.class);
                    bundle = getIntent().getExtras();
                    id = bundle.getString("ID");
                    i.putExtra("ID", id);
                    i.putExtra("cat", bundle.getString("cat"));
                    i.putExtra("PID", position);
                    startActivity(i);
                    mExampleList.clear();
                    finish();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(HomePage.this,Catagory.class);
        // i.putExtra("cat","Earphone");
        i.putExtra("ID",id);
        startActivity(i);
        finish();
    }
}
