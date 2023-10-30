package com.example.restyledonate.ui.catalogo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.restyledonate.CatalogosAdapter;
import com.example.restyledonate.MainCatalogos;
import com.example.restyledonate.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;

public class CatalogoFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<MainCatalogos> recycleList;

    // DatabaseReference para Firebase
    FirebaseDatabase firebaseDatabase;

    public CatalogoFragment() {
        // Constructor vacío requerido.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalogo, container, false);
        recyclerView = view.findViewById(R.id.rcv_catalogos);
        recycleList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        CatalogosAdapter recyclerAdapter = new CatalogosAdapter(recycleList, getContext().getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        //recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(recyclerAdapter);

        // Realiza la consulta Firebase para obtener todas las donaciones
        DatabaseReference donacionesRef = firebaseDatabase.getReference().child("donaciones");
        donacionesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    // Obtén cada donación y agrégala a la lista
                    for (DataSnapshot donacionSnapshot : dataSnapshot.getChildren()) {
                        MainCatalogos mainCatalogos = donacionSnapshot.getValue(MainCatalogos.class);
                        recycleList.add(mainCatalogos);
                    }

                    Collections.reverse(recycleList);
                    recyclerAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Maneja las cancelaciones de la consulta aquí si es necesario
            }
        });
        return view;
    }
}
