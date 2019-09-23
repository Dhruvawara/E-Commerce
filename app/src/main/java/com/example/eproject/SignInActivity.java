package com.example.eproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

   private EditText editText_User;
   private EditText editText_Pass;
   private Button button_SignIn;
   private Button button_SignUp;
   private Intent intent;
   private Intent i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editText_User = findViewById(R.id.editText_User);
        editText_Pass = findViewById(R.id.editText2_Pass);
        button_SignIn = findViewById(R.id.button2_SignIn);
        button_SignUp = findViewById(R.id.button_SignUp);
        intent = new Intent(SignInActivity.this,Catagory.class);
        i = new Intent(SignInActivity.this,GetDetails.class);


        button_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
                    if(!Patterns.EMAIL_ADDRESS.matcher(editText_User.getText().toString().trim()).matches()){
                        editText_User.setError("Invalid ID");
                        editText_User.requestFocus();
                        return;
                    }
                }
                if(editText_Pass.getText().toString().trim().equals("")){
                    editText_Pass.setError("Enter Password");
                    editText_Pass.requestFocus();
                    return;
                }

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(editText_User.getText().toString().trim(),editText_Pass.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(editText_User.getText().toString().trim(),editText_Pass.getText().toString().trim());
                            Toast.makeText(SignInActivity.this,"Done",Toast.LENGTH_LONG).show();
                            String uid = FirebaseAuth.getInstance().getUid();
                            i.putExtra("ID",uid);
                            i.putExtra("email",editText_User.getText().toString().trim());
                            Toast.makeText(SignInActivity.this,"Done Signing In",Toast.LENGTH_LONG).show();
                            startActivity(i);
                            finish();
                        }
                        else{
                            Toast.makeText(SignInActivity.this,"Not Done",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        button_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signInWithEmailAndPassword(editText_User.getText().toString().trim(),editText_Pass.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            String uid = FirebaseAuth.getInstance().getUid();
                            intent.putExtra("ID",uid);
                            Toast.makeText(SignInActivity.this,"Done Signing In",Toast.LENGTH_LONG).show();
                            startActivity(intent);
                            finish();

                        }
                        else {
                            Toast.makeText(SignInActivity.this,"Not Done Signing In",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }

}


