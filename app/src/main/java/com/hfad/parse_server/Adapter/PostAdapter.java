package com.hfad.parse_server.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hfad.parse_server.Model.Post;
import com.hfad.parse_server.R;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ProductViewHolder> {

    private Context mCtx;
    private List<Post> postList;

    public PostAdapter(Context mCtx, List<Post> postList) {
        this.mCtx = mCtx;
        this.postList = postList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mCtx);
        View view = layoutInflater.inflate(R.layout.list_layout, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Post post = postList.get(position);

        holder.textViewUsername.setText(post.getUsername());
        holder.imageView.setImageBitmap(post.getImage());
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textViewUsername;

        public ProductViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.post_image);
            textViewUsername = itemView.findViewById(R.id.post_username);
        }
    }
}
