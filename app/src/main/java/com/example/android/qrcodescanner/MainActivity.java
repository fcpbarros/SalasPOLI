package com.example.android.qrcodescanner;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.solver.ArrayLinkedVariables;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {// implements ZXingScannerView.ResultHandler {

//    FirebaseDatabase database,database1;
//    DatabaseReference ref,ref1;
//    ArrayList<Professores> list;
//    Professores professores;
//    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
//    DatabaseReference mSalaRef, mProfRef, cProfRef;
//    DataSnapshot dataSnapshot;
//    String scanResult, nome, entrada, saida;
//    Button scanButton, backUpButton, dadosButton, avisosButton;



    private static final String Url = "https://teste1-358a8.firebaseio.com/chico";

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

        setContentView(R.layout.activity_main);
        //Button backUpButton = (Button) findViewById(R.id.backUp);
        Button scanButton = (Button) findViewById(R.id.scanButton);
        Button dadosButton = (Button) findViewById(R.id.dadosButton);
        Button avisosButton = (Button) findViewById(R.id.avisosButton);
//        Button novoAvisoButton = (Button) findViewById(R.id.novoAvisoButton);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//                    if (checkPermission()) {
//                        Toast.makeText(MainActivity.this, "Permission is granted", Toast.LENGTH_LONG).show();
//                    } else {
//                        requestPermission();
//                    }
//
//                }


                Intent myIntent = new Intent(MainActivity.this, scannerActivity.class);
                startActivity(myIntent);


            }
        });

//        backUpButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                professores = new Professores();
//                database1 = FirebaseDatabase.getInstance();
//                ref1 = database1.getReference("Profs");
//
//                ref1.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                        list = new ArrayList<Professores>();
//                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                            professores = ds.getValue(Professores.class);
//                            list.add(professores);
//
//                        }
//
//                        addItemToSheet(list);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//
//                });
//
//            }//onclick
//        });

        dadosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(MainActivity.this, dadosActivity.class);
                startActivity(myIntent);
                

            }
        });

        avisosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  Intent myIntent = new Intent(MainActivity.this, avisosActivity.class);
                  startActivity(myIntent);
            }
        });


    }

//    private boolean checkPermission() {
//        return (ContextCompat.checkSelfPermission(MainActivity.this, CAMERA) == PackageManager.PERMISSION_GRANTED);
//    }
//
//    private void requestPermission() {
//        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
//    }

//    public void onRequestPermissionsResult(int requestCode, String permission[], int grantResults[]) {
//        switch (requestCode) {
//            case REQUEST_CAMERA:
//                if (grantResults.length > 0) {
//                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                    if (cameraAccepted) {
//                        Toast.makeText(MainActivity.this, "Permission granted", Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_LONG).show();
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            if (shouldShowRequestPermissionRationale(CAMERA)) {
//                                displayAlertMessage("You need to allow access for both permissions", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        requestPermissions(new String[]{CAMERA}, REQUEST_CAMERA);
//                                    }
//                                });
//                                return;
//                            }
//                        }
//                    }
//                }
//                break;
//        }
//
//    }

//    @Override
//    public void onResume() {
//
//        super.onResume();
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkPermission()) {
//
//                if (scannerView == null) {
//                    scannerView = new ZXingScannerView(this);
//                    setContentView(scannerView);
//                }
//                scannerView.startCamera();
//                scannerView.setResultHandler(this);
//
//
//            } else {
//                requestPermission();
//            }
//        }
//    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        scannerView.stopCamera();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        scannerView.stopCamera();
//
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        scannerView.stopCamera();
//    }

//    public void displayAlertMessage(String message, DialogInterface.OnClickListener listener) {
//        new AlertDialog.Builder(MainActivity.this)
//                .setMessage(message)
//                .setPositiveButton("OK", listener)
//                .setNegativeButton("Cancel", null)
//                .create().show();
//    }

//    @Override
//    public void handleResult(Result result) {
//
//        final String scanResult = result.getText();
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//        builder.setTitle("Resultado");
//        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//          public void onClick(DialogInterface dialog, int which) {
//                mSalaRef = mRootRef;
//                mSalaRef= mSalaRef.child("Salas");
//                mSalaRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        String text = dataSnapshot.getValue(String.class);
//
//                        if(text != null) {
//                            if (text.equals("DATP")) {
//                                mSalaRef.child("chave").setValue("Professor");
//                                //mSalaRef.setValue("Professor");
//
//                                Intent myIntent = new Intent(MainActivity.this, ProfActivity.class);
//                                myIntent.putExtra("Sala", scanResult); //Optional parameters
//                                myIntent.putExtra("movimento", "entrada"); //Optional parameters
//                                startActivity(myIntent);
//
//
//                            } else {
//                                mSalaRef.child("chave").setValue("DATP");
//
//                                mSalaRef.child("status").setValue("Livre");
//
//                                Intent myIntent = new Intent(MainActivity.this, ProfActivity.class);
//                                myIntent.putExtra("Sala", scanResult); //Optional parameters
//                                myIntent.putExtra("movimento", "saida"); //Optional parameters
//                                startActivity(myIntent);
//
//                                mSalaRef.child("prof").setValue("");
//
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//            }
//        });
//        builder.setMessage(scanResult);
//        AlertDialog alert = builder.create();
//        alert.show();
//    }

    private void addItemToSheet(ArrayList<Professores> professores) {

        final ArrayList<Professores> profs = professores;

        for ( int i = 0; i <= profs.size() - 1; i++) {

            final Professores profAtual = profs.get(i);
            Log.d("TAG","chegou aqui essa bagaca");
            //Toast.makeText(MainActivity.this, profNome, Toast.LENGTH_LONG).show();


            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbwQg27bVt1abhRVUUQ7JQp9b7Cy7lcMv8escm39hrJqCFfwHa8/exec",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(MainActivity.this, "sucesso", Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, "ERRRROUUUUUUU", Toast.LENGTH_LONG).show();
                        }
                    })
             {
                @Override
                protected Map<String, String> getParams() {
                    final Map<String, String> parmas = new HashMap<String, String>();


                    //here we pass params
                    parmas.put("action", "addItem");
                    parmas.put("nome", profAtual.getNome());
                    parmas.put("entrada", profAtual.getEntrada());
                    String sala = profAtual.getSala();
                    parmas.put("saida", profAtual.getSaida());
                    parmas.put("sala",profAtual.getSala());


                    return parmas;
                }
            }; //até aqui

            RequestQueue queue = Volley.newRequestQueue(this);

            queue.add(stringRequest);



        }



    }
}






