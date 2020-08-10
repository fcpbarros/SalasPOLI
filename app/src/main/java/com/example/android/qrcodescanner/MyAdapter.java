package com.example.android.qrcodescanner;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public  class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements Filterable{


    ArrayList<Salas> salasListFull;
    ArrayList<Salas> salasExample;
    Context context;
    ArrayList<Salas> salas;

    public MyAdapter(Context c, ArrayList<Salas> s){

        context = c;
        salas = s;
        this.salasExample = s;
        salasListFull = new ArrayList<>(s);

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.salas_info,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {



            // Find the TextView with view status
            TextView statusView = holder.statusInfo ;
//          GradientDrawable salasCircle = (GradientDrawable) salaView.getBackground();
            //Get the appropriate background color based on the current earthquake magnitude
            int statusColor = getColor(salas.get(position).getStatus());
            // Set the color on the magnitude circle
            statusView.setTextColor(statusColor);

            holder.statusInfo.setText(salas.get(position).getStatus());

            holder.profInfo.setText(salas.get(position).getProf());

            //holder.chaveInfo.setText(salas.get(position).getChave());
            holder.salaInfo.setText(salas.get(position).getSala());
            holder.materiasInfo.setText(salas.get(position).getMateria());



    }

    @Override
    public int getItemCount() {
        return salas.size();
    }

    @Override
    public Filter getFilter() {
        return salasFilter;
    }

    private Filter salasFilter = new Filter(){


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Salas> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(salasListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Salas item : salasListFull) {
                    if (item.getProf().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {


                salasExample.clear();
                salasExample.addAll((List) results.values);
                notifyDataSetChanged();



        }
    };

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView statusInfo,profInfo,chaveInfo,salaInfo,materiasInfo;

        Button btn;
        public MyViewHolder(View itemView) {
            super(itemView);
            statusInfo = (TextView) itemView.findViewById(R.id.statusInfo);
            profInfo = (TextView) itemView.findViewById(R.id.profInfo);
            //chaveInfo = (TextView) itemView.findViewById(R.id.chaveInfo);
            salaInfo = (TextView) itemView.findViewById(R.id.salaInfo);
            materiasInfo = (TextView) itemView.findViewById(R.id.materiasInfo);

        }

    }


    private int getColor(String status) {

        int salasColorResourceId = 0;

        switch (status) {
            case "Livre":
                salasColorResourceId = R.color.Livre;
                break;
            case "Aula":
                salasColorResourceId = R.color.Aula;
                break;

        }

        return ContextCompat.getColor(context, salasColorResourceId);
    }

}
