package com.example.llm.ui.ejercicios.manageEjercicios;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.custom.SimpleCustomValidation;
import com.example.llm.R;
import com.example.llm.databinding.FragmentAddEjercicioPincharBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class EditPincharFragment extends DialogFragment {

    private FragmentAddEjercicioPincharBinding binding;

    FirebaseFirestore firestore;
    AwesomeValidation awesomeValidation;

    Button btnAddEjercicio;
    EditText etIdEjercicio, etEnunciado, etOracion,etEstructura;

    String IDEJERCICIO;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            IDEJERCICIO = getArguments().getString("idEjercicio");
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddEjercicioPincharBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        etIdEjercicio = root.findViewById(R.id.etAddIdEjercicio);
        etIdEjercicio.setEnabled(false);
        etEnunciado = root.findViewById(R.id.etEnunciadoTest);

        etOracion = root.findViewById(R.id.etOracion);
        etEstructura = root.findViewById(R.id.etEstructura);

        btnAddEjercicio = root.findViewById(R.id.btnGuardarCambios);

        firestore = FirebaseFirestore.getInstance();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getEjercicio();

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(etIdEjercicio, ".{1,}","Introduce un ID al ejercicio");
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
                String idEjercicio = etIdEjercicio.getText().toString();
                String enunciado = etEnunciado.getText().toString();
                String oracion = etOracion.getText().toString();
                String estructura = etEstructura.getText().toString();

                if(awesomeValidation.validate()){
                    updateEjercicio(idEjercicio,enunciado,oracion,estructura);
                }else{
                    Toast.makeText(getActivity().getBaseContext(),"Faltan datos",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void getEjercicio(){
        firestore.collection("Ejercicios - Pinchar y Arrastrar").document(IDEJERCICIO).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String idEjercicio = documentSnapshot.getString("idEjercicio");
                String enunciado = documentSnapshot.getString("enunciado");
                String oracion = documentSnapshot.getString("oracion");
                String estructura = documentSnapshot.getString("estructura");

                etIdEjercicio.setText(idEjercicio);
                etEnunciado.setText(enunciado);
                etOracion.setText(oracion);
                etEstructura.setText(estructura);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error al obtener los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateEjercicio(String id, String enunciado, String oracion, String estructura) {
        Map<String,Object> datauser = new HashMap<>();
        datauser.put("idEjercicio",id);
        datauser.put("enunciado",enunciado);
        datauser.put("oracion",oracion);
        datauser.put("estructura",estructura);

        firestore.collection("Ejercicios - Pinchar y Arrastrar").document(IDEJERCICIO).update(datauser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getContext(), "Ejercicio editado", Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error al editar el ejercicio", Toast.LENGTH_SHORT).show();
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
        int height = (getResources().getDisplayMetrics().heightPixels)*8/9;
        getDialog().getWindow().setLayout(width, height);
    }
}
