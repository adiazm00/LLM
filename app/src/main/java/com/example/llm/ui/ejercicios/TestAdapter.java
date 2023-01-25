package com.example.llm.ui.ejercicios;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.llm.R;
import com.example.llm.ui.ejercicios.manageEjercicios.EditObservarFragment;
import com.example.llm.ui.ejercicios.manageEjercicios.EditTestFragment;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class TestAdapter extends FirestoreRecyclerAdapter<Ejercicio, TestAdapter.ViewHolder> {

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    Activity activity;
    FragmentManager fmanager;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TestAdapter(@NonNull FirestoreRecyclerOptions<Ejercicio> options, Activity activity, FragmentManager fmanager) {
        super(options);
        this.activity = activity;
        this.fmanager = fmanager;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Ejercicio ejercicio) {
        DocumentSnapshot docSnapchot = getSnapshots().getSnapshot(holder.getAbsoluteAdapterPosition());
        String id = docSnapchot.getId();
        holder.idEjercicio.setText(ejercicio.getIdEjercicio());
        holder.enunciado.setText(ejercicio.getEnunciado());
        holder.layout.setBackgroundResource(R.color.purple_100);

        holder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditTestFragment editGrupoFragment = new EditTestFragment();
                Bundle bundle = new Bundle();
                bundle.putString("idEjercicio", id);
                editGrupoFragment.setArguments(bundle);
                editGrupoFragment.show(fmanager,"edit fragment");
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(holder.btnDelete.getContext());
                alert.setTitle("¿Desea eliminar el ejercicio?");
                alert.setMessage("Pulse eliminar para confirmar");
                alert.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteEjercicio(id);
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

    private void deleteEjercicio(String id) {
        firestore.collection("Ejercicios - Test").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(activity.getApplicationContext(), "Ejercicio eliminado correctamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity.getApplicationContext(), "No se ha podido borrar el ejercicio", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_ejercicio, parent, false);
        return new TestAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView idEjercicio, enunciado;
        ImageButton btnEditar, btnDelete;
        ConstraintLayout layout;

        public ViewHolder(@NonNull View view) {
            super(view);

            idEjercicio = view.findViewById(R.id.txtIdEjercicio);
            enunciado = view.findViewById(R.id.txtEnunciado);
            layout = view.findViewById(R.id.mainEjercicioLayout);

            btnEditar = view.findViewById(R.id.btnEditEjericicio);
            btnDelete = view.findViewById(R.id.btnDeleteEjercicio);

        }
    }
}