package com.example.llm.ui.ejercicios.manageEjercicios;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.llm.R;
import com.example.llm.databinding.FragmentManageEjerciciosBinding;
import com.example.llm.ui.ejercicios.ObservarAdapter;
import com.example.llm.ui.ejercicios.Ejercicio;
import com.example.llm.ui.ejercicios.PincharAdapter;
import com.example.llm.ui.ejercicios.TestAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ManageEjercicioDialogFragment extends DialogFragment {

    private FragmentManageEjerciciosBinding binding;

    ObservarAdapter observarAdapter;
    TestAdapter testAdapter;
    PincharAdapter pincharAdapter;
    RecyclerView rvObservar, rvTest, rvPinchar;
    FirebaseFirestore firestore;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentManageEjerciciosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        observarAdapter.startListening();
        testAdapter.startListening();
        pincharAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        observarAdapter.stopListening();
        testAdapter.stopListening();
        pincharAdapter.stopListening();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        firestore = FirebaseFirestore.getInstance();

        //-------------------------------- Ejercicios de observación --------------------------------//
        rvObservar = view.findViewById(R.id.observarRecyclerView);
        rvObservar.setLayoutManager(new LinearLayoutManager(getActivity()));
        Query queryObservar = firestore.collection("Ejercicios - Observación");

        FirestoreRecyclerOptions<Ejercicio> observarRecyclerOptions = new FirestoreRecyclerOptions.Builder<Ejercicio>().setQuery(queryObservar, Ejercicio.class).build();
        observarAdapter = new ObservarAdapter(observarRecyclerOptions, getActivity(), getActivity().getSupportFragmentManager());
        observarAdapter.notifyDataSetChanged();
        rvObservar.setAdapter(observarAdapter);

        //---------------------------------- Ejercicios tipo test -----------------------------------//
        rvTest = view.findViewById(R.id.testRecyclerView);
        rvTest.setLayoutManager(new LinearLayoutManager(getActivity()));
        Query queryTest = firestore.collection("Ejercicios - Test");

        FirestoreRecyclerOptions<Ejercicio> testRecyclerOptions = new FirestoreRecyclerOptions.Builder<Ejercicio>().setQuery(queryTest, Ejercicio.class).build();
        testAdapter = new TestAdapter(testRecyclerOptions, getActivity(), getActivity().getSupportFragmentManager());
        testAdapter.notifyDataSetChanged();
        rvTest.setAdapter(testAdapter);

        //---------------------------------- Ejercicios de pinchar y arrastrar -----------------------------------//
        rvPinchar = view.findViewById(R.id.pincharRecyclerView);
        rvPinchar.setLayoutManager(new LinearLayoutManager(getActivity()));
        Query queryPinchar = firestore.collection("Ejercicios - Pinchar y Arrastrar");

        FirestoreRecyclerOptions<Ejercicio> pincharRecyclerOptions = new FirestoreRecyclerOptions.Builder<Ejercicio>().setQuery(queryPinchar, Ejercicio.class).build();
        pincharAdapter = new PincharAdapter(pincharRecyclerOptions, getActivity(), getActivity().getSupportFragmentManager());
        pincharAdapter.notifyDataSetChanged();
        rvPinchar.setAdapter(pincharAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = (getResources().getDisplayMetrics().heightPixels)*8/9;
        getDialog().getWindow().setLayout(width, height);
    }
}