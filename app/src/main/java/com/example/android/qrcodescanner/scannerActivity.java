package com.example.android.qrcodescanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.solver.ArrayLinkedVariables;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

public class scannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView, scannerViewProf;
    FirebaseDatabase database;
    DatabaseReference ref,refSala,refDiaDaSemana,refHorarios,refHora;
    Salas salas;
    Professores professores;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        scannerViewProf = new ZXingScannerView(this);
        setContentView(scannerView);





        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkPermission()) {
                Toast.makeText(scannerActivity.this, "Permission is granted", Toast.LENGTH_LONG).show();
            } else {
                requestPermission();
            }

        }
    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(scannerActivity.this, CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    public void onRequestPermissionsResult(int requestCode, String permission[], int grantResults[]) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted) {
                        Toast.makeText(scannerActivity.this, "Permission granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(scannerActivity.this, "Permission denied", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                displayAlertMessage("You need to allow access for both permissions", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestPermissions(new String[]{CAMERA}, REQUEST_CAMERA);
                                    }
                                });
                                return;
                            }
                        }
                    }
                }
                break;
        }

    }

    @Override
    public void onResume() {

        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {

                if (scannerView == null) {
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.startCamera();
                scannerView.setResultHandler(this);


            } else {
                requestPermission();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    @Override
    public void onStop() {
        super.onStop();
        scannerView.stopCamera();

    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    public void displayAlertMessage(String message, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(scannerActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", listener)
                .setNegativeButton("Cancel", null)
                .create().show();
    }


    @Override
    public void handleResult(Result result) {

        final String scanResult = result.getText();

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Salas");
        refSala = ref.child(scanResult);

        Calendar cal = Calendar.getInstance();
        Date time=cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("EEEE");
        DateFormat horaFormat = new SimpleDateFormat("HH:mm");
        final String formattedDate = dateFormat.format(time);
        final String hora = horaFormat.format(time);
        final Date horaDate = stringToDate(hora);

        refDiaDaSemana = refSala.child(formattedDate);
        refHorarios = refDiaDaSemana.child("horarios");
        //refHora = refHorarios.child(hora);


        ref.child(scanResult).child("chave").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);

                if(text.equals("DATP")){

                    AlertDialog.Builder builder = new AlertDialog.Builder(scannerActivity.this);
                    builder.setTitle("Entrada de professor");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            refHorarios.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    //Salas sala = dataSnapshot.getValue(Salas.class);

                                    refSala.child(scanResult).child("entrada").setValue("");

                                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                        final String name = ds.getKey();

                                        String[] horarios = name.split("-");
                                        Date horaEntrada = stringToDate(horarios[0]);
                                        Date horaSaida = stringToDate(horarios[1]);

                                        DateFormat horaFormatt = new SimpleDateFormat("HH:mm");


                                        Calendar horaModificada = Calendar.getInstance();
                                        horaModificada.setTime(horaEntrada);
                                        horaModificada.add(Calendar.MINUTE, -10);
                                        Date timeHoraModificada = horaModificada.getTime();
                                        horaDate.after(timeHoraModificada);


                                        if( (horaDate.equals(horaEntrada))|| (horaDate.before(horaSaida) && horaDate.after(horaEntrada))){

                                            refHorarios.child(name).child("professor").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    final String prof = dataSnapshot.getValue(String.class);

                                                    AlertDialog.Builder alerta = new AlertDialog.Builder(scannerActivity.this);
                                                    alerta.setTitle("Confirmação");
                                                    alerta.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            refHorarios.child(name).child("materia").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    final String materia = dataSnapshot.getValue(String.class);
                                                                    Intent myIntent = new Intent(scannerActivity.this, ProfActivity.class);
                                                                    myIntent.putExtra("Sala", scanResult); //Optional parameters
                                                                    myIntent.putExtra("prof", prof); //Optional parameters
                                                                    myIntent.putExtra("materia", materia); //Optional parameters
                                                                    myIntent.putExtra("movimento", "entrada"); //Optional parameters
                                                                    myIntent.putExtra("horarios",name);
                                                                    myIntent.putExtra("hora", hora);
                                                                    startActivity(myIntent);
                                                                }
                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {}
                                                            });
                                                        }
                                                    });
                                                    alerta.setMessage("Sala " + scanResult + ". Prof. " + prof + " ? \nLer QRcode do professor");
                                                    AlertDialog alertaProf = alerta.create();
                                                    alertaProf.show();
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {}
                                            });

                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {}
                            });
                        }
                    });
                    builder.setMessage(scanResult);
                    AlertDialog alert = builder.create();
                    alert.show();

                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(scannerActivity.this);
                    builder.setTitle("Saida de professor");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            refHorarios.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    //Salas sala = dataSnapshot.getValue(Salas.class);
                                    Log.d("TAGLOUCA","chegou ");
                                    for(DataSnapshot ds : dataSnapshot.getChildren()) {

                                        final String name = ds.getKey();
                                        String[] horarios = name.split("-");
                                        Date horaEntrada = stringToDate(horarios[0]);
                                        Date horaSaida = stringToDate(horarios[1]);
                                        if( (horaSaida.equals(horaDate))|| horaDate.before(horaSaida) || horaDate.after(horaEntrada)){
                                            Log.d("TAGLOUCA","chegou top");
                                            refHorarios.child(name).child("professor").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    final String prof = dataSnapshot.getValue(String.class);
                                                    Log.d("TAGLOUCA","chegou porraaaaaa");

                                                    AlertDialog.Builder alerta = new AlertDialog.Builder(scannerActivity.this);
                                                    alerta.setTitle("Confirmação");
                                                    alerta.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            refHorarios.child(name).child("materia").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    final String materia = dataSnapshot.getValue(String.class);
                                                                    Intent myIntent = new Intent(scannerActivity.this, ProfActivity.class);
                                                                    myIntent.putExtra("Sala", scanResult); //Optional parameters
                                                                    myIntent.putExtra("prof", prof); //Optional parameters
                                                                    myIntent.putExtra("materia", materia); //Optional parameters
                                                                    myIntent.putExtra("movimento", "saida"); //Optional parameters
                                                                    myIntent.putExtra("horarios",name);
                                                                    myIntent.putExtra("hora", hora); //Optional parameters
                                                                    startActivity(myIntent);
                                                                }
                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {}
                                                            });
                                                        }
                                                    });
                                                    alerta.setMessage("Sala " + scanResult + ". Prof. " + prof + " ? \nLer QRcode do professor");
                                                    AlertDialog alertaProf = alerta.create();
                                                    alertaProf.show();
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {}
                                            });
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {}
                            });
                        }
                    });
                    builder.setMessage(scanResult);
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }); //fim do ref.child(scanResult).child("chave")


    } //final de handle result

    public Date stringToDate(String string) {

        DateFormat horaFormat = new SimpleDateFormat("HH:mm");

        try {
            Date horaDate = horaFormat.parse(string);

            return horaDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }



    }

}



//                        if(sala == null){
//                            refSala.child("sala").setValue(scanResult);
//                            refSala.child("chave").setValue("DATP");
//                            refSala.child("status").setValue("Livre");
//                            refSala.child("prof").setValue("");
//                            finish();
//
//                        }else if(sala.getChave().equals("DATP")){
//                                refSala.child("sala").setValue(scanResult);
//                                refSala.child("chave").setValue("Professor");
//
//                                Intent myIntent = new Intent(scannerActivity.this, ProfActivity.class);
//                                myIntent.putExtra("Sala", scanResult); //Optional parameters
//                                myIntent.putExtra("movimento", "entrada"); //Optional parameters
//                                startActivity(myIntent);
//
//                        }else{
//                                refSala.child("chave").setValue("DATP");
//                                refSala.child("status").setValue("Livre");
//                                refSala.child("prof").setValue("");
//
//                                Intent myIntent = new Intent(scannerActivity.this, ProfActivity.class);
//                                myIntent.putExtra("Sala", scanResult); //Optional parameters
//                                myIntent.putExtra("movimento", "saida"); //Optional parameters
//                                startActivity(myIntent);
//
//                        }