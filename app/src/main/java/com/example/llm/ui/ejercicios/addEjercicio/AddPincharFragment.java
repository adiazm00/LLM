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
import com.example.llm.databinding.FragmentAddEjercicioPincharBinding;
import com.example.llm.databinding.FragmentAddEjercicioTestBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class AddPincharFragment extends DialogFragment {

    private FragmentAddEjercicioPincharBinding binding;
    FirebaseFirestore firestore;
    AwesomeValidation awesomeValidation;

    TextView txtTipoEjercicio;
    EditText etAddIdEjercicio, etEnunciado, etOracion, etEstructura;
    Button btnAddEjercicio;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddEjercicioPincharBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        firestore = FirebaseFirestore.getInstance();

        etAddIdEjercicio = root.findViewById(R.id.etAddIdEjercicio);
        txtTipoEjercicio = root.findViewById(R.id.spinnerTipo);
        etEnunciado = root.findViewById(R.id.etEnunciadoTest);
        etOracion = root.findViewById(R.id.etOracion);
        etEstructura = root.findViewById(R.id.etEstructura);

        btnAddEjercicio = root.findViewById(R.id.btnGuardarCambios);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(etAddIdEjercicio, ".{1,}","Introduce un ID al ejercicio");
        awesomeValidation.addValidation(etEnunciado, ".{1,}","El enunciado no puede estar vacío");

        awesomeValidation.addValidation(etOracion, new SimpleCustomValidation() {
            @Override
            public boolean compare(String s) {
                StringTokenizer stringTokenizer = new StringTokenizer(etOracion.getText().toString(),",");
                int count= stringTokenizer.countTokens();
                if (count >= 2)
                    return true;
                else
                    return false;
            }
        },"Escriba una oración separando elementos por comas (,)");

        awesomeValidation.addValidation(etEstructura, new SimpleCustomValidation() {
            @Override
            public boolean compare(String s) {
                StringTokenizer stringTokenizer = new StringTokenizer(etEstructura.getText().toString(),",");
                int count= stringTokenizer.countTokens();
                if (count >= 2)
                    return true;
                else
                    return false;
            }
        },"Escriba una oración separando elementos por comas (,)");

        btnAddEjercicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idEjercicio = etAddIdEjercicio.getText().toString();
                String tipo = txtTipoEjercicio.getText().toString();
                String enunciado = etEnunciado.getText().toString();
                String oracion = etOracion.getText().toString();
                String estructura = etEstructura.getText().toString();

                if(awesomeValidation.validate()){
                    DocumentReference dr = firestore.collection("Ejercicios - Pinchar y Arrastrar").document(etAddIdEjercicio.getText().toString());
                    DocumentReference drGeneral = firestore.collection("Ejercicios").document(etAddIdEjercicio.getText().toString());
                    Map<String,Object> datauser = new HashMap<>();
                    datauser.put("idEjercicio",idEjercicio);
                    datauser.put("tipo",tipo);
                    datauser.put("enunciado",enunciado);
                    datauser.put("oracion",oracion);
                    datauser.put("estructura",estructura);

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