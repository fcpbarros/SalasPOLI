package com.example.android.qrcodescanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ProfActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView profScannerView;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mProfRef;

    DataSnapshot dataSnapshot;
    String scanResult;
//    Intent intent = getIntent();
//    final String Sala = intent.getStringExtra("Sala");
//    final String prof = intent.getStringExtra("prof");
//    final String materia = intent.getStringExtra("materia");
//    final String movimento = intent.getStringExtra("movimento");
//    final String horarios = intent.getStringExtra("horarios");
//    final String hora = intent.getStringExtra("hora");


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        profScannerView = new ZXingScannerView(this);
        setContentView(profScannerView);
        profScannerView.startCamera();
        profScannerView.setResultHandler(this);


    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        profScannerView.stopCamera();
    }

    @Override
    public void onStop(){
        super.onStop();
        profScannerView.stopCamera();
    }

    @Override
    public void onPause(){
        super.onPause();
        profScannerView.stopCamera();
    }

    public void displayAlertMessage(String message, DialogInterface.OnClickListener listener){
        new AlertDialog.Builder(ProfActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", listener)
                .setNegativeButton("Cancel", null)
                .create().show();
    }

    @Override
    public void handleResult(Result result) {

        Intent intent = getIntent();
        final String Sala = intent.getStringExtra("Sala");
        final String prof = intent.getStringExtra("prof");
        final String materia = intent.getStringExtra("materia");
        final String movimento = intent.getStringExtra("movimento");
        final String horarios = intent.getStringExtra("horarios");
        final String hora = intent.getStringExtra("hora");

         final String scanResult = result.getText(); //ler o QRcode do professor

         if (scanResult.equals(prof) && movimento.equals("entrada") ) {

            AlertDialog.Builder builder = new AlertDialog.Builder(ProfActivity.this);
            builder.setTitle("Leitura de confirmação do professor");
            builder.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    mRootRef.child("Salas").child(Sala).child("entrada").setValue(hora);
                    mRootRef.child("Salas").child(Sala).child("chave").setValue("Professor");
                    //mRootRef.child("Salas").child(Sala).child("status").setValue("Aula");
                    mRootRef.child("Salas").child(Sala).child("prof").setValue(scanResult);
                    mRootRef.child("Salas").child(Sala).child("materia").setValue(materia);

                    Intent myIntent = new Intent(ProfActivity.this, MainActivity.class);
                    startActivity(myIntent);


                }
            });
            builder.setMessage("LEITURA CONFIRMADA!!\n" + "Prof. " + scanResult + "\nDisciplina: " + materia );
            AlertDialog alert = builder.create();
            alert.show();

        } else if(scanResult.equals(prof) && movimento.equals("saida")){

            AlertDialog.Builder builder = new AlertDialog.Builder(ProfActivity.this);
            builder.setTitle("Leitura de confirmação do professor");
            builder.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    final String saida = hora;
                    //final ArrayList<String> horaEntrada = new ArrayList<>();


                    mRootRef.child("Salas").child(Sala).child("chave").setValue("DATP");
                    mRootRef.child("Salas").child(Sala).child("status").setValue("Livre");
                    mRootRef.child("Salas").child(Sala).child("prof").setValue("");
                    mRootRef.child("Salas").child(Sala).child("materia").setValue("");
                    mRootRef.child("Salas").child(Sala).child("entrada").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String entrada = dataSnapshot.getValue(String.class);
                            //horaEntrada.add(dataSnapshot.getValue(String.class));
                            addItemToSheet(scanResult,horarios,entrada,saida,Sala,materia);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    //mRootRef.child("Salas").child(Sala).child("entrada").setValue("");
                    Intent myIntent = new Intent(ProfActivity.this, MainActivity.class);
                    startActivity(myIntent);

                }
            });
            builder.setMessage("Chave devolvida ao DATP");
            AlertDialog alert = builder.create();
            alert.show();


            //addItemToSheet(scanResult,horarios,entrada,saida,Sala,materia);


            }

        else {

            AlertDialog.Builder builder = new AlertDialog.Builder(ProfActivity.this);
            builder.setTitle("Leitura de confirmação do professor");
            builder.setPositiveButton("Ok",new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent myIntent = new Intent(ProfActivity.this, MainActivity.class);
                    startActivity(myIntent);

                }
            });
            builder.setMessage("A sala " + Sala + " não está atribuída a(o) Prof. " + scanResult + ".\n"
            + "Voltar a tela inicial e iniciar processo novamente");
            AlertDialog alert = builder.create();
            alert.show();
        }

    }



    private void addItemToSheet(final String nomeProf, final String horarioProf ,final String entradaProf ,final String saidaProf,final String salaProf , final String materiaProf) {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbx_lB2LFFtm06tS8IHXYQPW61iHq3IBmYdTdA1A0YcvSh7sxm8L/exec",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(ProfActivity.this, "sucesso", Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ProfActivity.this, "ERRRROUUUUUUU", Toast.LENGTH_LONG).show();
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams() {
                    final Map<String, String> parmas = new HashMap<String, String>();


                    //here we pass params
                    parmas.put("action", "addItem");
                    parmas.put("nome", nomeProf);
                    parmas.put("horario", horarioProf);
                    parmas.put("sala", salaProf);
                    parmas.put("materia",materiaProf);
                    parmas.put("entrada",entradaProf);
                    parmas.put("saida",saidaProf);


                    return parmas;
                }
            }; //até aqui

            RequestQueue queue = Volley.newRequestQueue(this);

            queue.add(stringRequest);

    }

}


