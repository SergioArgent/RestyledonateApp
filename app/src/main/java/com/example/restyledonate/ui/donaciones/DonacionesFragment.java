package com.example.restyledonate.ui.donaciones;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restyledonate.DonacionesAdapter;
import com.example.restyledonate.MainCatalogos;
import com.example.restyledonate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DonacionesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<MainCatalogos> recycleList;

    // DatabaseReference para Firebase
    FirebaseDatabase firebaseDatabase;

    public DonacionesFragment() {
        // Constructor vacío requerido.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donaciones, container, false);
        recyclerView = view.findViewById(R.id.rcv_catalogos1);
        recycleList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        DonacionesAdapter recyclerAdapter = new DonacionesAdapter(recycleList, getContext().getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(null);

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(recyclerAdapter);

        // Obtén el ID del usuario actualmente autenticado
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Realiza la consulta Firebase para obtener las donaciones del usuario actual
            DatabaseReference donacionesRef = firebaseDatabase.getReference().child("donaciones").child(userId);
            donacionesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        MainCatalogos mainCatalogos = dataSnapshot.getValue(MainCatalogos.class);
                        recycleList.add(mainCatalogos);
                    }
                    recyclerAdapter.notifyDataSetChanged();
                    recyclerView.setLayoutManager(linearLayoutManager);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Maneja las cancelaciones de la consulta aquí si es necesario
                }
            });
        }

        return view;
    }
}
