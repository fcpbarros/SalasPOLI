package com.example.android.qrcodescanner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class avisosAdapter extends RecyclerView.Adapter<avisosAdapter.MyViewHolder> {

    Context context;
    ArrayList<Avisos> avisos;

    public avisosAdapter(Context c, ArrayList<Avisos> a){

        context = c;
        avisos = a;

    }


    @NonNull
    @Override
    public avisosAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new avisosAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.avisos_info,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull avisosAdapter.MyViewHolder myViewHolder, int position) {

        myViewHolder.profInfo.setText(avisos.get(position).getProfessor());
        myViewHolder.mensagemInfo.setText(avisos.get(position).getMensagem());

    }

    @Override
    public int getItemCount() {
        return avisos.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView profInfo,mensagemInfo;

        Button btn;
        public MyViewHolder(View itemView) {
            super(itemView);

            profInfo = (TextView) itemView.findViewById(R.id.profInfo);
            mensagemInfo = (TextView) itemView.findViewById(R.id.mensagemInfo);

        }

    }
}
