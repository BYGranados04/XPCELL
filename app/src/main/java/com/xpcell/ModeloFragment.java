package com.xpcell;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import Adapter.ModeloAdapter;
import Model.Modelo;

public class ModeloFragment extends Fragment {
    private static final String ARG_MODELOS = "modelos";
    private List<Modelo> modelos;

    public static ModeloFragment newInstance(List<Modelo> modelos) {
        ModeloFragment fragment = new ModeloFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_MODELOS, new ArrayList<>(modelos));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modelo, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewModelos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getArguments() != null) {
            modelos = getArguments().getParcelableArrayList(ARG_MODELOS);
            ModeloAdapter adapter = new ModeloAdapter(modelos);
            recyclerView.setAdapter(adapter);
        }

        return view;
    }
}
