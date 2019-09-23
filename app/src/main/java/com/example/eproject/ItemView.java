package com.example.eproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ItemView extends AppCompatActivity {
    Bundle bundle;
    int pId;
    Button cart, buy;
    boolean aBoolean = false;
    String cat;
    ImageView imageView;
    TextView textViewn, textViewd;
    DatabaseReference databaseReference;
    String url;
    String price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);

        bundle = getIntent().getExtras();

        pId = (int) bundle.get("PID");
        cat = bundle.getString("cat");
        cart = findViewById(R.id.buttonc);
        buy = findViewById(R.id.buttonb);
        imageView = findViewById(R.id.imageView);
        textViewn = findViewById(R.id.textn);
        textViewd = findViewById(R.id.textd);
        databaseReference = FirebaseDatabase.getInstance().getReference("PRODUCT");


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //  count = Integer.parseInt( dataSnapshot.child(cat).child("count").getValue().toString().trim()) ;
                url = (String) dataSnapshot.child(cat).child(String.valueOf(pId)).child("Url").getValue();
                Picasso.get().load(url).into(imageView);
                textViewn.setText((String) dataSnapshot.child(cat).child(String.valueOf(pId)).child("name").getValue());
                textViewd.setText((String) dataSnapshot.child(cat).child(String.valueOf(pId)).child("Des").getValue());
                price = "BUY ";
                price = price.concat((String) dataSnapshot.child(cat).child(String.valueOf(pId)).child("Price").getValue());
                buy.setText(price);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDataSet();
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder bO1 = new AlertDialog.Builder(ItemView.this);
                bO1.setTitle("Bought").setMessage("Deliverd to your given Address \n Mode of payment is COD").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(ItemView.this, "Done", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(ItemView.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                bO1.create().show();
            }
        });
    }


    public void onDataSet() {
        bundle = getIntent().getExtras();
        pId = (int) bundle.get("PID");
        final String ID = bundle.getString("ID");
        cat = bundle.getString("cat");
        databaseReference = FirebaseDatabase.getInstance().getReference("PRODUCT");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                //  count = Integer.parseInt( dataSnapshot.child(cat).child("count").getValue().toString().trim()) ;
                if ((long) dataSnapshot.child(cat).child(String.valueOf(pId)).child("count").getValue() == 0) {
                    Toast.makeText(ItemView.this, "Out of Stock", Toast.LENGTH_SHORT).show();
                } else {
                    aBoolean = true;
                    long count;
                    count = (long) dataSnapshot.child(cat).child(String.valueOf(pId)).child("count").getValue();
                    count--;
                    databaseReference.child(cat).child(String.valueOf(pId)).child("count").setValue(count);
                    if (aBoolean) {
                        aBoolean = false;
                        long ccount;
                        long icount;
                        long tcount;
                        try {
                            ccount = (long) dataSnapshot.child("Users").child(ID).child("Cart").child("ccount").getValue();
                            if (ccount == 0) {
                                databaseReference.child("Users").child(ID).child("Cart").child(String.valueOf(ccount)).child("cat").setValue(cat);
                                databaseReference.child("Users").child(ID).child("Cart").child(String.valueOf(ccount)).child("pID").setValue(pId);
                                databaseReference.child("Users").child(ID).child("Cart").child(String.valueOf(ccount)).child("icount").setValue(1);
                                ccount++;
                                databaseReference.child("Users").child(ID).child("Cart").child("ccount").setValue(ccount);
                            } else {
                                boolean t = true;
                                for (int i = 0; i < ccount; i++) {
                                    String a = (String) dataSnapshot.child("Users").child(ID).child("Cart").child(String.valueOf(i)).child("cat").getValue();
                                    if (a.equals(cat)) {
                                        long b = (long) dataSnapshot.child("Users").child(ID).child("Cart").child(String.valueOf(i)).child("pID").getValue();
                                        if (b == pId) {
                                            icount = (long) (dataSnapshot.child("Users").child(ID).child("Cart").child(String.valueOf(i)).child("icount").getValue());
                                            icount++;
                                            databaseReference.child("Users").child(ID).child("Cart").child(String.valueOf(i)).child("icount").setValue(icount);
                                            t = false;
                                            break;
                                        }
                                    }
                                }
                                if (t) {
                                    databaseReference.child("Users").child(ID).child("Cart").child(String.valueOf(ccount)).child("cat").setValue(cat);
                                    databaseReference.child("Users").child(ID).child("Cart").child(String.valueOf(ccount)).child("pID").setValue(pId);
                                    databaseReference.child("Users").child(ID).child("Cart").child(String.valueOf(ccount)).child("icount").setValue(1);
                                    ccount++;
                                    databaseReference.child("Users").child(ID).child("Cart").child("ccount").setValue(ccount);
                                }
                            }
                            tcount = (long) dataSnapshot.child("Users").child(ID).child("Cart").child("tcount").getValue();
                            tcount++;
                            databaseReference.child("Users").child(ID).child("Cart").child("tcount").setValue(tcount);

                        } catch (NullPointerException e) {
                            Toast.makeText(ItemView.this,e.toString(), Toast.LENGTH_LONG).show();
                        }


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        bundle = getIntent().getExtras();
        Intent i = new Intent(ItemView.this, HomePage.class);
        i.putExtra("ID", bundle.getString("ID"));
        i.putExtra("cat", bundle.getString("cat"));
        startActivity(i);
        finish();
    }
}
