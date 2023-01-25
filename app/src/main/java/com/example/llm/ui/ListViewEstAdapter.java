package com.example.llm.ui;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.llm.R;
import com.example.llm.ui.alumnado.Estudiante;
import com.example.llm.ui.temas.manageTema.EditTemaFragment;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListViewEstAdapter extends FirestoreRecyclerAdapter<Estudiante, ListViewEstAdapter.ViewHolder> {

    Activity activity;
    FragmentManager fmanager;
    EditTemaFragment editTemaFragment;
    View v;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    ArrayList<String> estudiantesFirebase, estudiantesFinales = new ArrayList<>();

    public ListViewEstAdapter(@NonNull FirestoreRecyclerOptions<Estudiante> options, Activity activity, FragmentManager fmanager, EditTemaFragment editTemaFragment) {
        super(options);
        this.fmanager = fmanager;
        this.activity = activity;
        this.editTemaFragment = editTemaFragment;
    }

    @Override
    protected void onBindViewHolder(@NonNull ListViewEstAdapter.ViewHolder holder, int position, @NonNull Estudiante estudiante) {
        DocumentSnapshot docSnapshot = getSnapshots().getSnapshot(holder.getAbsoluteAdapterPosition());
        holder.nombre.setText(estudiante.getNombre());

        ArrayList<String> insignias = (ArrayList<String>) docSnapshot.get("insignias");

        //-------------Datos estáticos para mostrar insignias-----------------\\
        try {
            if (insignias.get(0).equals("PAS: Temps Simples"))
                holder.insignia1.setImageDrawable(v.getResources().getDrawable(R.drawable.badge_1));
            else
                holder.insignia1.setImageDrawable(v.getResources().getDrawable(R.drawable.round_shape));

        }catch (IndexOutOfBoundsException e) {holder.insignia1.setImageDrawable(v.getResources().getDrawable(R.drawable.round_shape));}
        catch (NullPointerException e) {}

        try {
            if (insignias.get(1).equals("PAS: Temps Composés"))
                holder.insignia2.setImageDrawable(v.getResources().getDrawable(R.drawable.badge_2));
            else
                holder.insignia2.setImageDrawable(v.getResources().getDrawable(R.drawable.round_shape));

        }catch (IndexOutOfBoundsException e) {holder.insignia2.setImageDrawable(v.getResources().getDrawable(R.drawable.round_shape));}
        catch (NullPointerException e) {}
        //-------------Datos estáticos para mostrar insignias-----------------\\

        firestore.collection("Estudiantes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            estudiantesFirebase = editTemaFragment.getEstudiantesFirebase();
                            estudiantesFinales = estudiantesFirebase;
                            for (int i = 0; i < estudiantesFirebase.size(); i++){
                                if(estudiantesFirebase.get(i).equals(holder.nombre.getText().toString())){
                                    Log.d("estList", estudiantesFirebase.get(i) + " => " + holder.nombre.getText().toString());
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
                Log.d("estList", "EstudiantesFinales: "+estudiantesFinales);
                if(holder.checkBox.isChecked()) {
                    estudiantesFinales.add(holder.nombre.getText().toString());
                }else
                    estudiantesFinales.remove(holder.nombre.getText().toString());

                Log.d("estList", "EstudiantesFinales: "+estudiantesFinales);
            }
        });
    }

    public ArrayList<String> getEstudiantesFinales(){
        return estudiantesFinales;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_estudiante, parent, false);
        v = view;
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre;
        CheckBox checkBox;
        ImageView insignia1, insignia2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.txtNombreEstudianteListView);
            checkBox = itemView.findViewById(R.id.checkBox);
            insignia1 = itemView.findViewById(R.id.insignia1ListView);
            insignia2 = itemView.findViewById(R.id.insignia2ListView);
        }
    }
}