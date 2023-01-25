package com.example.llm.ui.alumnado.manageEstudiantes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.llm.R;
import com.example.llm.databinding.FragmentEditEstudianteBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class EditEstudianteFragment extends DialogFragment {

    private FragmentEditEstudianteBinding binding;

    EditText etNombreEdit, etEmailEdit, etCursoEdit;
    String EMAIL;
    Button btnSaveEstudiante;
    FirebaseFirestore firestore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            EMAIL = getArguments().getString("email");
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentEditEstudianteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        etNombreEdit = root.findViewById(R.id.etNombreEdit);
        etEmailEdit = root.findViewById(R.id.etEmailEdit);
        etCursoEdit = root.findViewById(R.id.etGroupEdit);

        btnSaveEstudiante = root.findViewById(R.id.btnSaveEstudiante);

        firestore = FirebaseFirestore.getInstance();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getEstudiante();

        btnSaveEstudiante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = etNombreEdit.getText().toString();
                String email = etEmailEdit.getText().toString();
                String curso = etCursoEdit.getText().toString();

                StringTokenizer stringTokenizer = new StringTokenizer(etNombreEdit.getText().toString());
                int count= stringTokenizer.countTokens();
                if (count >= 2)
                    updateEstudiante(nombre, email, curso);
                else
                    Toast.makeText(getContext(), "Introduzca el nombre completo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getEstudiante(){
        firestore.collection("Estudiantes").document(EMAIL).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String nombre = documentSnapshot.getString("nombre");
                String email = documentSnapshot.getString("email");
                String curso = documentSnapshot.getString("curso");

                etNombreEdit.setText(nombre);
                etEmailEdit.setText(email);
                etCursoEdit.setText(curso);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error al obtener los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateEstudiante(String nombre, String email, String curso) {
        Map<String, Object> map = new HashMap<>();
        map.put("nombre", nombre);
        map.put("email", email);
        map.put("curso", curso);

        firestore.collection("Estudiantes").document(EMAIL).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getContext(), "Estudiante editado", Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error al editar estudiante", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}