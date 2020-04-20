package com.kalu.mainactivity.Activties;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.kalu.mainactivity.EventModel;
import com.kalu.mainactivity.R;

public class DisplayActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG ="" ;
    EventModel eventModel;
    DatabaseReference db;
    private String userid;
    GoogleMap map;
    TextView textView, textView2, textView3, textView4, textView5;
    Button buttonlocate, button2route,generatebar;
    ImageView barcode;
    String barcodedata;

    CardView barcodecard,mapCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        textView = findViewById(R.id.editTextname);
        textView2 = findViewById(R.id.editTextdescription);
        textView3 = findViewById(R.id.editTextplace);
        textView4 = findViewById(R.id.textViewtime);
        buttonlocate = findViewById(R.id.buttonlocate);
        mapCard=findViewById(R.id.butc8);
        buttonlocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapCard.setVisibility(View.VISIBLE);
                LatLng bairdar = new LatLng(eventModel.getLatitude(), eventModel.getLongitude());
                map.addMarker(new MarkerOptions().position(bairdar).title(eventModel.getName()));
                map.moveCamera(CameraUpdateFactory.newLatLng(bairdar));
                map.setMinZoomPreference(20);
                // MapStyleOptions mapStyleOptions=MapStyleOptions.loadRawResourceStyle()
            }
        });
        userid = getIntent().getExtras().get("userid").toString();
        db = FirebaseDatabase.getInstance().getReference().child("EventModel").child(userid);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventModel = dataSnapshot.getValue(EventModel.class);
                textView.setText("Name- "+eventModel.getName());
                textView2.setText("Description- "+eventModel.getDescription());
                textView3.setText("Time- "+eventModel.getTime());
                textView4.setText("Place- "+eventModel.getPlace());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        barcode=findViewById(R.id.imageView);
        barcodecard=findViewById(R.id.butc2);
        generatebar=findViewById(R.id.buttonstart);
        generatebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                barcodecard.setVisibility(View.VISIBLE);
                barcodedata=eventModel.getName().toString();

                MultiFormatWriter multiFormatWriter=new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix=multiFormatWriter.encode(barcodedata, BarcodeFormat.QR_CODE, 500,600);
                    BarcodeEncoder barcodeEncoder=new BarcodeEncoder();
                    Bitmap bitmap=barcodeEncoder.createBitmap(bitMatrix);
                    barcode.setImageBitmap(bitmap);

                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
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
        // Position the map's camera near Sydney, Australia.
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(-34, 151)));
       }

//        double elat=0.0;
//         elat=eventModel.getLatitude();
//        double elong=eventModel.getLatitude();
//        String title=eventModel.getName();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.map_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int ID=item.getItemId();
        // if(ID== R.id.Normal_hybrid)

        if(ID==R.id.traffic)
        {if(map.isTrafficEnabled()==true)
            map.setTrafficEnabled(false);
        else  map.setTrafficEnabled(true);}
        return super.onOptionsItemSelected(item);
    }
}
