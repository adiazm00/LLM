package com.example.llm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText itEmail,itPassword;

    AwesomeValidation awesomeValidation;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseAuth auxAuth = FirebaseAuth.getInstance();
        FirebaseUser user = auxAuth.getCurrentUser();
        if (user != null){
            entrar();
        }

        itEmail = findViewById(R.id.itEmail);
        itPassword = findViewById(R.id.itPassword);

        itEmail.setText("");
        itPassword.setText("");

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.itEmail, Patterns.EMAIL_ADDRESS,R.string.invalid_mail);
        awesomeValidation.addValidation(this,R.id.itPassword, ".{6,}",R.string.invalid_password);
    }

    public void oyente_SignUp (View view){
        Intent i = new Intent(this, RegisterActivity.class );
        startActivity(i);
    }

    public void oyente_SignIn (View view){

        if(awesomeValidation.validate()){
            String email= itEmail.getText().toString();
            String password = itPassword.getText().toString();

            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        entrar();
                    }else{
                        String error = ((FirebaseAuthException) task.getException()).getErrorCode();
                        Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void entrar(){
        try{
            Intent i = new Intent(this, MainActivity.class );
            i.putExtra("mail",itEmail.getText().toString());
            startActivity(i);
        }catch (NullPointerException e){
            Intent i = new Intent(this, MainActivity.class );
            startActivity(i);
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("¿Desea salir?");
        alert.setMessage("Pulsa salir para abandonar la aplicación");
        alert.setPositiveButton("Salir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();
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

}