package com.example.llm;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    Button btnRegister;
    EditText itName, itGroup, itEmail, itPassword, itRepeatPassword;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister= findViewById(R.id.btnRegister);
        itName = findViewById(R.id.itName);
        itGroup = findViewById(R.id.itGroup);
        itEmail = findViewById(R.id.itEmail);
        itPassword = findViewById(R.id.itPassword);
        itRepeatPassword = findViewById(R.id.itRepeatPassword);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.itEmail, Patterns.EMAIL_ADDRESS,R.string.invalid_mail);
        awesomeValidation.addValidation(this,R.id.itPassword, ".{6,}",R.string.invalid_password);
        awesomeValidation.addValidation(this, R.id.itRepeatPassword, R.id.itPassword, R.string.invalid_passwords);
        awesomeValidation.addValidation(this,R.id.itName, ".{3,}",R.string.invalid_name);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String email = itEmail.getText().toString();
                    String password = itPassword.getText().toString();
                    String name = itName.getText().toString();
                    String group = itGroup.getText().toString();

                    if(awesomeValidation.validate()){
                        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    String userID = user.getUid();
                                    DocumentReference dr = firestore.collection("Usuarios").document(userID);
                                    Map<String,Object> datauser = new HashMap<>();
                                    datauser.put("nombre",name);
                                    datauser.put("grupo",group);
                                    dr.set(datauser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d("REG","Datos creados correctamente");
                                            Toast.makeText(RegisterActivity.this, "Usuario creado", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("REG","Error al crear docuemnto de usuario");
                                        }
                                    });
                                }else{
                                    String error = ((FirebaseAuthException) task.getException()).getErrorCode();
                                    Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(RegisterActivity.this,"Faltan datos",Toast.LENGTH_SHORT).show();
                    }
                }
        });
    }
}