package com.example.llm.ui.ejercicios.addEjercicio;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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
import com.example.llm.databinding.FragmentAddEjercicioTestBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddTestFragment extends DialogFragment {

    private FragmentAddEjercicioTestBinding binding;

    FirebaseFirestore firestore;
    AwesomeValidation awesomeValidation;

    TextView txtTipoEjercicio;
    EditText etAddIdEjercicio, etEnunciado, etRespuesta1, etRespuesta2, etRespuesta3, etRespuesta4;
    RadioButton rb1, rb2, rb3, rb4;
    Button btnAddEjercicio;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddEjercicioTestBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        firestore = FirebaseFirestore.getInstance();

        etAddIdEjercicio = root.findViewById(R.id.etAddIdEjercicio);
        txtTipoEjercicio = root.findViewById(R.id.spinnerTipo);
        etEnunciado = root.findViewById(R.id.etEnunciadoTest);
        rb1 = root.findViewById(R.id.rbA); etRespuesta1 = root.findViewById(R.id.etRespuestaA);
        rb2 = root.findViewById(R.id.rbB); etRespuesta2 = root.findViewById(R.id.etRespuestaB);
        rb3 = root.findViewById(R.id.rbC); etRespuesta3 = root.findViewById(R.id.etRespuestaC);
        rb4 = root.findViewById(R.id.rbD); etRespuesta4 = root.findViewById(R.id.etRespuestaD);
        btnAddEjercicio = root.findViewById(R.id.btnGuardarCambios);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(etAddIdEjercicio, ".{1,}","Introduce un ID al ejercicio");
        awesomeValidation.addValidation(etEnunciado, ".{1,}","El enunciado no puede estar vacío");
        awesomeValidation.addValidation(etRespuesta1, ".{1,}","El campo no puede estar vacío");
        awesomeValidation.addValidation(etRespuesta2, ".{1,}","El campo no puede estar vacío");
        awesomeValidation.addValidation(etRespuesta3, ".{1,}","El campo no puede estar vacío");
        awesomeValidation.addValidation(etRespuesta4, ".{1,}","El campo no puede estar vacío");

        awesomeValidation.addValidation(etRespuesta4, new SimpleCustomValidation() {
            @Override
            public boolean compare(String s) {
                if(rb1.isChecked()||rb2.isChecked()||rb3.isChecked()||rb4.isChecked())
                    return true;
                else
                    return false;
            }
        },"Debe seleccionar alguna opción");

        btnAddEjercicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idEjercicio = etAddIdEjercicio.getText().toString();
                String tipo = txtTipoEjercicio.getText().toString();
                String enunciado = etEnunciado.getText().toString();
                String respuesta1 = etRespuesta1.getText().toString();
                String respuesta2 = etRespuesta2.getText().toString();
                String respuesta3 = etRespuesta3.getText().toString();
                String respuesta4 = etRespuesta4.getText().toString();
                String sol="";

                if(awesomeValidation.validate()){
                    DocumentReference dr = firestore.collection("Ejercicios - Test").document(etAddIdEjercicio.getText().toString());
                    DocumentReference drGeneral = firestore.collection("Ejercicios").document(etAddIdEjercicio.getText().toString());
                    Map<String,Object> datauser = new HashMap<>();
                    datauser.put("idEjercicio",idEjercicio);
                    datauser.put("tipo",tipo);
                    datauser.put("enunciado",enunciado);
                    datauser.put("respuesta 1",respuesta1);
                    datauser.put("respuesta 2",respuesta2);
                    datauser.put("respuesta 3",respuesta3);
                    datauser.put("respuesta 4",respuesta4);

                    if(rb1.isChecked())
                        sol=respuesta1;
                    if(rb2.isChecked())
                        sol=respuesta2;
                    if(rb3.isChecked())
                        sol=respuesta3;
                    if(rb4.isChecked())
                        sol=respuesta4;

                    datauser.put("correcta",sol);

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