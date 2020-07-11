package com.example.hdwalls.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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

public class FavouritesFragment extends Fragment {

List<Wallpaper> favwalls;
RecyclerView recyclerView;
ProgressBar progressBar;
wallpapersadapter wallpapersadapter;
DatabaseReference dbfav;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourites,container,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        favwalls= new ArrayList<>();
        recyclerView=view.findViewById(R.id.recyclerview);
        progressBar= view.findViewById(R.id.progressbar);
        wallpapersadapter= new wallpapersadapter(getActivity(),favwalls);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.setAdapter(wallpapersadapter);

        if(FirebaseAuth.getInstance().getCurrentUser()==null)
        {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_area,new SettingsFragment()).commit();
            return;
        }

        dbfav= FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("favourites");
        progressBar.setVisibility(View.VISIBLE);
        dbfav.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.INVISIBLE);
                for(DataSnapshot category:dataSnapshot.getChildren())
                {
                    for(DataSnapshot wallpapersSnapshot:category.getChildren())
                    {
                        String id= wallpapersSnapshot.getKey();
                        String title= wallpapersSnapshot.child("title").getValue(String.class);
                        String desc= wallpapersSnapshot.child("desc").getValue(String.class);
                        String url= wallpapersSnapshot.child("url").getValue(String.class);

                        Wallpaper w= new Wallpaper(id,title,desc,url,category.getKey());
w.isFavourite=true;
favwalls.add(w);

                        }
                }
                wallpapersadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
