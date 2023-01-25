package com.example.llm.ui.alumnado.addEstudiante;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.custom.SimpleCustomValidation;
import com.example.llm.R;
import com.example.llm.databinding.FragmentAddEstudianteBinding;
import com.example.llm.ui.alumnado.manageEstudiantes.ManageEstudianteDialogFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class AddEstudianteFragment extends Fragment {

    private FragmentAddEstudianteBinding binding;

    FirebaseFirestore firestore;
    AwesomeValidation awesomeValidation;

    Button btnAddAlumno;
    EditText etNombre, etEmail, etGroup;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddEstudianteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        etNombre = root.findViewById(R.id.etNombre);
        etEmail = root.findViewById(R.id.etEmail);
        etGroup = root.findViewById(R.id.etGroup);
        btnAddAlumno = root.findViewById(R.id.btnAddAlumno);

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firestore = FirebaseFirestore.getInstance();

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(getActivity(),R.id.etEmail, Patterns.EMAIL_ADDRESS,R.string.invalid_mail);
        awesomeValidation.addValidation(getActivity(),R.id.etGroup, ".{1,}",R.string.invalid_curso);
        awesomeValidation.addValidation(getActivity(), R.id.etNombre, new SimpleCustomValidation() {
            @Override
            public boolean compare(String input) {
                StringTokenizer stringTokenizer = new StringTokenizer(etNombre.getText().toString());
                int count= stringTokenizer.countTokens();
                    if (count >= 2)
                        return true;
                    else
                        return false;
            }
        }, R.string.invalid_name);

        awesomeValidation.addValidation(getActivity(), R.id.etNombre, new SimpleCustomValidation() {
            @Override
            public boolean compare(String input) {
                for (int i = 0; i < etNombre.getText().toString().length(); i++) {
                    char caracter = etNombre.getText().toString().toUpperCase().charAt(i);
                    Log.d("REG", String.valueOf(caracter));
                    int valorASCII = (int)caracter;
                    Log.d("REG", String.valueOf(valorASCII));
                    if (valorASCII != 32 && valorASCII != 209 && valorASCII != 193 && valorASCII != 201 && valorASCII != 205  && valorASCII != 211 && valorASCII != 218 && (valorASCII < 65 || valorASCII > 90))
                        return false;
                }
                return true;
            }
        }, R.string.invalid_format_name);

        btnAddAlumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etNombre.getText().toString();
                String email = etEmail.getText().toString();
                String curso = etGroup.getText().toString();

                if(awesomeValidation.validate()){
                    DocumentReference dr = firestore.collection("Estudiantes").document(etEmail.getText().toString());
                    Map<String,Object> datauser = new HashMap<>();
                    datauser.put("email",email);
                    datauser.put("nombre",name);
                    datauser.put("curso",curso);

                    dr.set(datauser).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("REG","Datos creados correctamente");
                            Toast.makeText(getActivity().getBaseContext(), "Estudiante añadido", Toast.LENGTH_SHORT).show();

                            new ManageEstudianteDialogFragment().show(
                                    getChildFragmentManager(), "gestionar est");

                            etNombre.setText("");
                            etEmail.setText("");
                            etGroup.setText("");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("REG","Error al crear docuemnto de estudiantes");
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

}