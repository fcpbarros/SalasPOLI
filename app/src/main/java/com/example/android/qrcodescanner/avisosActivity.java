package com.example.android.qrcodescanner;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class avisosActivity extends AppCompatActivity {


    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    FirebaseDatabase database;
    DatabaseReference ref,rootRef;
    RecyclerView recyclerView;
    ArrayList<Avisos> list;
    avisosAdapter adapter;
    Avisos avisos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avisos);
        Button novoAvisoButton = (Button) findViewById(R.id.novoAviso);
        Button deleteButton = (Button) findViewById(R.id.deleteButton);

        recyclerView = (RecyclerView) findViewById(R.id.myRecyclerAvisos);
        recyclerView.setLayoutManager( new LinearLayoutManager(avisosActivity.this));

        avisos = new Avisos();

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("mensagens");
        //list = new ArrayList<>();
        // Create a new adapter that takes an empty list of earthquakes as input
        //adapter = new ArrayAdapter<String>(this,R.layout.salas_info,R.id.statusInfo,list);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<Avisos>();
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    avisos = ds.getValue(Avisos.class);
                    //list.add(salas.getProf() + " " + salas.getChave() + " " + salas.getStatus());
                    list.add(avisos);
                }

                adapter = new avisosAdapter(avisosActivity.this,list);
                recyclerView.setAdapter(adapter);
                //listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(avisosActivity.this, "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });

        novoAvisoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(avisosActivity.this, addvisosActivity.class);
                startActivity(myIntent);
            }
        });
//
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance();
                ref = database.getReference("mensagens");
                ref.removeValue();
            }
        });




    }
}
