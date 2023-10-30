package com.example.restyledonate;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DonacionesAdapter extends RecyclerView.Adapter<DonacionesAdapter.ViewHolder> {

    private ArrayList<MainCatalogos> list;
    private Context context;

    public DonacionesAdapter(ArrayList<MainCatalogos> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_catalogos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MainCatalogos model = list.get(position);
        Picasso.get().load(model.getImagenUrl()).placeholder(R.drawable.ic_dashboard_black_24dp).into(holder.img2);

        holder.nombredonaciontext.setText(model.getNombreArticulo());
        holder.propietariotext.setText(model.getNombreCompleto() + " " + model.getApellido());
        holder.tipotext.setText(model.getTipoArticulo());
        holder.preciotext.setText(model.getPrecio());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Solicitar.class);
                intent.putExtra("txtnombrearticulo", model.getNombreArticulo());
                intent.putExtra("txtnombrecompleto", model.getNombreCompleto());
                intent.putExtra("txtapellidos", model.getApellido());
                intent.putExtra("txtcantidad", model.getCantidad());
                intent.putExtra("txtcorreo", model.getCorreo());
                intent.putExtra("txtdescripcion", model.getDescripcion());
                intent.putExtra("txthorario", model.getHorario());
                intent.putExtra("txtlatitud", model.getLatitud());
                intent.putExtra("txtlongitud", model.getLongitud());
                intent.putExtra("txtprecio", model.getPrecio());
                intent.putExtra("txttelefono", model.getTelefono());
                intent.putExtra("txttipo", model.getTipoArticulo());
                intent.putExtra("imagenarticulo", model.getImagenUrl());
                intent.putExtra("imagenusuario", model.getImage());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombredonaciontext, propietariotext, tipotext, preciotext;
        ImageView img2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombredonaciontext = itemView.findViewById(R.id.nombredonaciontext);
            propietariotext = itemView.findViewById(R.id.propietariotext);
            tipotext = itemView.findViewById(R.id.tipotext);
            preciotext = itemView.findViewById(R.id.preciotext);
            img2 = itemView.findViewById(R.id.img1);
        }
    }
}
