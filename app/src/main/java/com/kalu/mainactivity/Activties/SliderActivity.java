package com.kalu.mainactivity.Activties;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kalu.mainactivity.Adapters.SlideAdapter;
import com.kalu.mainactivity.R;

public class SliderActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private SlideAdapter slideAdapter;
    FloatingActionButton fab;
    FirebaseAuth mAuth;
    Intent traveller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_slider);
        mAuth=FirebaseAuth.getInstance();
        fab=findViewById(R.id.floatingActionButton);

        getSupportActionBar().hide();
         viewPager=(ViewPager)findViewById(R.id.viewpager);
        slideAdapter=new SlideAdapter(this);
        viewPager.setAdapter(slideAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                traveller=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(traveller);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user=mAuth.getCurrentUser();
        if(user!=null)
            updateUi();
    }
    private void updateUi() {
         traveller=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(traveller);
        finish();
    }
}
