package com.kalu.mainactivity.Activties;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kalu.mainactivity.EventModel;
import com.kalu.mainactivity.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "";
    EditText Eventname, Eventplace, Eventtime;
    EditText Eventdescription;
    Double Longtide=1.1111, Latitiude=1.1111;
    ProgressBar progressBar4;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    Button save, locate;
   GoogleMap map;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Eventname = findViewById(R.id.editTextname);
        Eventplace = findViewById(R.id.editTextplace);
        Eventdescription = findViewById(R.id.editTextdescription);
        Eventtime = findViewById(R.id.editTexttiime);
        progressBar4 = findViewById(R.id.buttonNext);
        mAuth=FirebaseAuth.getInstance();

        progressBar4.setVisibility(View.INVISIBLE);
        final SupportMapFragment mapFragment= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.setMenuVisibility(false);
        mapFragment.getMapAsync(this);

        searchView=findViewById(R.id.sv_map);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location=searchView.getQuery().toString();
                List<Address> addressList = null;
                Address address=null;
                if(location!=null && !location.equals("")){
                    Geocoder geocoder=new Geocoder(RegisterActivity.this);
                    try {
                        addressList=geocoder.getFromLocationName(location,1);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(addressList==null){
                        Toast.makeText(getApplicationContext(),"can not find your place,search again",Toast.LENGTH_LONG).show();
                    }else {
                        address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        map.addMarker(new MarkerOptions().position(latLng).title(location));
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                        Latitiude = address.getLatitude();
                        Longtide = address.getLongitude();
                    }

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });



        save = findViewById(R.id.buttonstart);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map=googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.setBuildingsEnabled(true);

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.map_style));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        LatLng bairdar=new LatLng(9.005401, 38.763611);
        map.addMarker(new MarkerOptions().position(bairdar).title("BahirDar"));
        map.moveCamera(CameraUpdateFactory.newLatLng(bairdar));
        map.setMinZoomPreference(20);
        map.setMaxZoomPreference(20);

    }
}
