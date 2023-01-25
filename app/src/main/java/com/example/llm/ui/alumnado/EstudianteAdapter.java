package com.example.llm.ui.alumnado;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.llm.R;
import com.example.llm.ui.alumnado.manageEstudiantes.EditEstudianteFragment;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class EstudianteAdapter extends FirestoreRecyclerAdapter<Estudiante, EstudianteAdapter.ViewHolder> {

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    Activity activity;
    FragmentManager fmanager;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public EstudianteAdapter(@NonNull FirestoreRecyclerOptions<Estudiante> options, Activity activity, FragmentManager fmanager) {
        super(options);
        this.fmanager = fmanager;
        this.activity = activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Estudiante estudiante) {
        DocumentSnapshot docSnapshot = getSnapshots().getSnapshot(holder.getAbsoluteAdapterPosition());
        String id = docSnapshot.getId();
        holder.nombre.setText(estudiante.getNombre());
        holder.email.setText(estudiante.getEmail());


        ArrayList<String> insignias = (ArrayList<String>) docSnapshot.get("insignias");

        //-------------Datos estáticos para mostrar insignias-----------------\\
        try {
            if (insignias.get(0).equals("PAS: Temps Simples"))
                holder.ivPASts.setVisibility(View.VISIBLE);
            else
                holder.ivPASts.setVisibility(View.INVISIBLE);
        }catch (IndexOutOfBoundsException e) {holder.ivPASts.setVisibility(View.INVISIBLE);}
        catch (NullPointerException e) {holder.ivPASts.setVisibility(View.INVISIBLE);}

        try {
            if (insignias.get(1).equals("PAS: Temps Composés"))
                holder.ivPAStc.setVisibility(View.VISIBLE);
            else
                holder.ivPAStc.setVisibility(View.INVISIBLE);
        }catch (IndexOutOfBoundsException e) { holder.ivPAStc.setVisibility(View.INVISIBLE);}
        catch (NullPointerException e) {holder.ivPAStc.setVisibility(View.INVISIBLE);}
        //-------------Datos estáticos para mostrar insignias-----------------\\

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditEstudianteFragment editEstudianteFragment = new EditEstudianteFragment();
                Bundle bundle = new Bundle();
                bundle.putString("email", id);
                editEstudianteFragment.setArguments(bundle);
                editEstudianteFragment.show(fmanager,"edit fragment");
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(holder.btnDelete.getContext());
                alert.setTitle("¿Desea eliminar el estudiante?");
                alert.setMessage("Pulse eliminar para confirmar");
                alert.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteEstudiante(id);
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog =alert.create();
                alertDialog.show();
            }
        });
    }

    private void deleteEstudiante(String id) {
        firestore.collection("Estudiantes").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(activity.getApplicationContext(), "Estudiante eliminado correctamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity.getApplicationContext(), "No se ha podido borrar el estudiante", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_estudiante, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nombre;
        public TextView email;
        ImageView ivPASts, ivPAStc;
        ImageButton btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.txtNombreListView);
            email = itemView.findViewById(R.id.txtEmailListView);
            ivPASts = itemView.findViewById(R.id.ivPASts);
            ivPAStc = itemView.findViewById(R.id.ivPAStc);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDeleteEstudianteGrupo);
        }
    }
}
