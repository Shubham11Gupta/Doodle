package com.example.vchat.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vchat.Models.users;
import com.example.vchat.R;
import com.example.vchat.chatDetailActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class usersadapter extends RecyclerView.Adapter<usersadapter.ViewHolder> {
    ArrayList<users> list;
    Context context;
    public usersadapter(ArrayList<users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sampleuser,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        users user=list.get(position);
        Picasso.get().load(user.getProfilepicture()).placeholder(R.drawable.ic_user).into(holder.image);
        holder.username.setText(user.getUsername());
        holder.lastmessage.setText(user.getStatus());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, chatDetailActivity.class);
                intent.putExtra("userid",user.getUserid());
                intent.putExtra("profilePic",user.getProfilepicture());
                intent.putExtra("userName",user.getUsername());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView username,lastmessage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.profileshow);
            username=itemView.findViewById(R.id.usernameList);
            lastmessage=itemView.findViewById(R.id.status);
        }
    }
}
