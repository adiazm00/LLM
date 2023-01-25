package com.example.llm.ui.alumnado.manageEstudiantes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.llm.ui.alumnado.Estudiante;
import com.example.llm.ui.alumnado.EstudianteAdapter;
import com.example.llm.R;
import com.example.llm.databinding.FragmentManageEstudiantesBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ManageEstudianteDialogFragment extends DialogFragment {

    private FragmentManageEstudiantesBinding binding;
    RecyclerView mRecycled;
    EstudianteAdapter eAdapter;
    FirebaseFirestore firestore;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentManageEstudiantesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        eAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        eAdapter.stopListening();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        firestore = FirebaseFirestore.getInstance();
        mRecycled = view.findViewById(R.id.estudiantesRecyclerView);
        mRecycled.setLayoutManager(new LinearLayoutManager(getActivity()));
        Query query = firestore.collection("Estudiantes");

        FirestoreRecyclerOptions<Estudiante> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Estudiante>().setQuery(query, Estudiante.class).build();
        eAdapter = new EstudianteAdapter(firestoreRecyclerOptions, getActivity(), getActivity().getSupportFragmentManager());
        eAdapter.notifyDataSetChanged();
        mRecycled.setAdapter(eAdapter);

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