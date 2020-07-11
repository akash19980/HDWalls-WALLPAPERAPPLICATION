package com.example.hdwalls.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hdwalls.R;
import com.example.hdwalls.activities.showwallpaper;
import com.example.hdwalls.models.Category;
import com.example.hdwalls.models.Wallpaper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class wallpapersadapter extends RecyclerView.Adapter<wallpapersadapter.wallpaperViewholder> {
    private Context mCtx;
    private List<Wallpaper> wallpaperList;
    int position=0;

    public wallpapersadapter(Context mCtx, List<Wallpaper> wallpaperList) {
        this.mCtx = mCtx;
        this.wallpaperList = wallpaperList;

    }

    @NonNull
    @Override
    public wallpaperViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_wallpapers,viewGroup,false);
        return new wallpaperViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull wallpaperViewholder categoryViewholder, int i) {
        Wallpaper  w= wallpaperList.get(i);

         categoryViewholder.textView.setText(w.title);
         Glide.with(mCtx).load(w.url)
         .into(categoryViewholder.imageView);

         if(w.isFavourite)
         {
             categoryViewholder.checkBox.setChecked(true);
         }

    }

    @Override
    public int getItemCount() {
        return wallpaperList.size();
    }

    class wallpaperViewholder extends RecyclerView.ViewHolder implements  View.OnClickListener , CompoundButton.OnCheckedChangeListener {

        TextView textView;
        ImageView imageView;
        CheckBox checkBox;

        public wallpaperViewholder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.text_view_title);
            imageView=itemView.findViewById(R.id.image_view);
            checkBox=itemView.findViewById(R.id.checkbox_favourite);


            checkBox.setOnCheckedChangeListener(this);


            imageView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {


            Toast.makeText(mCtx, "Clicked", Toast.LENGTH_SHORT).show();
            int p=getAdapterPosition();
            Wallpaper w1= wallpaperList.get(p);
            Intent intent= new Intent(mCtx,showwallpaper.class);
            intent.putExtra("image_url",w1.url);
            intent.putExtra("position",String.valueOf(p));

            mCtx.startActivity(intent);

        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(FirebaseAuth.getInstance().getCurrentUser()==null)
            {
                Toast.makeText(mCtx, "Please Login First", Toast.LENGTH_SHORT).show();
                buttonView.setChecked(false);
                return;
            }

            int p=getAdapterPosition();
            Wallpaper w= wallpaperList.get(p);
            DatabaseReference dbreference= FirebaseDatabase.getInstance().getReference("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("favourites").child(w.category);
            if(isChecked)
            {
                dbreference.child(w.id).setValue(w);
            }
            else
            {
                dbreference.child(w.id).setValue(null);
            }

        }
    }
}
