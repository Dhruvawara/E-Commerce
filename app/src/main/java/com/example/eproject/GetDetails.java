package com.example.eproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

public class GetDetails extends AppCompatActivity {



    Bundle bundle;
    String id;
    String email;
    String x;
    String conCat;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference1;
    FirebaseStorage storage;
    StorageReference storageReference;
    EditText editText1;
    EditText editText2;
    EditText editText3;
    EditText editText4;
    EditText editText5;
    EditText editText6;
    Button button1;
    Button button2;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_details);

        bundle=getIntent().getExtras();
        id=bundle.getString("ID");
        email=bundle.getString("email").trim();

        Toast.makeText(GetDetails.this,id,Toast.LENGTH_LONG).show();
        x="-";
        editText1=findViewById(R.id.editText);
        editText2=findViewById(R.id.editText2);
        editText3=findViewById(R.id.editText3);
        editText4=findViewById(R.id.editText4);
        editText5=findViewById(R.id.editText5);
        editText6=findViewById(R.id.editText6);
        button1=findViewById(R.id.button_Upload);
        button2=findViewById(R.id.button_Submit);
        conCat=email.concat(x);
        conCat=conCat.concat(id);
        imageView=findViewById(R.id.imageView2);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFireChooser();
            }
        });



        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //pushing all input data
                databaseReference= FirebaseDatabase.getInstance().getReference("PRODUCT").child("Users");
                databaseReference.child(id).child("presonalDetails").child("name").setValue(editText1.getText().toString().trim());
                databaseReference.child(id).child("presonalDetails").child("door").setValue(editText2.getText().toString().trim());
                databaseReference.child(id).child("presonalDetails").child("area").setValue(editText3.getText().toString().trim());
                databaseReference.child(id).child("presonalDetails").child("city").setValue(editText4.getText().toString().trim());
                databaseReference.child(id).child("presonalDetails").child("pinCode").setValue(editText5.getText().toString().trim());
                databaseReference.child(id).child("presonalDetails").child("phone").setValue(editText6.getText().toString().trim());
                databaseReference.child(id).child("presonalDetails").child("email").setValue(email);
                databaseReference.child(id).child("Cart").child("ccount").setValue(0);
                databaseReference.child(id).child("Cart").child("tcount").setValue(0);



                uploadImage();

                //Going to Home Pag Activity
                Intent i=new Intent(GetDetails.this,Catagory.class);
                i.putExtra("ID",id);
                startActivity(i);
                finish();
            }
        });
    }


    private void openFireChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();

            Picasso.get().load(filePath).into(imageView);

        }
    }

    private void uploadImage() {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(GetDetails.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(GetDetails.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        bundle=getIntent().getExtras();
        id=bundle.getString("ID");
        email=bundle.getString("email").trim();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential("user@example.com", "password1234");

        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                           Toast.makeText(GetDetails.this,"Deleted",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }
                });
        Intent i = new Intent(GetDetails.this,SignInActivity.class);
        startActivity(i);
        finish();
    }
}
