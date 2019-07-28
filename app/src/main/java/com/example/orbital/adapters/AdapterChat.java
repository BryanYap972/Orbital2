package com.example.orbital.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orbital.R;
import com.example.orbital.models.ModelChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyHolder> {


    //Initialise variables
    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    Context context;
    List<ModelChat> chatList;
    String imageUrl;

    FirebaseUser fUser;

    //Constructor
    public AdapterChat(Context context, List<ModelChat> chatList, String imageUrl) {
        this.context = context;
        this.chatList = chatList;
        this.imageUrl = imageUrl;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (i == MSG_TYPE_RIGHT) {
            //use row_chat_right layout for sent messages
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_right, viewGroup, false);
            return new MyHolder(view);
        }
        else {
            //use row_chat_left layout for received messages
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_left, viewGroup, false);
            return new MyHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, final int i) {

        //Get message and time stamp from each chat
        String message = chatList.get(i).getMessage();
        String timeStamp = chatList.get(i).getTimestamp();

        ModelChat chat = chatList.get(i);


        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(timeStamp));
        String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString();

        //Set message text view and time text view with the necessary information obtained from firebase
        myHolder.messageTv.setText(message);
        myHolder.timeTv.setText(dateTime);


        try {
            //load profile image data and place image into profile image view
            Picasso.get().load(imageUrl)
                    .placeholder(R.drawable.ic_default_img_pink).into(myHolder.profileIv);
        }
        catch (Exception e) {


        }

        //Set option to delete message upon clicking on message
        myHolder.messageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create alert dialog to ask if user wants to delete message
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure you want to delete this message?");

                //Implement delete function for delete option
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMessage(i);
                    }
                });

                //Implement cancel function for No option
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });

        //Doesn't work
        if (i == (chatList.size() - 1)) {
            if (chat.isSeen()) {
                myHolder.isSeenTv.setText("Seen");
            }
            else {
                myHolder.isSeenTv.setText("Delivered");
            }

        }
        else {
            myHolder.isSeenTv.setVisibility(View.GONE);
        }
    }

    //Method for deleting message
    private void deleteMessage(int position) {
        final String myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();


        String msgTimeStamp = chatList.get(position).getTimestamp();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");
        Query query = dbRef.orderByChild("timestamp").equalTo(msgTimeStamp);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //check if user who wants to delete message was the one who sent it
                    if (ds.child("sender").getValue().equals(myUID)) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        //Replace message with delete message
                        hashMap.put("message", "This message was deleted");
                        ds.getRef().updateChildren(hashMap);

                        Toast.makeText(context, "Message has been deleted", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //If user was not the one who sent the message, do not delete message and info user that they are not able to delete message
                        Toast.makeText(context, "You can only delete your messages", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (chatList.get(position).getSender().equals(fUser.getUid())) {
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }
    }

    //Constructor for view holder
    class MyHolder extends RecyclerView.ViewHolder{

        ImageView profileIv;
        TextView messageTv, timeTv, isSeenTv;
        LinearLayout messageLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            profileIv = itemView.findViewById(R.id.profileIv);
            messageTv = itemView.findViewById(R.id.messageTv);
            timeTv = itemView.findViewById(R.id.timeTv);
            isSeenTv = itemView.findViewById(R.id.isSeenTv);
            messageLayout = itemView.findViewById(R.id.messageLayout);
        }
    }
}
