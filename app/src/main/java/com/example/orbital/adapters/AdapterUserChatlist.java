package com.example.orbital.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orbital.ChatActivity;
import com.example.orbital.R;
import com.example.orbital.ThereProfileActivity;
import com.example.orbital.models.ModelChat;
import com.example.orbital.models.ModelUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;


public class AdapterUserChatlist extends RecyclerView.Adapter<AdapterUserChatlist.MyHolder> {


    Context context;
    List<ModelUser> userList;

    String theLastMessage;

    //Constructor for chat list adapter
    public AdapterUserChatlist(Context context, List<ModelUser> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //Apply row_userschat layout for users in chat list
        View view = LayoutInflater.from(context).inflate(R.layout.row_userschat, viewGroup, false);

        return new MyHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {

        final ModelUser user = userList.get(i);
        final String hisUID = userList.get(i).getUid();
        String userImage = userList.get(i).getImage();
        String userName = userList.get(i).getName();
        final String userEmail = userList.get(i).getEmail();

        //Retrieve last message sent in chat
        lastMessage(user.getUid(), myHolder.mMessageTv);

        //Set name text view and email text view with user's info
        myHolder.mNameTv.setText(userName);
        myHolder.mEmailTv.setText(userEmail);

        //load user's profile picture into image view
        try {
            Picasso.get().load(userImage)
                    .placeholder(R.drawable.ic_default_img_pink)
                    .into(myHolder.mAvatarIv);
        }
        catch (Exception e) {

        }

        //show option to go to profile or chat when user clicks on another user in the chatlist
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setItems(new String[]{"Profile", "Chat"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Redirect user to the profile of the user they clicked on
                        if (which == 0) {
                            Intent intent = new Intent(context, ThereProfileActivity.class);
                            intent.putExtra("uid", hisUID);
                            context.startActivity(intent);
                        }
                        //Redirect user to the chat
                        if (which == 1) {
                            Intent intent = new Intent(context, ChatActivity.class);
                            intent.putExtra("hisUid", hisUID);
                            context.startActivity(intent);
                        }
                    }
                });
                builder.create().show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        ImageView mAvatarIv;
        TextView mNameTv, mEmailTv, mMessageTv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            mAvatarIv = itemView.findViewById(R.id.avatarIv);
            mNameTv = itemView.findViewById(R.id.nameTv);
            mEmailTv = itemView.findViewById(R.id.emailTv);
            mMessageTv = itemView.findViewById(R.id.messageTv);

        }

    }

    //Method to find and set the last message of the chat into the last_msg text view
    private void lastMessage(final String userid, final TextView last_msg) {

        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ModelChat chat = snapshot.getValue(ModelChat.class);

                    if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())) {
                        theLastMessage = chat.getMessage();
                    }
                }

                last_msg.setText(theLastMessage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
