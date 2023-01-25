package com.example.llm.ui.temas.manageTema;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.llm.ui.ListViewEjAdapter;
import com.example.llm.ui.alumnado.Estudiante;
import com.example.llm.ui.ListViewEstAdapter;
import com.example.llm.R;
import com.example.llm.databinding.FragmentEditTemaBinding;
import com.example.llm.ui.ejercicios.Ejercicio;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditTemaFragment extends DialogFragment{

    private FragmentEditTemaBinding binding;

    FirebaseFirestore firestore;
    EditText txtNombreTema, txtNDificultadTema, txtDescriptionTema;
    Spinner spinnerDificultadEditar;
    Button btnGuardarCambios;

    ListViewEstAdapter estAdapter; ListViewEjAdapter ejAdapter;
    RecyclerView recyclerEstView, recyclerEjView;
    SearchView searchEstBar, searchEjBar;
    Query queryEst, queryEjs;

    ArrayList<String> estudiantesFirebase = new ArrayList<>();
    ArrayList<String> estudiantesFinales = new ArrayList<>();

    ArrayList<String> ejerciciosFirebase = new ArrayList<>();
    ArrayList<String> ejerciciosFinales = new ArrayList<>();

    String NTEMA;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            NTEMA = getArguments().getString("id-grupo");
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentEditTemaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        firestore = FirebaseFirestore.getInstance();

        txtNombreTema = root.findViewById(R.id.etEditNombreGrupo);
        txtNDificultadTema = root.findViewById(R.id.txtNDificultadGrupo);
        txtDescriptionTema = root.findViewById(R.id.etDescriptionTema);
        spinnerDificultadEditar= root.findViewById(R.id.spinnerDificultadEditar);

        getTema();

        searchEstBar = root.findViewById(R.id.searchEstBar);
        searchEjBar = root.findViewById(R.id.searchEjBar);
        btnGuardarCambios = root.findViewById(R.id.btnGuardarEditGrupo);

        setUpRecyclerView(root);
        search_view();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = txtNombreTema.getText().toString();
                String dificultad = spinnerDificultadEditar.getSelectedItem().toString();
                String description = txtDescriptionTema.getText().toString();

                if(dificultad.equals("1 (más fácil)"))
                    dificultad="1";
                else if (dificultad.equals("5 (más difícil)"))
                    dificultad="5";

                updateTema(nombre, dificultad, description);
            }
        });
    }

    private void getTema(){
        firestore.collection("Temas").document(NTEMA).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                estudiantesFirebase = (ArrayList<String>) documentSnapshot.get("estudiantes");
                ejerciciosFirebase = (ArrayList<String>) documentSnapshot.get("ejercicios");
                String nombre = documentSnapshot.getString("nombre");
                String dificultad = documentSnapshot.getString("dificultad");
                String description = documentSnapshot.getString("description");

                txtNombreTema.setText(nombre);
                spinnerDificultadEditar.setSelection(Integer.parseInt(dificultad)-1);
                spinnerDificultadEditar.setPrompt("Actual: "+dificultad);
                txtDescriptionTema.setText(description);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error al obtener los datos", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public ArrayList<String> getEstudiantesFirebase(){
        return estudiantesFirebase;
    }

    public ArrayList<String> getEjerciciosFirebase(){
        return ejerciciosFirebase;
    }

    private void updateTema(String nombre, String dificultad, String description) {
        Map<String, Object> map = new HashMap<>();
        map.put("nombre", nombre);
        map.put("dificultad", dificultad);
        map.put("description", description);
        estudiantesFinales= estAdapter.getEstudiantesFinales();
        map.put("estudiantes", estudiantesFinales);
        ejerciciosFinales= ejAdapter.getEjerciciosFinales();
        map.put("ejercicios", ejerciciosFinales);

        firestore.collection("Temas").document(NTEMA).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getContext(), "Tema editado", Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error al editar tema", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setUpRecyclerView(View view) {
        //------------------------Recycler de Estudiantes-----------------------//
        recyclerEstView = view.findViewById(R.id.searchEstRecyclerView);
        recyclerEstView.setLayoutManager(new LinearLayoutManager(getActivity()));
        queryEst = firestore.collection("Estudiantes");

        FirestoreRecyclerOptions<Estudiante> firestoreEstRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Estudiante>().setQuery(queryEst, Estudiante.class).build();

        estAdapter = new ListViewEstAdapter(firestoreEstRecyclerOptions, getActivity(), getActivity().getSupportFragmentManager(),this);
        estAdapter.notifyDataSetChanged();
        recyclerEstView.setAdapter(estAdapter);

        //------------------------Recycler de Ejercicios-----------------------//
        recyclerEjView = view.findViewById(R.id.searchEjRecyclerView);
        recyclerEjView.setLayoutManager(new LinearLayoutManager(getActivity()));
        queryEjs = firestore.collection("Ejercicios");

        FirestoreRecyclerOptions<Ejercicio> firestoreEjRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Ejercicio>().setQuery(queryEjs, Ejercicio.class).build();

        ejAdapter = new ListViewEjAdapter(firestoreEjRecyclerOptions, getActivity(), getActivity().getSupportFragmentManager(),this);
        ejAdapter.notifyDataSetChanged();
        recyclerEjView.setAdapter(ejAdapter);
    }

    private void search_view() {
        searchEstBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                textSearchEst(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                textSearchEst(s);
                return false;
            }
        });

        searchEjBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                textSearchEj(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                textSearchEj(s);
                return false;
            }
        });
    }

    private void textSearchEst(String s) {
        FirestoreRecyclerOptions<Estudiante> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Estudiante>().setQuery(queryEst.orderBy("nombre")
                        .startAt(s).endAt(s+"~"), Estudiante.class).build();

        estAdapter = new ListViewEstAdapter(firestoreRecyclerOptions, getActivity(), getActivity().getSupportFragmentManager(),this);
        estAdapter.startListening();
        recyclerEstView.setAdapter(estAdapter);
    }

    private void textSearchEj(String s) {
        FirestoreRecyclerOptions<Ejercicio> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Ejercicio>().setQuery(queryEjs.orderBy("idEjercicio")
                        .startAt(s).endAt(s+"~"), Ejercicio.class).build();

        ejAdapter = new ListViewEjAdapter(firestoreRecyclerOptions, getActivity(), getActivity().getSupportFragmentManager(),this);
        ejAdapter.startListening();
        recyclerEjView.setAdapter(ejAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        estAdapter.startListening();
        ejAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        estAdapter.stopListening();
        ejAdapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = (getResources().getDisplayMetrics().heightPixels)*8/9;
        getDialog().getWindow().setLayout(width, height);
    }
}