package com.example.llm.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.llm.R;
import com.example.llm.databinding.FragmentMainBinding;
import com.example.llm.ui.alumnado.manageEstudiantes.ManageEstudianteDialogFragment;
import com.example.llm.ui.ejercicios.manageEjercicios.ManageEjercicioDialogFragment;
import com.example.llm.ui.insignias.InsigniasDialogFragment;
import com.example.llm.ui.temas.manageTema.ManageTemaDialogFragment;

public class MainFragment extends Fragment implements View.OnClickListener{

    private FragmentMainBinding binding;

    Button btnGestionarAlumnado, btnGestionarEjercicios, btnGestionarTemas, btnVerInsignias;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        btnGestionarAlumnado = root.findViewById(R.id.btnGestionarAlumnado);
        btnGestionarEjercicios = root.findViewById(R.id.btnGestionarEjercicios);
        btnGestionarTemas = root.findViewById(R.id.btnGestionarTemas);
        btnVerInsignias = root.findViewById(R.id.btnVerInsignias);

        btnGestionarAlumnado.setOnClickListener(this);
        btnGestionarEjercicios.setOnClickListener(this);
        btnGestionarTemas.setOnClickListener(this);
        btnVerInsignias.setOnClickListener(this);

        return root;
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnGestionarAlumnado:
                new ManageEstudianteDialogFragment().show(
                        getChildFragmentManager(), "gestionar est");
                break;

            case R.id.btnGestionarEjercicios:
                new ManageEjercicioDialogFragment().show(
                        getChildFragmentManager(), "gestionar ejs");
                break;

            case R.id.btnGestionarTemas:
                new ManageTemaDialogFragment().show(
                        getChildFragmentManager(), "gestionar ejs");
                break;
            case R.id.btnVerInsignias:
                new InsigniasDialogFragment().show(
                        getChildFragmentManager(), "gestionar ejs");
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}