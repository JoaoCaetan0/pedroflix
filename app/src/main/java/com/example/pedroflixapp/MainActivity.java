package com.example.pedroflixapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edEmail, edSenha;
    TextView tvRecSenha, tvCriaUsuario;
    Button btLogar;
    ProgressBar progressBar;

    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ArrayList<Filme> filmeList = new ArrayList<>();

    ListView listView;
    ArrayAdapter<Filme> filmeArrayAdapter;
    Intent i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        edEmail = findViewById(R.id.editTextEmail);
        edSenha = findViewById(R.id.editTextSenha);
        tvCriaUsuario = findViewById(R.id.textViewAdicionaFilme);
        tvRecSenha = findViewById(R.id.textViewEsqueciSenha);
        btLogar = findViewById(R.id.buttonLogin);
        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        tvCriaUsuario.setOnClickListener(this);
        tvRecSenha.setOnClickListener(this);
        btLogar.setOnClickListener(this);

        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonLogin:
                logar();
                break;
            case R.id.textViewEsqueciSenha:
                i = new Intent(MainActivity.this, RecuperarSenha.class);
                startActivity(i);
                break;
            case R.id.textViewAdicionaFilme:
                i = new Intent(MainActivity.this, CriarUsuario.class);
                startActivity(i);
                break;

        }
    }

    public void logar(){
        String email = edEmail.getText().toString();
        String senha = edSenha.getText().toString();

        if (email.equals("") || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edEmail.setError("Preencha corretamente");
            edEmail.requestFocus();
            return;
        }

        if (senha.equals("")) {
            edSenha.setError("Preencha corretamente");
            edSenha.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    //verificar cadastro via email (link para clicar e ativar cadastro)
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified()) {
                        //se já verificou via email, redireciona login
                        i = new Intent(MainActivity.this, Listagem.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(MainActivity.this, "Verifique conta via email.", Toast.LENGTH_LONG).show();
                        user.sendEmailVerification();
                    }
                } else //login falhou
                    Toast.makeText(MainActivity.this, "Erro ao logar", Toast.LENGTH_LONG).show();

                progressBar.setVisibility(View.GONE);
            }
        });



    }
}