package com.kalu.mainactivity.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.kalu.mainactivity.Adapters.ItemAdapter;
import com.kalu.mainactivity.ItemModel;
import com.kalu.mainactivity.R;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUESTCODE = 000;
    static int FReqCode=1;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    ItemAdapter adapter2;
    DatabaseReference databaseReference;
    FloatingActionButton floatingActionButton;
    List<ItemModel> itemModellist;
    String user;
    FloatingActionButton fab;
    private RecyclerView recyclerView;
    Dialog popupAddPost=null;
    FirebaseUser mAuth;
    private ProgressBar popupCLickprogress;
    Uri pickedImgUrl;

    ImageView itemuserphoto,itemimg;
    EditText itemname;
    EditText itemprice;
    EditText itemquantity;
    EditText itemtoken;
    TextView username;
    TextView usernumber;

    Button additem;


    public ItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemFragment newInstance(String param1, String param2) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View itemfrag=inflater.inflate(R.layout.fragment_item, container, false);
        recyclerView=itemfrag.findViewById(R.id.itemviewrv);
        databaseReference= FirebaseDatabase.getInstance().getReference("Item");
        databaseReference.keepSynced(true);
        mAuth=FirebaseAuth.getInstance().getCurrentUser();
        LinearLayoutManager k=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(k);
        recyclerView.setHasFixedSize(true);

        fab=itemfrag.findViewById(R.id.itemfragfab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inipopup();
            }
        });
        return itemfrag;
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(getContext(),"for loop",Toast.LENGTH_LONG).show();
                itemModellist=new ArrayList<>();
                for(DataSnapshot postsnap:dataSnapshot.getChildren()){
                    ItemModel post=postsnap.getValue(ItemModel.class);
                    shortMessage(post.getName());
                    itemModellist.add(post);
                }
                adapter2=new ItemAdapter(getActivity(),itemModellist);
                recyclerView.setAdapter(adapter2);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    private void inipopup() {
        popupAddPost = new Dialog(getActivity());
        popupAddPost.setContentView(R.layout.popup_register_item);
        popupAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popupAddPost.getWindow().getAttributes().gravity = Gravity.TOP;
        // Toast.makeText(getApplicationContext(),"successfull", Toast.LENGTH_LONG).show();
        popupAddPost.show();
        itemimg = popupAddPost.findViewById(R.id.itemregimg);
        itemimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>22)
                    checkAndRequestPermission();
                else {openGallary();}
            }
        });


        popupCLickprogress = popupAddPost.findViewById(R.id.progressBarloaditem);
        popupCLickprogress.setVisibility(View.INVISIBLE);
        itemuserphoto = popupAddPost.findViewById(R.id.itemreguserpic);
        itemname = popupAddPost.findViewById(R.id.itemregname);
        itemprice = popupAddPost.findViewById(R.id.itemregprice);
        itemquantity = popupAddPost.findViewById(R.id.itemregquantity);
        itemtoken = popupAddPost.findViewById(R.id.itemregtoken);
        username = popupAddPost.findViewById(R.id.itemregusername);
        usernumber = popupAddPost.findViewById(R.id.itemregusernumber);
        username.setText(mAuth.getDisplayName());
        usernumber.setText(mAuth.getEmail());
        Glide.with(getActivity()).load(mAuth.getPhotoUrl()).into(itemuserphoto);

        final String strprice=itemprice.getText().toString();
        final String strquan=itemquantity.getText().toString();
        final String strname=itemname.getText().toString();
        final String token = String.valueOf(itemtoken.getText());
shortMessage(strprice+strname);



        additem = popupAddPost.findViewById(R.id.itemregaddbtn);
        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupCLickprogress.setVisibility(View.VISIBLE);
                additem.setVisibility(View.INVISIBLE);
                if (!itemname.getText().toString().isEmpty() && (!itemprice.getText().toString().isEmpty()) && (!itemquantity.getText().toString().isEmpty()) && pickedImgUrl != null) {
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Item_Images");
                    final StorageReference imgfilepath = storageReference.child(pickedImgUrl.getLastPathSegment());
                    imgfilepath.putFile(pickedImgUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imgfilepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imgdownloadlink = uri.toString();
                                    ItemModel post = new ItemModel(imgdownloadlink.toString(),strprice,strquan,
                                            strname,token,
                                            mAuth.getUid().toString(),
                                            mAuth.getPhotoUrl().toString(),
                                            mAuth.getDisplayName().toString());
                                    addPost(post);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    shortMessage(e.getMessage());
                                    popupCLickprogress.setVisibility(View.INVISIBLE);
                                    additem.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    });

                } else {
                    popupCLickprogress.setVisibility(View.INVISIBLE);
                    additem.setVisibility(View.VISIBLE);
                    shortMessage("Please Verify All Fields");
                }

            }
        });

    }


    private void addPost(ItemModel post) {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference("Item").push();

        String key=reference.getKey();
        post.setItemKey(key);
        reference.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                shortMessage("Post Added Successfully");
                popupCLickprogress.setVisibility(View.INVISIBLE);
                additem.setVisibility(View.VISIBLE);
                popupAddPost.dismiss();
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
            itemimg.setImageURI(pickedImgUrl);
            shortMessage(pickedImgUrl.getLastPathSegment() + "is selected");
        }
    }
}
