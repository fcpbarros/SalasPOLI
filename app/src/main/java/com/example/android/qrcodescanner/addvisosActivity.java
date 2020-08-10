package com.example.android.qrcodescanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class addvisosActivity extends AppCompatActivity {

    Button publicarButton;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    FirebaseDatabase database;
    DatabaseReference refMensagens;

    String profNome, mensagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addvisos);

        database = FirebaseDatabase.getInstance();
        refMensagens = database.getReference("mensagens");


        Button avisosButton = (Button) findViewById(R.id.publicarButton);
        final TextView profName = (TextView) findViewById(R.id.profNameView);
        final TextView mensagemView = (TextView) findViewById(R.id.mensagemView);

        avisosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                profNome = profName.getText().toString();
                mensagem = mensagemView.getText().toString();

                refMensagens.child(profNome).child("professor").setValue(profNome);
                refMensagens.child(profNome).child("mensagem").setValue(mensagem);
                finish();

//                Intent myIntent = new Intent(addvisosActivity.this, MainActivity.class);
//                startActivity(myIntent);

            }
        });


    }
}
