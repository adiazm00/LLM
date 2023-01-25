package com.example.llm.ui.temas.addTema;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.llm.R;
import com.example.llm.databinding.FragmentAddTemaBinding;
import com.example.llm.ui.alumnado.manageEstudiantes.ManageEstudianteDialogFragment;
import com.example.llm.ui.temas.manageTema.ManageTemaDialogFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddTemaFragment extends Fragment {

    private FragmentAddTemaBinding binding;

    FirebaseFirestore firestore;
    AwesomeValidation awesomeValidation;

    Button btnAddCurso;
    EditText etAddNombreGrupo, etAddDescriptionGrupo;
    Spinner spinnerDiff;
    RadioGroup radioGroup;
    RadioButton cbPAS, cbPLUS, cbJAMAIS, cbRIEN, cbPERSONNE, cbNENINI, cbINFORMAL;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddTemaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        etAddNombreGrupo = root.findViewById(R.id.etAddNombreGrupo);
        etAddDescriptionGrupo = root.findViewById(R.id.etAddDescriptionGrupo);
        spinnerDiff = root.findViewById(R.id.spinnerAddDificultad);
        radioGroup = root.findViewById(R.id.insigniasRadioGroup);
        cbPAS = root.findViewById(R.id.cbPAS);
        cbPLUS = root.findViewById(R.id.cbPLUS);
        cbJAMAIS = root.findViewById(R.id.cbJAMAIS);
        cbRIEN = root.findViewById(R.id.cbRIEN);
        cbPERSONNE = root.findViewById(R.id.cbPERSONE);
        cbNENINI = root.findViewById(R.id.cbNENINI);
        cbINFORMAL = root.findViewById(R.id.cbLINFORMAL);
        btnAddCurso = root.findViewById(R.id.btnGestionarTemas);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firestore = FirebaseFirestore.getInstance();

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(getActivity(),R.id.etAddNombreGrupo, ".{1,}",R.string.invalid_group_name);
        awesomeValidation.addValidation(getActivity(),R.id.etAddDescriptionGrupo, ".{6,}",R.string.invalid_grop_description);


        btnAddCurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = etAddNombreGrupo.getText().toString();
                String description = etAddDescriptionGrupo.getText().toString();
                String dificultad = spinnerDiff.getSelectedItem().toString();
                ArrayList<String> estudiantes = new ArrayList<>();
                ArrayList<String> ejercicios = new ArrayList<>();
                ArrayList<String> insignias = new ArrayList<>();

                if (dificultad.equals("1 (más fácil)"))
                    dificultad = "1";
                else if (dificultad.equals("5 (más difícil)"))
                    dificultad = "5";


                if (cbPAS.isChecked()) {
                    insignias.add("PAS");
                } else
                    insignias.remove("PAS");

                if (cbPLUS.isChecked()) {
                    insignias.add("PLUS");

                } else
                    insignias.remove("PLUS");

                if (cbJAMAIS.isChecked()) {
                    insignias.add("JAMAIS");
                } else
                    insignias.remove("JAMAIS");

                if (cbRIEN.isChecked()){
                    insignias.add("RIEN");
                }else
                    insignias.remove("RIEN");

                if(cbPERSONNE.isChecked()) {
                    insignias.add("PERSONNE");
                }else
                    insignias.remove("PERSONNE");

                if(cbINFORMAL.isChecked()) {
                    insignias.add("INFORMAL");
                }else
                    insignias.remove("INFORMAL");

                if(cbNENINI.isChecked()){
                    insignias.add("NENINI");
                } else
                    insignias.remove("NENINI");

                if(awesomeValidation.validate()){
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    String id = String.valueOf(timestamp.getTime());
                    DocumentReference dr = firestore.collection("Temas").document(id);
                    Map<String,Object> datauser = new HashMap<>();
                    datauser.put("id-grupo",id);
                    datauser.put("nombre",nombre);
                    datauser.put("description",description);
                    datauser.put("dificultad",dificultad);
                    datauser.put("ejercicios",ejercicios);
                    datauser.put("estudiantes",estudiantes);
                    datauser.put("insignias",insignias);
                    dr.set(datauser).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("REG","Datos creados correctamente");
                            Toast.makeText(getActivity().getBaseContext(), "Tema creado", Toast.LENGTH_SHORT).show();

                            new ManageTemaDialogFragment().show(
                                    getChildFragmentManager(), "gestionar est");

                            etAddNombreGrupo.setText("");
                            etAddDescriptionGrupo.setText("");
                            spinnerDiff.setSelection(0);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("REG","Error al crear docuemnto del tema");
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