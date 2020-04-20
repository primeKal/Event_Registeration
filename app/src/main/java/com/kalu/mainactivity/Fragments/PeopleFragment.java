package com.kalu.mainactivity.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kalu.mainactivity.EventModel;
import com.kalu.mainactivity.R;

import java.util.ArrayList;

public class PeopleFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "";
    TextView textView;
    int zz=0;
    DatabaseReference db;
    ArrayList<EventModel> eventModelArrayList=new ArrayList<>();
    Button button5,button6;
    GoogleMap map;
    View mapinclview;
    MapView mapView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
     mapinclview=inflater.inflate(R.layout.fragment_people,container,false);
     mapView=mapinclview.findViewById(R.id.map2);
        button5=mapinclview.findViewById(R.id.buttonstart);
        button6=mapinclview.findViewById(R.id.buttonlocate);
        db = FirebaseDatabase.getInstance().getReference().child("EventModel");
        db.keepSynced(true);
        return mapinclview;
    }

    @Override
    public void onStart() {
        super.onStart();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.e("Count " ,""+dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    EventModel post = postSnapshot.getValue(EventModel.class);
                    Log.e("Get Data", post.getName());
                    eventModelArrayList.add(post);
                    //  Toast.makeText(getApplicationContext(),post.getDescription()+"Loved"+eventModelArrayList.size(),Toast.LENGTH_LONG).show();
                }}


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        map=googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        CameraPosition li=CameraPosition.builder().target(new LatLng(40.68397,-74.044507)).zoom(16).bearing(0).tilt(45).build();
    }
}
