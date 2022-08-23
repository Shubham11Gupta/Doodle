package com.example.vchat.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vchat.Models.messagesModel;
import com.example.vchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class chatAdapter extends RecyclerView.Adapter{
    ArrayList<messagesModel> messagesM;
    Context context;
    String reciverID;
    int senderViewType=1;
    int reciverViewType=2;

    public chatAdapter(ArrayList<messagesModel> messages, Context context) {
        this.messagesM = messages;
        this.context = context;
    }

    public chatAdapter(ArrayList<messagesModel> messagesM, Context context, String reciverID) {
        this.messagesM = messagesM;
        this.context = context;
        this.reciverID = reciverID;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==senderViewType){
            View view= LayoutInflater.from(context).inflate(R.layout.samplesender,parent,false);
            return new senderViewHolder(view);
        }
        else {
            View view= LayoutInflater.from(context).inflate(R.layout.samplereciver,parent,false);
            return new reviverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(messagesM.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())){
            return senderViewType;
        }
        else{
            return reciverViewType;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        messagesModel messages=messagesM.get(position);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete Message")
                        .setMessage("Want to delete message?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseDatabase database=FirebaseDatabase.getInstance();
                                String senderroom=FirebaseAuth.getInstance().getUid()+reciverID;
                                database.getReference().child("CHATS").child(senderroom)
                                        .child(messages.getMessageid())
                                        .setValue(null);
                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
                return false;
            }
        });
        if(holder.getClass()==senderViewHolder.class){
            ((senderViewHolder)holder).senderMsg.setText(messages.getMessage());
        }
        else{
            ((reviverViewHolder)holder).reciverMsg.setText(messages.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messagesM.size();
    }

    public class reviverViewHolder extends RecyclerView.ViewHolder {
        TextView reciverMsg,reciverTime;
        public reviverViewHolder(@NonNull View itemView) {
            super(itemView);
            reciverMsg=itemView.findViewById(R.id.recivertext);
        }
    }
    public class senderViewHolder extends RecyclerView.ViewHolder {
        TextView senderMsg,senderTime;
        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg=itemView.findViewById(R.id.senderText);
        }
    }
}
