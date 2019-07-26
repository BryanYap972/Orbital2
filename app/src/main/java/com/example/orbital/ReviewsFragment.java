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
import com.example.orbital.BostonU.BostonUActivity;
import com.example.orbital.adapters.AdapterUnis;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewsFragment extends Fragment {

    FirebaseAuth firebaseAuth;

    ArrayList<String> menuItems = new ArrayList<>();

    ListView listView;


    public ReviewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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
            firebaseAuth.signOut();
            checkUserStatus();
        }


        return super.onOptionsItemSelected(item);
    }


}
