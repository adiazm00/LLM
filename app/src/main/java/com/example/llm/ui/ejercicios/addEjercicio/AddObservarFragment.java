package com.example.llm.ui.ejercicios.addEjercicio;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.custom.SimpleCustomValidation;
import com.example.llm.R;
import com.example.llm.databinding.FragmentAddEjercicioObservarBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class AddObservarFragment extends DialogFragment {

    private FragmentAddEjercicioObservarBinding binding;

    FirebaseFirestore firestore;
    AwesomeValidation awesomeValidation;

    Button btnAddEjercicio;
    EditText etAddIdEjercicio, etEnunciado, etA1,etA2,etB1,etB2,etC1,etC2,etD1,etD2;
    TextView txtTipoEjercicio;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddEjercicioObservarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        firestore = FirebaseFirestore.getInstance();

        etAddIdEjercicio = root.findViewById(R.id.etAddIdEjercicio);
        txtTipoEjercicio = root.findViewById(R.id.spinnerTipo);
        etEnunciado = root.findViewById(R.id.etEnunciadoObservar);
        etA1 = root.findViewById(R.id.etA1); etA2 = root.findViewById(R.id.etA2);
        etB1 = root.findViewById(R.id.etB1); etB2 = root.findViewById(R.id.etB2);
        etC1 = root.findViewById(R.id.etC1); etC2 = root.findViewById(R.id.etC2);
        etD1 = root.findViewById(R.id.etD1); etD2 = root.findViewById(R.id.etD2);
        btnAddEjercicio = root.findViewById(R.id.btnGuardarCambios);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(etAddIdEjercicio, ".{1,}","Introduce un ID al ejercicio");
        awesomeValidation.addValidation(etEnunciado, ".{1,}","El enunciado no puede estar vacío");
        awesomeValidation.addValidation(etA1, ".{1,}","El campo no puede estar vacío");
        awesomeValidation.addValidation(etA2, ".{1,}","El campo no puede estar vacío");
        awesomeValidation.addValidation(etB1, ".{1,}","El campo no puede estar vacío");
        awesomeValidation.addValidation(etB2, ".{1,}","El campo no puede estar vacío");
        awesomeValidation.addValidation(etC1, ".{1,}","El campo no puede estar vacío");
        awesomeValidation.addValidation(etC2, ".{1,}","El campo no puede estar vacío");
        awesomeValidation.addValidation(etD1, ".{1,}","El campo no puede estar vacío");
        awesomeValidation.addValidation(etD2, ".{1,}","El campo no puede estar vacío");

        awesomeValidation.addValidation(etD2, new SimpleCustomValidation() {
            @Override
            public boolean compare(String s) {
                StringTokenizer tokA2 = new StringTokenizer(etA2.getText().toString(),",");
                StringTokenizer tokB2 = new StringTokenizer(etB2.getText().toString(),",");
                StringTokenizer tokC2 = new StringTokenizer(etC2.getText().toString(),",");
                StringTokenizer tokD2 = new StringTokenizer(etD2.getText().toString(),",");

                int countA= tokA2.countTokens();
                int countB= tokB2.countTokens();
                int countC= tokC2.countTokens();
                int countD= tokD2.countTokens();
                if (countA >= 2 && countB >= 2 && countC >= 2 && countD >= 2)
                    return true;
                else
                    return false;
            }
        },"Separe las partículas negativas por comas (,)");

        btnAddEjercicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = etAddIdEjercicio.getText().toString();
                String tipo = txtTipoEjercicio.getText().toString();
                String enunciado = etEnunciado.getText().toString();
                String A1 = etA1.getText().toString(); String A2 = etA2.getText().toString();
                String B1 = etB1.getText().toString(); String B2 = etB2.getText().toString();
                String C1 = etC1.getText().toString(); String C2 = etC2.getText().toString();
                String D1 = etD1.getText().toString(); String D2 = etD2.getText().toString();

                if(awesomeValidation.validate()){
                    DocumentReference dr = firestore.collection("Ejercicios - Observación").document(etAddIdEjercicio.getText().toString());
                    DocumentReference drGeneral = firestore.collection("Ejercicios").document(etAddIdEjercicio.getText().toString());
                    Map<String,Object> datauser = new HashMap<>();
                    datauser.put("idEjercicio",id);
                    datauser.put("tipo",tipo);
                    datauser.put("enunciado",enunciado);
                    datauser.put("A1",A1); datauser.put("A2",A2);
                    datauser.put("B1",B1); datauser.put("B2",B2);
                    datauser.put("C1",C1); datauser.put("C2",C2);
                    datauser.put("D1",D1); datauser.put("D2",D2);

                    dr.set(datauser).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("REG","Datos creados correctamente");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("REG","Error al crear docuemnto de ejercicio");
                        }
                    });

                    drGeneral.set(datauser).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("REG","Datos creados correctamente");
                            Toast.makeText(getActivity().getBaseContext(), "Ejercicio añadido", Toast.LENGTH_SHORT).show();
                            getDialog().dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("REG","Error al crear docuemnto de ejercicio");
                        }
                    });
                }else{
                    Toast.makeText(getActivity().getBaseContext(),"Faltan datos",Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        int height = getResources().getDisplayMetrics().heightPixels;
        getDialog().getWindow().setLayout(width, height);
    }
}