package com.kalu.mainactivity.Adapters;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kalu.mainactivity.ItemModel;
import com.kalu.mainactivity.R;

import java.util.List;
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyItemHolder> {
    Context mcontext;
    List<ItemModel> mData;

    public ItemAdapter(Context activity, List<ItemModel> itemModellist) {
        this.mcontext=activity;
        this.mData=itemModellist;

    }

    @NonNull
    @Override
    public ItemAdapter.MyItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.item_holder,parent,false);
        ItemAdapter.MyItemHolder myViewHolder=new ItemAdapter.MyItemHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyItemHolder holder, int position) {
        Toast.makeText(mcontext,"Love"+mData.get(position).getQuantity(),Toast.LENGTH_LONG).show();
//        holder.itemname.setText(mData.get(position).getName());
        holder.itemprice.setText(mData.get(position).getPrice().toString());
        holder.itemquantity.setText(mData.get(position).getQuantity().toString());
        holder.username.setText(mData.get(position).getUsername().toString());
        holder.usernumber.setText(mData.get(position).getUsername().toString());
        holder.itemtoken.setText(mData.get(position).getToken());
        Glide.with(mcontext).load(mData.get(position).getUserimg()).into(holder.userimg);
        Glide.with(mcontext).load(mData.get(position).getItemimage()).into(holder.itemimg);
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyItemHolder extends RecyclerView.ViewHolder {
        TextView username,usernumber,itemtimestamp,itemtoken;
        TextView itemname,itemprice,itemquantity;
        ImageView userimg,itemimg;
        public MyItemHolder(@NonNull View itemView) {
            super(itemView);
            userimg=itemView.findViewById(R.id.itemuserimg);
            itemimg=itemView.findViewById(R.id.itemimg);

            username=itemView.findViewById(R.id.itemusername);
            usernumber=itemView.findViewById(R.id.itemusernumber);
            itemtimestamp=itemView.findViewById(R.id.itemtimestamp);

            itemtoken=itemView.findViewById(R.id.itemtoken);
            itemquantity=itemView.findViewById(R.id.itemquantity);
            itemname=itemView.findViewById(R.id.itemname);
            itemprice=itemView.findViewById(R.id.itemprice);



        }
    }
}
