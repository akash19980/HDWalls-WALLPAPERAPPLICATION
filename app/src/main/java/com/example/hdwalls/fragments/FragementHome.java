package com.example.hdwalls.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ProgressBar;

import com.example.hdwalls.R;
import com.example.hdwalls.adapters.categoriesadapter;
import com.example.hdwalls.models.Category;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragementHome extends Fragment {
    ProgressBar progressBar;

    private List<Category> categoryList;
    private DatabaseReference databaseReference;
   private RecyclerView recyclerView;
   private categoriesadapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar=view.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        categoryList= new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference("categories");
        recyclerView=view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter=new categoriesadapter(getActivity(),categoryList);
        recyclerView.setAdapter(adapter);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             if(dataSnapshot.exists()) {
                 progressBar.setVisibility(View.GONE);
                 for (DataSnapshot ds : dataSnapshot.getChildren()) {
                     String name = ds.getKey();
                     String desc = ds.child("desc").getValue(String.class);
                     String thumb = ds.child("thumbnail").getValue(String.class);
                     Category c = new Category(name, desc, thumb);
                     categoryList.add(c);

                 }
                 adapter.notifyDataSetChanged();
             }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
