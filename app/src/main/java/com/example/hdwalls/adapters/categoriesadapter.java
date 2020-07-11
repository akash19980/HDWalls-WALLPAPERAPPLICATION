package com.example.hdwalls.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hdwalls.R;
import com.example.hdwalls.activities.WallpapersActivity;
import com.example.hdwalls.models.Category;

import java.util.List;

public class categoriesadapter extends RecyclerView.Adapter<categoriesadapter.categoryViewholder> {
    private Context mCtx;
    private List<Category> categoryList;

    public categoriesadapter(Context mCtx, List<Category> categoryList) {
        this.mCtx = mCtx;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public categoryViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_category,viewGroup,false);
        return new categoryViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull categoryViewholder categoryViewholder, int i) {
         Category c= categoryList.get(i);
         categoryViewholder.textView.setText(c.name);
         Glide.with(mCtx).load(c.thumb)
         .into(categoryViewholder.imageView);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class categoryViewholder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        TextView textView;
        ImageView imageView;

        public categoryViewholder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.text_view_cat_name);
            imageView=itemView.findViewById(R.id.image_view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int p=getAdapterPosition();
            Category c = categoryList.get(p);
            Intent intent= new Intent(mCtx, WallpapersActivity.class);
            intent.putExtra("category",c.name);
            mCtx.startActivity(intent);
        }
    }
}
