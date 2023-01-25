package com.example.llm.ui.ejercicios.addEjercicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.llm.R;
import com.example.llm.databinding.FragmentAddEjercicioBinding;
import com.example.llm.databinding.FragmentAddEjercicioObservarBinding;

public class AddEjercicioFragment extends Fragment {

    private FragmentAddEjercicioBinding binding;
    Button btnAddObservar, btnAddTest, btnAddPinchar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddEjercicioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        btnAddObservar = root.findViewById(R.id.btnEjercicioObservar);
        btnAddTest = root.findViewById(R.id.btnEjercicioTest);
        btnAddPinchar = root.findViewById(R.id.btnEjercicioPinchar);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnAddObservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddObservarFragment ob = new AddObservarFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                ob.show(fm,"observar");
            }
        });

        btnAddTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTestFragment ob = new AddTestFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                ob.show(fm,"test");
            }
        });

        btnAddPinchar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPincharFragment ob = new AddPincharFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                ob.show(fm,"pinchar");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}