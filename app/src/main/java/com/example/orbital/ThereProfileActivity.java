package com.example.orbital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ThereProfileActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    ImageView avatarIv, coverIv;
    TextView nameTv, emailTv, phoneTv, descriptionTv, countryTv;

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_there_profile);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        avatarIv = findViewById(R.id.avatarIv);
        coverIv = findViewById(R.id.coverIv);
        nameTv = findViewById(R.id.nameTv);
        emailTv = findViewById(R.id.emailTv);
        phoneTv = findViewById(R.id.phoneTv);
        descriptionTv = findViewById(R.id.descriptionTv);
        countryTv = findViewById(R.id.countryTv);

        firebaseAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");

        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("uid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String name = "" + ds.child("name").getValue();
                    String email= "Email: " + ds.child("email").getValue();
                    String phone = "Phone: " + ds.child("phone").getValue();
                    String description = "Past exchange experiences: " + ds.child("description").getValue();
                    String country = "Current exchange country: " + ds.child("country").getValue();
                    String image = "" + ds.child("image").getValue();
                    String cover = "" + ds.child("cover").getValue();


                    nameTv.setText(name);
                    emailTv.setText(email);
                    phoneTv.setText(phone);
                    descriptionTv.setText(description);
                    countryTv.setText(country);

                    try {
                        Picasso.get().load(image).into(avatarIv);
                    }
                    catch (Exception e) {
                        Picasso.get().load(R.drawable.ic_default_img_pink).into(avatarIv);
                    }

                    try {
                        Picasso.get().load(cover).into(coverIv);
                    }
                    catch (Exception e) {
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        checkUserStatus();
    }

    private void checkUserStatus() {
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            //mProfileTv.setText(user.getEmail());

        }
        else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_add_post).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_logout) {
            firebaseAuth.signOut();
            checkUserStatus();
        }
        return super.onOptionsItemSelected(item);
    }
}
