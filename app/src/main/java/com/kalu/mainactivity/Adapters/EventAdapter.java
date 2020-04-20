package com.kalu.mainactivity.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;
import com.kalu.mainactivity.Activties.DisplayActivity;
import com.kalu.mainactivity.EventModel;
import com.kalu.mainactivity.R;

import java.util.List;

public class EventAdapter  extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {
    Context mcontext;
    List<EventModel> mData;


    public EventAdapter(Context mcontext, List<EventModel> mData) {
        this.mcontext = mcontext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.holder_view,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.mTitle.setText(mData.get(position).getName().toString());
        holder.mplace.setText(mData.get(position).getPlace().toString());
        Glide.with(mcontext).load(mData.get(position).getZuserPhoto()).into(holder.imguser);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myRef= FirebaseDatabase.getInstance().getReference().child("EventsModel").child(mData.get(position).getName()).getKey();
                Toast.makeText(mcontext,"Truely YOurs",Toast.LENGTH_SHORT);
                Intent intent=new Intent(mcontext, DisplayActivity.class);
                intent.putExtra("userid",myRef);
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mData == null)return 0;
        else return mData.size();
    }


    public class  MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        TextView mplace;
        ImageView imguser;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle=itemView.findViewById(R.id.textView);
            mplace=itemView.findViewById(R.id.textView2);
            imguser=itemView.findViewById(R.id.imageView2);
        }
    }
}
