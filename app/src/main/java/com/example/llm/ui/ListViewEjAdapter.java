package com.example.llm.ui;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.llm.R;
import com.example.llm.ui.ejercicios.Ejercicio;
import com.example.llm.ui.temas.manageTema.EditTemaFragment;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListViewEjAdapter extends FirestoreRecyclerAdapter<Ejercicio, ListViewEjAdapter.ViewHolder> {

    Activity activity;
    FragmentManager fmanager;
    EditTemaFragment editTemaFragment;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    ArrayList<String> ejerciciosFirebase, ejerciciosFinales = new ArrayList<>();

    public ListViewEjAdapter(@NonNull FirestoreRecyclerOptions<Ejercicio> options, Activity activity, FragmentManager fmanager, EditTemaFragment editTemaFragment) {
        super(options);
        this.fmanager = fmanager;
        this.activity = activity;
        this.editTemaFragment = editTemaFragment;
    }

    @Override
    protected void onBindViewHolder(@NonNull ListViewEjAdapter.ViewHolder holder, int position, @NonNull Ejercicio ejercicio) {
        holder.nombre.setText(ejercicio.getIdEjercicio());

        firestore.collection("Estudiantes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ejerciciosFirebase = editTemaFragment.getEjerciciosFirebase();
                            ejerciciosFinales = ejerciciosFirebase;
                            for (int i = 0; i < ejerciciosFirebase.size(); i++){
                                if(ejerciciosFirebase.get(i).equals(holder.nombre.getText().toString())){
                                    Log.d("estList", ejerciciosFirebase.get(i) + " => " + holder.nombre.getText().toString());
                                    holder.checkBox.setChecked(true);
                                }
                            }

                        } else {
                            Log.d("estList", "Error getting documents: ", task.getException());
                        }
                    }
                });

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("estList", "EstudiantesFinales: "+ejerciciosFinales);
                if(holder.checkBox.isChecked()) {
                    ejerciciosFinales.add(holder.nombre.getText().toString());
                }else
                    ejerciciosFinales.remove(holder.nombre.getText().toString());

                Log.d("estList", "EstudiantesFinales: "+ejerciciosFinales);
            }
        });
    }

    public ArrayList<String> getEjerciciosFinales(){
        return ejerciciosFinales;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.txtNombreEstudianteListView);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}