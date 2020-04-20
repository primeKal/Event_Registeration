package com.kalu.mainactivity.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kalu.mainactivity.Adapters.EventAdapter;
import com.kalu.mainactivity.EventModel;
import com.kalu.mainactivity.R;
import com.kalu.mainactivity.Activties.RegisterActivity;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.kalu.mainactivity.Fragments.ItemFragment.FReqCode;

public class EventsFragment extends Fragment {
    private static final int REQUESTCODE =000 ;
    EventAdapter adapter2;
    DatabaseReference databaseReference;
    FloatingActionButton fab;
    List<EventModel> eventModellist;
    String user;

    Dialog popupAddPost;

    EditText ename,edecription,eplace,etime;
    Button addeventbtn;
    TextView username,timestamp,usernumber;
    Double Longtide=1.1111, Latitiude=1.1111;
    ImageView eventimg,userpic;
    SearchView searchView;
    GoogleMap map;

    private ProgressBar popupaddeventbar;
    Uri pickedImgUrl;


    private RecyclerView recyclerView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
          View view=inflater.inflate(R.layout.fragment_events,container,false);
        FirebaseDatabase k=FirebaseDatabase.getInstance();
        databaseReference =k.getReference().child("EventModel");
        databaseReference.keepSynced(true);
        recyclerView=view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);



        fab=view.findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initipopup();


            }
        });


        return view;
    }

    private void initipopup() {
        popupAddPost = new Dialog(getActivity());
        popupAddPost.setContentView(R.layout.popup_register_event);
        popupAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popupAddPost.getWindow().getAttributes().gravity = Gravity.TOP;
        // Toast.makeText(getApplicationContext(),"successfull", Toast.LENGTH_LONG).show();
        popupAddPost.show();

        popupaddeventbar=popupAddPost.findViewById(R.id.progressBar);
        popupaddeventbar.setVisibility(View.INVISIBLE);

        ename=popupAddPost.findViewById(R.id.eventname);
        edecription=popupAddPost.findViewById(R.id.eventDescription);
        etime=popupAddPost.findViewById(R.id.eventTime);
        eplace=popupAddPost.findViewById(R.id.eventPlace);

        username=popupAddPost.findViewById(R.id.eventregusername);
        usernumber=popupAddPost.findViewById(R.id.eventregusernumber);

        eventimg=popupAddPost.findViewById(R.id.eventpic);
        userpic=popupAddPost.findViewById(R.id.eventreguserpic);


        final FirebaseUser currentuser=FirebaseAuth.getInstance().getCurrentUser();
        username.setText(currentuser.getDisplayName());
        usernumber.setText(currentuser.getEmail());
        Glide.with(getActivity()).load(currentuser.getPhotoUrl()).into(userpic);

        eventimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>22)
                    checkAndRequestPermission();
                else {openGallary();}
            }
        });

        searchView=popupAddPost.findViewById(R.id.searchView2);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                String location=searchView.getQuery().toString();
                List<Address> addressList = null;
                Address address=null;
                if(location!=null && !location.equals("")){
                    Geocoder geocoder=new Geocoder(getContext());
                    try {
                        addressList=geocoder.getFromLocationName(location,1);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(addressList==null){
                        Toast.makeText(getContext(),"can not find your place,search again",Toast.LENGTH_LONG).show();
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


        addeventbtn=popupAddPost.findViewById(R.id.button);
        addeventbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addeventbtn.setVisibility(View.INVISIBLE);
                popupaddeventbar.setVisibility(View.VISIBLE);
                final String name = ename.getText().toString();
                final String place = eplace.getText().toString();
                final String time = etime.getText().toString();
                final String descri = edecription.getText().toString();
                                if (!name.isEmpty() && (!place.isEmpty()) && (!time.isEmpty()) && (!descri.isEmpty()) && pickedImgUrl != null) {
                                                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Event_Images");
                                              final StorageReference imgfilepath = storageReference.child(pickedImgUrl.getLastPathSegment());
                                               imgfilepath.putFile(pickedImgUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                               imgfilepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imgdownloadlink = uri.toString();
                                    EventModel eventModel = new EventModel(descri, Latitiude, Longtide, name, place, time,currentuser.getPhotoUrl().toString(),imgdownloadlink);

                                    addPost(eventModel);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    addeventbtn.setVisibility(View.VISIBLE);
                                    popupaddeventbar.setVisibility(View.INVISIBLE);

                                }
                            });
                        }
                    });

                } else {

                    addeventbtn.setVisibility(View.VISIBLE);
                    popupaddeventbar.setVisibility(View.INVISIBLE);
                }

        }

        });}


        private void addPost(EventModel lo) {
            FirebaseDatabase database=FirebaseDatabase.getInstance();
            DatabaseReference reference=database.getReference("Events").push();
            String key=reference.getKey();
            lo.setEventkey(key);
            reference.setValue(lo).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    shortMessage("Post Added Successfully");
                    addeventbtn.setVisibility(View.VISIBLE);
                    popupaddeventbar.setVisibility(View.INVISIBLE);
                    popupAddPost.dismiss();
                }
            });
        }




        @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(getContext(),"for loop",Toast.LENGTH_LONG).show();
                eventModellist=new ArrayList<>();
                for(DataSnapshot postsnap:dataSnapshot.getChildren()){
                    EventModel post=postsnap.getValue(EventModel.class);
                    eventModellist.add(post);
                    //    Toast.makeText(getContext(),postList.size(),Toast.LENGTH_LONG).show();
                }
                adapter2=new EventAdapter(getActivity(),eventModellist);
                recyclerView.setAdapter(adapter2);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
    private void shortMessage(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
    }




    private void openGallary() {
        shortMessage("Opening ur gallary");
        Intent gallaryIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(gallaryIntent,"choose ur image"),REQUESTCODE);
    }

    private void checkAndRequestPermission() {
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE))
                Toast.makeText(getContext(),"Please Accept permission",Toast.LENGTH_LONG).show();
            else ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},FReqCode);
        }
        else openGallary();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null) {
            pickedImgUrl = data.getData();
            eventimg.setImageURI(pickedImgUrl);
            shortMessage(pickedImgUrl.getLastPathSegment() + "is selected");
        }
    }




}
