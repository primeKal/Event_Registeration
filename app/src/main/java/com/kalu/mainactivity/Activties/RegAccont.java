package com.kalu.mainactivity.Activties;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kalu.mainactivity.R;

public class RegAccont extends AppCompatActivity {
    static final int REQUESTCODE =000;
    ImageView imgUserPhoto;
    static int FReqCode=1;
    Uri pickedImgUrl=null;
    EditText userEmail,userPassword,userPaswword2,username;
    ProgressBar loadingprogressBar;
    Button regBtn;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_accont);

        userEmail=findViewById(R.id.regMail);
        username=findViewById(R.id.regName);
        userPassword=findViewById(R.id.regPassword);
        userPaswword2=findViewById(R.id.regPassword2);
        imgUserPhoto =findViewById(R.id.nav_userphoto);
        loadingprogressBar=findViewById(R.id.buttonNext);
        loadingprogressBar.setVisibility(View.INVISIBLE);
        regBtn=findViewById(R.id.regBtn);

        firebaseAuth=FirebaseAuth.getInstance();

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regBtn.setVisibility(View.INVISIBLE);
                loadingprogressBar.setVisibility(View.VISIBLE);


                String email=userEmail.getText().toString();
                String name=username.getText().toString();
                String password=userPassword.getText().toString();
                String password2=userPaswword2.getText().toString();
                if(email.isEmpty() || name.isEmpty() || password.isEmpty() || !(password.equals(password2) )){
                    shortMessage("Verify all Fields");
                    regBtn.setVisibility(View.VISIBLE);}
                else creatUserAccount(email,password,name);
            }
        });



        imgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>22)
                    checkAndRequestPermission();
                else {openGallary();}
            }
        });
    }

    private void creatUserAccount(String email, String password, final String name) {
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    shortMessage("Account Created");
                    updateUserInfo(name,pickedImgUrl,firebaseAuth.getCurrentUser());
                }
                else{
                    shortMessage("Failed To create Account");
                    loadingprogressBar.setVisibility(View.INVISIBLE);
                    regBtn.setVisibility(View.VISIBLE);
                }
            }
        });



    }
    private void updateUserInfo(final String name, Uri pickedImgUrl, final FirebaseUser currentuser){
        shortMessage("starting img upload");
        StorageReference mstorage= FirebaseStorage.getInstance().getReference().child("user_photo");
        final StorageReference imgFilePath=mstorage.child(pickedImgUrl.getLastPathSegment().toString());
        imgFilePath.putFile(pickedImgUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                shortMessage("img succesfully uploaded");
                imgFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        shortMessage("uri downloaded");
                        UserProfileChangeRequest profilechange=new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();

                        currentuser.updateProfile(profilechange).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    shortMessage("Registration Completed");
                                    updateUI();}
                            }
                        });


                    }
                });

            }
        });

    }

    private void updateUI() {
        Intent homeactivity=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(homeactivity);
    }

    private void shortMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }

    private void openGallary() {
        shortMessage("Opening ur gallary");
        Intent gallaryIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(gallaryIntent,"choose ur image"),REQUESTCODE);
//        Intent gallaryIntent=new Intent(Intent.ACTION_GET_CONTENT);
//        gallaryIntent.setType("Image/*");
//        startActivityForResult(Intent.createChooser(gallaryIntent,"choose ur image"),REQUESTCODE);
//
    }

    private void checkAndRequestPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE))
                Toast.makeText(RegAccont.this,"Please Accept permission",Toast.LENGTH_LONG).show();
            else ActivityCompat.requestPermissions(RegAccont.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},FReqCode);
        }
        else openGallary();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null){
            pickedImgUrl=data.getData();
            imgUserPhoto.setImageURI(pickedImgUrl);
            shortMessage(pickedImgUrl.getLastPathSegment()+"is selected");
        }
    }


}

