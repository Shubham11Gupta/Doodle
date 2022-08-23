package com.example.vchat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.vchat.Models.messagesModel;
import com.example.vchat.adapters.chatAdapter;
import com.example.vchat.databinding.ActivityChatDetailBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class chatDetailActivity extends AppCompatActivity {
    ActivityChatDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        final String senderID= auth.getUid();
        String reciveID=getIntent().getStringExtra("userid");
        String username=getIntent().getStringExtra("userName");
        String profilePic=getIntent().getStringExtra("profilePic");
        binding.username.setText(username);
        Picasso.get().load(profilePic).placeholder(R.drawable.ic_user).into(binding.profileImage);
        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(chatDetailActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        final ArrayList<messagesModel> messages=new ArrayList<>();
        final chatAdapter chatAdapter=new chatAdapter(messages,this,reciveID);
        binding.chatrecyclerview.setAdapter(chatAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        binding.chatrecyclerview.setLayoutManager(linearLayoutManager);
        final String senderroom=senderID+reciveID;
        final String reciverroom=reciveID+senderID;
        database.getReference().child("CHATS").
                child(senderroom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for(DataSnapshot dataSnapshot1:snapshot.getChildren()){
                    messagesModel model=dataSnapshot1.getValue(messagesModel.class);
                    model.setMessageid(dataSnapshot1.getKey());
                    messages.add(model);
                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=binding.etMessage.getText().toString();
                final messagesModel messagesModel=new messagesModel(senderID,text);
                messagesModel.setTimestamp(new Date().getTime());
                binding.etMessage.setText("");
                database.getReference().child("CHATS").child(senderroom).push()
                        .setValue(messagesModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        database.getReference().child("CHATS").child(reciverroom).push()
                                .setValue(messagesModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                    }
                });
            }
        });
    }
}