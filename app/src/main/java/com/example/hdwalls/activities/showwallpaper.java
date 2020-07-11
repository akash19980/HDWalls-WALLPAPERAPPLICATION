package com.example.hdwalls.activities;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.audiofx.EnvironmentalReverb;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.hdwalls.R;
import com.example.hdwalls.models.Wallpaper;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class showwallpaper extends AppCompatActivity {
    ImageView imageview;
    CheckBox favourite;
    ImageButton share, download;
    String category;
    Button setaswallpaper;
    String imageurl;
    int position;
    ArrayList<Wallpaper> list=new ArrayList<Wallpaper>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showwallpaper);
        getIncomingintent();

      setaswallpaper= findViewById(R.id.setas_wallpaper);

setaswallpaper.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Bitmap bitmapImg = ((BitmapDrawable) imageview.getDrawable()).getBitmap();
        WallpaperManager wallpaperManager=WallpaperManager.getInstance(getApplicationContext());
        try {
            wallpaperManager.clear();
            wallpaperManager.setBitmap(bitmapImg);


        } catch (IOException ex) {

        }
        Toast.makeText(showwallpaper.this, "Setting as wallpaper", Toast.LENGTH_SHORT).show();
    }
});
      }
      private void getIncomingintent()
      {
          if(getIntent().hasExtra("image_url"))
          {
              position= Integer.parseInt(getIntent().getStringExtra("position"));
              category=getIntent().getStringExtra("category");
              imageview= findViewById(R.id.finalwallpaper);
               imageurl=getIntent().getStringExtra("image_url");
              Glide.with(this)
                      .asBitmap().load(imageurl).into(imageview);
          Toast.makeText(this, "Available"+category +""+position, Toast.LENGTH_LONG).show();
      }}



    }







