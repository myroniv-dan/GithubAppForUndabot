package com.example.bogdan.githubapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

class ReposAdapter extends RecyclerView.Adapter<ReposAdapter.ViewHolder>{

    private Context context;
    private List<ReposData> dataList;

    public ReposAdapter(Context context, List<ReposData> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvpart,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.tvRepoName.setText(dataList.get(position).getRepoName());
        holder.tvAuthorName.setText("Author: " + dataList.get(position).getAuthorName());
        holder.tvWatches.setText("Watches: " + dataList.get(position).getNumOfWatches());
        holder.tvForks.setText("Forks: " + dataList.get(position).getNumOfForks());
        holder.tvIssues.setText("Issues: " + dataList.get(position).getNumOfIssues());

        Picasso.get().load(dataList.get(position).getImgLink()).into(holder.imageView);

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RepoDetailsActivity.class);
                intent.putExtra("id", dataList.get(position).getId());
                intent.putExtra("login", dataList.get(position).getAuthorName());

                holder.imageView.buildDrawingCache();
                Bitmap image = holder.imageView.getDrawingCache();
                Bundle extras = new Bundle();
                extras.putParcelable("imagebitmap", image);
                intent.putExtras(extras);

                context.startActivity(intent);
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, UserDetailsActivity.class);
                intent.putExtra("login", dataList.get(position).getAuthorName());

                holder.imageView.buildDrawingCache();
                Bitmap image = holder.imageView.getDrawingCache();
                Bundle extras = new Bundle();
                extras.putParcelable("imagebitmap", image);
                intent.putExtras(extras);

                //intent.putExtra("avatar_url", dataList.get(position).getImgLink());

                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvRepoName, tvAuthorName, tvWatches, tvForks, tvIssues;
        private ImageView imageView;
        private ConstraintLayout constraintLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            tvRepoName = itemView.findViewById(R.id.tvRepoName);
            tvAuthorName = itemView.findViewById(R.id.tvAuthorName);
            tvWatches = itemView.findViewById(R.id.tvWatches);
            tvForks = itemView.findViewById(R.id.tvForks);
            tvIssues = itemView.findViewById(R.id.tvIssues);

            imageView = itemView.findViewById(R.id.imageViewImg);

            constraintLayout = itemView.findViewById(R.id.constraintLayout);
        }
    }
}
