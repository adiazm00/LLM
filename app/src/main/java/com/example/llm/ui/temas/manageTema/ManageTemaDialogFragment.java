package com.example.llm.ui.temas.manageTema;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.llm.ui.temas.Tema;
import com.example.llm.ui.temas.TemaAdapter;
import com.example.llm.R;
import com.example.llm.databinding.FragmentManageTemasBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ManageTemaDialogFragment extends DialogFragment {

    private FragmentManageTemasBinding binding;
    RecyclerView mRecycled;
    TemaAdapter gAdapter;
    FirebaseFirestore firestore;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentManageTemasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        mRecycled = view.findViewById(R.id.gruposRecyclerView);
        mRecycled.setLayoutManager(new LinearLayoutManager(getActivity()));
        Query query = firestore.collection("Temas");

        FirestoreRecyclerOptions<Tema> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Tema>().setQuery(query, Tema.class).build();
        gAdapter = new TemaAdapter(firestoreRecyclerOptions, getActivity(), getActivity().getSupportFragmentManager());
        gAdapter.notifyDataSetChanged();
        mRecycled.setAdapter(gAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        gAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        gAdapter.stopListening();
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