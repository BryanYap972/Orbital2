package com.example.orbital;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.orbital.Aarhus.AarhusActivity;
import com.example.orbital.AlbertLudwig.AlbertLudwigActivity;
import com.example.orbital.Bilkent.BilkentActivity;
import com.example.orbital.BostonU.BostonUActivity;
import com.example.orbital.Carleton.CarletonActivity;
import com.example.orbital.Chongqing.ChongqingActivity;
import com.example.orbital.Clarkson.ClarksonActivity;
import com.example.orbital.Durham.DurhamActivity;
import com.example.orbital.Fudan.FudanActivity;
import com.example.orbital.ImperialCollege.ImperialCollegeActivity;
import com.example.orbital.adapters.AdapterUnis;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewsFragment extends Fragment {

    FirebaseAuth firebaseAuth;

    ArrayList<String> menuItems = new ArrayList<>();

    ListView listView;

    FirebaseUser fUser;

    String myUid;

    public ReviewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        firebaseAuth = FirebaseAuth.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        myUid = fUser.getUid();

        menuItems.add("Aarhus University");
        menuItems.add("Albert-Ludwig University of Freiburg");
        menuItems.add("Boston University");
        menuItems.add("Bilkent University");
        menuItems.add("Carleton College");
        menuItems.add("Chongqing University");
        menuItems.add("Clarkson University");
        menuItems.add("Durham University");
        menuItems.add("Fudan University");
        menuItems.add("Imperial College London");



        View view =  inflater.inflate(R.layout.fragment_reviews, container, false);

        listView =  view.findViewById(R.id.reviewList);


        listView.setAdapter(new AdapterUnis(getActivity(), R.layout.reviewlayout, menuItems));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                if (position == 0) {
                    Intent intent = new Intent(getActivity(), AarhusActivity.class);
                    startActivity(intent);
                }
                if (position == 1) {
                    Intent intent = new Intent(getActivity(), AlbertLudwigActivity.class);
                    startActivity(intent);
                }
                if (position == 2) {
                    Intent intent = new Intent(getActivity(), BostonUActivity.class);
                    startActivity(intent);
                }
                if (position == 3) {
                    Intent intent = new Intent(getActivity(), BilkentActivity.class);
                    startActivity(intent);
                }
                if (position == 4) {
                    Intent intent = new Intent(getActivity(), CarletonActivity.class);
                    startActivity(intent);
                }
                if (position == 5) {
                    Intent intent = new Intent(getActivity(), ChongqingActivity.class);
                    startActivity(intent);
                }
                if (position == 6) {
                    Intent intent = new Intent(getActivity(), ClarksonActivity.class);
                    startActivity(intent);
                }
                if (position == 7) {
                    Intent intent = new Intent(getActivity(), DurhamActivity.class);
                    startActivity(intent);
                }
                if (position == 8) {
                    Intent intent = new Intent(getActivity(), FudanActivity.class);
                    startActivity(intent);
                }
                if (position == 9) {
                    Intent intent = new Intent(getActivity(), ImperialCollegeActivity.class);
                    startActivity(intent);
                }

            }
        });

        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        menu.findItem(R.id.action_add_post).setVisible(false);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                ArrayList<String> results = new ArrayList<>();

                for (String i : menuItems) {
                    if(i.toLowerCase().contains(s.toLowerCase())) {
                        results.add(i);
                    }
                }

                ((AdapterUnis)listView.getAdapter()).update(results);

                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }


    private void checkUserStatus() {
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            //mProfileTv.setText(user.getEmail());
        }
        else {
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
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


        return super.onOptionsItemSelected(item);
    }


}
