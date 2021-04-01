package com.example.shopper;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private List<Comment> commentModelList;




    public CommentAdapter(List<Comment> commentModelList) {
        this.commentModelList = commentModelList;
    }


    public List<Comment> getCommentModelList() {
        return commentModelList;
    }

    public void setCommentModelList(List<Comment> commentModelList) {
        this.commentModelList = commentModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_iitem, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        String publisherid = commentModelList.get(position).getPublisher();
        String comment = commentModelList.get(position).getComment();

        viewHolder.setData(comment);



    }


    @Override
    public int getItemCount() {
        return commentModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView comment;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            comment = itemView.findViewById(R.id.comment);

        }


        private void setData(final String commentt) {
            comment.setText(commentt);
        }
    }
}











