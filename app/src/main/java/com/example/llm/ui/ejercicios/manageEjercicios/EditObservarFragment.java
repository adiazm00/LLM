package com.example.llm.ui.ejercicios.manageEjercicios;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.custom.SimpleCustomValidation;
import com.example.llm.R;
import com.example.llm.databinding.FragmentAddEjercicioObservarBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class EditObservarFragment extends DialogFragment {

    private FragmentAddEjercicioObservarBinding binding;

    FirebaseFirestore firestore;
    AwesomeValidation awesomeValidation;

    Button btnAddEjercicio;
    EditText etIdEjercicio, etEnunciado, etA1,etA2,etB1,etB2,etC1,etC2,etD1,etD2;

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

        binding = FragmentAddEjercicioObservarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        etIdEjercicio = root.findViewById(R.id.etAddIdEjercicio);
        etIdEjercicio.setEnabled(false);
        etEnunciado = root.findViewById(R.id.etEnunciadoObservar);

        etA1 = root.findViewById(R.id.etA1); etA2 = root.findViewById(R.id.etA2);
        etB1 = root.findViewById(R.id.etB1); etB2 = root.findViewById(R.id.etB2);
        etC1 = root.findViewById(R.id.etC1); etC2 = root.findViewById(R.id.etC2);
        etD1 = root.findViewById(R.id.etD1); etD2 = root.findViewById(R.id.etD2);
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
                String id = etIdEjercicio.getText().toString();
                String enunciado = etEnunciado.getText().toString();
                String A1 = etA1.getText().toString(); String A2 = etA2.getText().toString();
                String B1 = etB1.getText().toString(); String B2 = etB2.getText().toString();
                String C1 = etC1.getText().toString(); String C2 = etC2.getText().toString();
                String D1 = etD1.getText().toString(); String D2 = etD2.getText().toString();

                if(awesomeValidation.validate()){
                    updateEjercicio(id,enunciado,A1,A2,B1,B2,C1,C2,D1,D2);
                }else{
                    Toast.makeText(getActivity().getBaseContext(),"Faltan datos",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void getEjercicio(){
        firestore.collection("Ejercicios - Observación").document(IDEJERCICIO).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String idEjercicio = documentSnapshot.getString("idEjercicio");
                String enunciado = documentSnapshot.getString("enunciado");

                String a1 = documentSnapshot.getString("A1"); String a2 = documentSnapshot.getString("A2");
                String b1 = documentSnapshot.getString("B1"); String b2 = documentSnapshot.getString("B2");
                String c1 = documentSnapshot.getString("C1"); String c2 = documentSnapshot.getString("C2");
                String d1 = documentSnapshot.getString("D1"); String d2 = documentSnapshot.getString("D2");

                etIdEjercicio.setText(idEjercicio);
                etEnunciado.setText(enunciado);
                etA1.setText(a1); etA2.setText(a2);
                etB1.setText(b1); etB2.setText(b2);
                etC1.setText(c1); etC2.setText(c2);
                etD1.setText(d1); etD2.setText(d2);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error al obtener los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateEjercicio(String id, String enunciado, String A1, String A2, String B1, String B2, String C1, String C2, String D1, String D2) {
        Map<String,Object> datauser = new HashMap<>();
        datauser.put("idEjercicio",id);
        datauser.put("enunciado",enunciado);
        datauser.put("A1",A1); datauser.put("A2",A2);
        datauser.put("B1",B1); datauser.put("B2",B2);
        datauser.put("C1",C1); datauser.put("C2",C2);
        datauser.put("D1",D1); datauser.put("D2",D2);

        firestore.collection("Ejercicios - Observación").document(IDEJERCICIO).update(datauser).addOnSuccessListener(new OnSuccessListener<Void>() {
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