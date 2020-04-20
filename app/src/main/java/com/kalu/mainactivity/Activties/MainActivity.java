package com.kalu.mainactivity.Activties;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kalu.mainactivity.Fragments.AccsettFragment;
import com.kalu.mainactivity.Fragments.EventsFragment;
import com.kalu.mainactivity.Fragments.ItemFragment;
import com.kalu.mainactivity.Fragments.PeopleFragment;
import com.kalu.mainactivity.Fragments.ScanqrFragment;
import com.kalu.mainactivity.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView navigationView;

    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    FloatingActionButton floatingActionButton;
    private DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(getApplicationContext()," Connected",Toast.LENGTH_SHORT);
      androidx.appcompat.widget.Toolbar toolbar= findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
      drawer=findViewById(R.id.drawer_layout);
        floatingActionButton=(FloatingActionButton)findViewById(R.id.floatingActionButtonpopup);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });


        navigationView=findViewById(R.id.nav_view);
      navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(MainActivity.this,drawer, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragement_container, new EventsFragment()).commit();
            navigationView.setCheckedItem(R.id.events);
        }
        updateNavHeader();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.events:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragement_container, new EventsFragment()).commit();
                break;
            case R.id.accsett:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragement_container, new AccsettFragment()).commit();
                break;
            case R.id.people:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragement_container, new PeopleFragment()).commit();
                break;
            case R.id.scan:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragement_container, new ScanqrFragment()).commit();
                break;
            case R.id.contact:
                FirebaseAuth.getInstance().signOut();
                Intent lognActivity=new Intent(getApplicationContext(),SliderActivity.class);
                startActivity(lognActivity);
                finish();
                break;
            case R.id.items:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragement_container, new ItemFragment()).commit();
                break;
            default:
                Toast.makeText(getApplicationContext(), "This happened", Toast.LENGTH_LONG).show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
                return true;

    }
    public void updateNavHeader(){
        navigationView = findViewById(R.id.nav_view);
        View headerview=navigationView.getHeaderView(0);
        TextView navName=headerview.findViewById(R.id.username);
        TextView navMail=headerview.findViewById(R.id.usermail);
        ImageView navPhoto=headerview.findViewById(R.id.userimg);

        String name= currentUser.getDisplayName();
        String email= currentUser.getEmail();
        Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),email, Toast.LENGTH_LONG).show();

        navName.setText("Welcome, "+name);
        navMail.setText(name);
        Glide.with(this).load(currentUser.getPhotoUrl()).into(navPhoto);
    }
    @Override
    public void onBackPressed() {

        if(drawer.isDrawerOpen(GravityCompat.START))
             {drawer.closeDrawer(GravityCompat.START);}
        else
            super.onBackPressed();
    }
}

