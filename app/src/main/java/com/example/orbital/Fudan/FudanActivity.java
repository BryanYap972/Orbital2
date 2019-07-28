package com.example.orbital.Fudan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import com.example.orbital.MainActivity;
import com.example.orbital.R;
import com.example.orbital.models.ModelReviews;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FudanActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    RecyclerView recyclerView;
    List<ModelReviews> reviewsList;
    AdapterFudan adapterReviews;

    ActionBar actionBar;

    FirebaseUser fUser;
    String myUid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fudan);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Fudan University");

        firebaseAuth = FirebaseAuth.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        myUid = fUser.getUid();

        recyclerView = this.findViewById(R.id.fudanRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(layoutManager);

        reviewsList = new ArrayList<>();

        loadReviews();
    }

    private void loadReviews() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Fudan");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reviewsList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    ModelReviews modelReviews = ds.getValue(ModelReviews.class);

                    reviewsList.add(modelReviews);

                    adapterReviews = new AdapterFudan(getApplicationContext(), reviewsList);
                    recyclerView.setAdapter(adapterReviews);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void checkUserStatus() {
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            myUid = fUser.getUid();
        }
        else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        menu.findItem(R.id.action_search).setVisible(false);


        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.action_logout) {
            String timestamp = String.valueOf(System.currentTimeMillis());

            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users").child(myUid);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("onlineStatus", timestamp);

            dbRef.updateChildren(hashMap);

            firebaseAuth.signOut();
            checkUserStatus();
        }
        if (id == R.id.action_add_post) {
            startActivity(new Intent(this, AddReviewFudan.class));
        }

        return super.onOptionsItemSelected(item);
    }

}
