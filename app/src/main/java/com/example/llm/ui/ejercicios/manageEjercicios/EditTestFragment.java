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
import com.example.llm.R;
import com.example.llm.databinding.FragmentAddEjercicioTestBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditTestFragment extends DialogFragment {

    private FragmentAddEjercicioTestBinding binding;

    FirebaseFirestore firestore;
    AwesomeValidation awesomeValidation;

    Button btnAddEjercicio;
    EditText etIdEjercicio, etEnunciado, etA,etB,etC,etD;
    RadioButton rbA, rbB, rbC, rbD;

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

        binding = FragmentAddEjercicioTestBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        etIdEjercicio = root.findViewById(R.id.etAddIdEjercicio);
        etIdEjercicio.setEnabled(false);
        etEnunciado = root.findViewById(R.id.etEnunciadoTest);

        rbA = root.findViewById(R.id.rbA); etA = root.findViewById(R.id.etRespuestaA);
        rbB = root.findViewById(R.id.rbB); etB = root.findViewById(R.id.etRespuestaB);
        rbC = root.findViewById(R.id.rbC); etC = root.findViewById(R.id.etRespuestaC);
        rbD = root.findViewById(R.id.rbD); etD = root.findViewById(R.id.etRespuestaD);
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
        awesomeValidation.addValidation(etA, ".{1,}","El campo no puede estar vacío");
        awesomeValidation.addValidation(etB, ".{1,}","El campo no puede estar vacío");
        awesomeValidation.addValidation(etC, ".{1,}","El campo no puede estar vacío");
        awesomeValidation.addValidation(etD, ".{1,}","El campo no puede estar vacío");

        btnAddEjercicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idEjercicio = etIdEjercicio.getText().toString();
                String enunciado = etEnunciado.getText().toString();
                String A = etA.getText().toString();
                String B = etB.getText().toString();
                String C = etC.getText().toString();
                String D = etD.getText().toString();
                String sol="";

                if(rbA.isChecked())
                    sol=A;
                if(rbB.isChecked())
                    sol=B;
                if(rbC.isChecked())
                    sol=C;
                if(rbD.isChecked())
                    sol=D;

                if(awesomeValidation.validate()){
                    updateEjercicio(idEjercicio,enunciado,A,B,C,D,sol);
                }else{
                    Toast.makeText(getActivity().getBaseContext(),"Faltan datos",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void getEjercicio(){
        firestore.collection("Ejercicios - Test").document(IDEJERCICIO).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String idEjercicio = documentSnapshot.getString("idEjercicio");
                String enunciado = documentSnapshot.getString("enunciado");
                String sol = documentSnapshot.getString("correcta");
                String a = documentSnapshot.getString("respuesta 1");
                String b = documentSnapshot.getString("respuesta 2");
                String c = documentSnapshot.getString("respuesta 3");
                String d = documentSnapshot.getString("respuesta 4");

                etIdEjercicio.setText(idEjercicio);
                etEnunciado.setText(enunciado);
                etA.setText(a);
                etB.setText(b);
                etC.setText(c);
                etD.setText(d);

                if(sol.equals(a))
                    rbA.setChecked(true);
                else
                    rbA.setChecked(false);
                if(sol.equals(b))
                    rbB.setChecked(true);
                else
                    rbB.setChecked(false);
                if(sol.equals(c))
                    rbC.setChecked(true);
                else
                    rbC.setChecked(false);
                if(sol.equals(d))
                    rbD.setChecked(true);
                else
                    rbD.setChecked(false);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error al obtener los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateEjercicio(String id, String enunciado, String A, String B, String C, String D, String sol) {
        Map<String,Object> datauser = new HashMap<>();
        datauser.put("idEjercicio",id);
        datauser.put("enunciado",enunciado);
        datauser.put("respuesta 1",A);
        datauser.put("respuesta 2",B);
        datauser.put("respuesta 3",C);
        datauser.put("respuesta 4",D);

        if(rbA.isChecked())
            sol=A;
        if(rbB.isChecked())
            sol=B;
        if(rbC.isChecked())
            sol=C;
        if(rbD.isChecked())
            sol=D;

        datauser.put("correcta",sol);

        firestore.collection("Ejercicios - Test").document(IDEJERCICIO).update(datauser).addOnSuccessListener(new OnSuccessListener<Void>() {
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