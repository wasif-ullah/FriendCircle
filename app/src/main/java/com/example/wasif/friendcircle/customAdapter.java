package com.example.wasif.friendcircle;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Wasif on 4/12/2018.
 */

public class customAdapter extends RecyclerView.Adapter<customAdapter.myviewholder> {

   private Context context;
   private List<Integer>images;


    public customAdapter(Context context, List<Integer>images){

        this.context = context;
        this.images = images;

    }
    @Override
    public myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.customview,parent,false);
        return new  myviewholder(view);
    }

    @Override
    public void onBindViewHolder(myviewholder holder, int position) {
        Glide.with(context)
                .load(images.get(position))
                .into(holder.imageView);

      //  holder.imageView.setImageResource((Integer)images.get(position));

    }

    @Override
    public int getItemCount() {

        return images.size();

    }
    class myviewholder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;

        public myviewholder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.iv);
            //textView = itemView.findViewById(R.id.tv);
        }
    }
}
