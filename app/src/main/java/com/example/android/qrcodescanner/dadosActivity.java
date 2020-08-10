package com.example.android.qrcodescanner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;

import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import android.app.SearchManager;
import android.support.v7.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;


public class dadosActivity extends AppCompatActivity {


    FirebaseDatabase database;
    DatabaseReference ref;
    RecyclerView recyclerView;
    //ListView listView;
    //ArrayList<String> list;
    ArrayList<Salas> list;
    MyAdapter adapter;
    Salas salas;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //list = listaSalas();
        salas = new Salas();
        //listView = (ListView)findViewById(R.id.listView);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Salas");
        //list = new ArrayList<>();
        // Create a new adapter that takes an empty list of earthquakes as input
        //adapter = new ArrayAdapter<String>(this,R.layout.salas_info,R.id.statusInfo,list);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<Salas>();
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    salas = ds.getValue(Salas.class);
                    //list.add(salas.getProf() + " " + salas.getChave() + " " + salas.getStatus());
                    list.add(salas);
                }

                setUpRecyclerView(list);
                //adapter = new MyAdapter(dadosActivity.this,list);
                //recyclerView.setAdapter(adapter);
                //listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(dadosActivity.this, "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });



    }





    private void setUpRecyclerView(ArrayList<Salas> list) {
        RecyclerView recyclerView = findViewById(R.id.myRecycler);
        //recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new MyAdapter(dadosActivity.this,list);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);


                return true;
            }
        });
        return true;
    }


}
