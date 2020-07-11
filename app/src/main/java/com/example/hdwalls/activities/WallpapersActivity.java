package com.example.hdwalls.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


import com.example.hdwalls.R;
import com.example.hdwalls.adapters.wallpapersadapter;
import com.example.hdwalls.models.Wallpaper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WallpapersActivity extends AppCompatActivity {


    List<Wallpaper> wallpaperList;
    List<Wallpaper> favlist;
    RecyclerView recyclerView;
    wallpapersadapter adapter;
    DatabaseReference dbWallpapers,dbfavs;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpapers);
        Intent intent= getIntent();
        final String category= intent.getStringExtra("category");
        wallpaperList=new ArrayList<>();
        favlist=new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        adapter=new wallpapersadapter(this,wallpaperList);
        progressBar=findViewById(R.id.progressbar);
recyclerView.setAdapter(adapter);
dbWallpapers= FirebaseDatabase.getInstance().getReference("images").child(category);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            dbfavs= FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
            .child("favourites")
            .child(category);
            fetchFavWallpapers(category);


        }
        else
        {
            fetchWallpapers(category);
        }



    }

    private void fetchFavWallpapers(final String category)
    {
        progressBar.setVisibility(View.VISIBLE);
        dbfavs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot wallpapersSnapshot:dataSnapshot.getChildren())
                    {
                        String id= wallpapersSnapshot.getKey();
                        String title= wallpapersSnapshot.child("title").getValue(String.class);
                        String desc= wallpapersSnapshot.child("desc").getValue(String.class);
                        String url= wallpapersSnapshot.child("url").getValue(String.class);

                        Wallpaper w= new Wallpaper(id,title,desc,url,category);
                        favlist.add(w);

                    }

                }
                fetchWallpapers(category);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void fetchWallpapers(final String category)
    {
        progressBar.setVisibility(View.VISIBLE);
        dbWallpapers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot wallpapersSnapshot:dataSnapshot.getChildren())
                    {
                        String id= wallpapersSnapshot.getKey();
                        String title= wallpapersSnapshot.child("title").getValue(String.class);
                        String desc= wallpapersSnapshot.child("desc").getValue(String.class);
                        String url= wallpapersSnapshot.child("url").getValue(String.class);

                        Wallpaper w= new Wallpaper(id,title,desc,url,category);


                        if(isFavourite(w))
                        {
                            w.isFavourite=true;
                        }
                        wallpaperList.add(w);

                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private boolean isFavourite(Wallpaper w)
    {
        for(Wallpaper f:favlist)
        {
            if(f.id.equals(w.id))
            {
                return true;
            }
        }
        return false;
    }

}