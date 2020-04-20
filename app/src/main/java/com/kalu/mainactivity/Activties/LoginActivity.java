package com.kalu.mainactivity.Activties;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kalu.mainactivity.R;

public class LoginActivity extends AppCompatActivity {
    private EditText userEmail,userPassword;
    Button btnLogin;
    ProgressBar loginProgress;
    FirebaseAuth mAuth;
    Intent homeActivity;
    ImageView loginPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userEmail=findViewById(R.id.login_mail);
        userPassword=findViewById(R.id.login_password);
        loginProgress=findViewById(R.id.login_progress);
        loginProgress.setVisibility(View.INVISIBLE);
        loginPhoto=findViewById(R.id.loginimg);
        loginPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),RegAccont.class);
                startActivity(intent);
                finish();
            }
        });

        mAuth=FirebaseAuth.getInstance();
        homeActivity=new Intent(getApplicationContext(),MainActivity.class);

        btnLogin=findViewById(R.id.logn_btn);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProgress.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.INVISIBLE);
                final String mail=userEmail.getText().toString();
                final String password=userPassword.getText().toString();
                if(mail.isEmpty() || password.isEmpty()){
                    shortMessage("Please verify all fields");
                    btnLogin.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.INVISIBLE);}
                else  signIn(mail,password);
            }
        });

    }




    private void signIn(String mail, String password) {
        mAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    loginProgress.setVisibility(View.INVISIBLE);
                    btnLogin.setVisibility(View.VISIBLE);
                    updateUi();
                }
                else {
                    btnLogin.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.INVISIBLE);
                    shortMessage(task.getException().getMessage());
                }

            }
        });

    }
    private void updateUi() {
        startActivity(homeActivity);
        finish();
    }

    private void shortMessage(String please_verify_all_fields) {
        Toast.makeText(getApplicationContext(),please_verify_all_fields,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user=mAuth.getCurrentUser();
        if(user!=null)
            updateUi();
    }

}
