package com.example.llm.ui.temas;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.llm.R;
import com.example.llm.ui.temas.manageTema.EditTemaFragment;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class TemaAdapter extends FirestoreRecyclerAdapter<Tema, TemaAdapter.ViewHolder> {

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    Activity activity;
    FragmentManager fmanager;
    View v;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TemaAdapter(@NonNull FirestoreRecyclerOptions<Tema> options, Activity activity, FragmentManager fmanager) {
        super(options);
        this.activity = activity;
        this.fmanager = fmanager;
    }

    @Override
    protected void onBindViewHolder(@NonNull TemaAdapter.ViewHolder holder, int position, @NonNull Tema tema) {
        DocumentSnapshot docSnapchot = getSnapshots().getSnapshot(holder.getAbsoluteAdapterPosition());
        String id = docSnapchot.getId();
        holder.nombre.setText(tema.getNombre());
        holder.description.setText(tema.getDescription());
        holder.dificultad.setText(tema.getDificultad());

        switch (holder.dificultad.getText().toString()){
            case "1":
                holder.cardTemaLayout.setBackgroundResource(R.color.dif1);
                break;
            case "2":
                holder.cardTemaLayout.setBackgroundResource(R.color.dif2);
                break;
            case "3":
                holder.cardTemaLayout.setBackgroundResource(R.color.dif3);
                break;
            case "4":
                holder.cardTemaLayout.setBackgroundResource(R.color.dif4);
                break;
            case "5":
                holder.cardTemaLayout.setBackgroundResource(R.color.dif5);
                break;
        }

        String ejs = Integer.toString(tema.getEjercicios().size());
        holder.ejercicios.setText(ejs);

        String est = Integer.toString(tema.getEstudiantes().size());
        holder.estudiantes.setText(est);

        if(tema.getInsignias().isEmpty()) {
            holder.insigniasTXT.setText("Sin insignias");
        }

        for(int i=0; i < tema.getInsignias().size(); i++) {
            switch (tema.getInsignias().get(i)) {
                case "PAS":
                    holder.insignia1.setImageDrawable(v.getResources().getDrawable(R.drawable.badge_1));
                    holder.insignia2.setImageDrawable(v.getResources().getDrawable(R.drawable.badge_2));
                    break;
                case "PLUS":
                    holder.insignia1.setImageDrawable(v.getResources().getDrawable(R.drawable.badge_3));
                    holder.insignia2.setImageDrawable(v.getResources().getDrawable(R.drawable.badge_4));
                    break;
                case "JAMAIS":
                    holder.insignia1.setImageDrawable(v.getResources().getDrawable(R.drawable.badge_6));
                    holder.insignia2.setImageDrawable(v.getResources().getDrawable(R.drawable.badge_5));
                    break;
                case "RIEN":
                    holder.insignia1.setImageDrawable(v.getResources().getDrawable(R.drawable.badge_11));
                    holder.insignia2.setImageDrawable(v.getResources().getDrawable(R.drawable.badge_12));
                    break;
                case "NENINI":
                    holder.insignia1.setImageDrawable(v.getResources().getDrawable(R.drawable.badge_13));
                    holder.insignia2.setImageDrawable(v.getResources().getDrawable(R.drawable.badge_14));
                    break;
                case "INFORMAL":
                    holder.insignia1.setImageDrawable(v.getResources().getDrawable(R.drawable.badge_9));
                    holder.insignia2.setImageDrawable(v.getResources().getDrawable(R.drawable.badge_10));
                    break;
                case "PERSONNE":
                    holder.insignia1.setImageDrawable(v.getResources().getDrawable(R.drawable.badge_7));
                    holder.insignia2.setImageDrawable(v.getResources().getDrawable(R.drawable.badge_8));
                    break;
            }
        }

        Log.d("estList", "Total: "+tema.getEjercicios()+"/"+tema.getEstudiantes());

        holder.btnEditarGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditTemaFragment editTemaFragment = new EditTemaFragment();
                Bundle bundle = new Bundle();
                bundle.putString("id-grupo", id);
                editTemaFragment.setArguments(bundle);
                editTemaFragment.show(fmanager,"edit fragment");
            }
        });

        holder.btnDeleteGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(holder.btnDeleteGrupo.getContext());
                alert.setTitle("¿Desea eliminar el tema?");
                alert.setMessage("Pulse eliminar para confirmar la eliminación");
                alert.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteGrupo(id);
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

    private void deleteGrupo(String id) {
        firestore.collection("Temas").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                    Toast.makeText(activity.getApplicationContext(), "Tema eliminado correctamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity.getApplicationContext(), "No se ha podido borrar el tema", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public TemaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_tema, parent, false);
        v=view;
        return new ViewHolder(view);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, description,dificultad ,estudiantes, ejercicios, insigniasTXT;
        ImageButton btnEditarGrupo, btnDeleteGrupo;
        ImageView insignia1, insignia2;
        ConstraintLayout cardTemaLayout;

        public ViewHolder(@NonNull View view) {
            super(view);

            nombre = view.findViewById(R.id.txtNombreGrupo);
            description = view.findViewById(R.id.txtDescriptionGrupo);
            dificultad = view.findViewById(R.id.txtNDificultadGrupo);
            estudiantes = view.findViewById(R.id.txtNEstudiantesGrupo);
            ejercicios = view.findViewById(R.id.txtNEjerciciosTema);
            cardTemaLayout = view.findViewById(R.id.cardTemaLayout);
            insigniasTXT = view.findViewById(R.id.txtInsigniasTema);
            insignia1 = view.findViewById(R.id.insignia1);
            insignia2 = view.findViewById(R.id.insignia2);
            btnEditarGrupo = view.findViewById(R.id.btnEditGrupo);
            btnDeleteGrupo = view.findViewById(R.id.btnDeleteGrupo);

        }
    }
}
