package com.example.pedroflixapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.os.Bundle;

public class CriarFilme extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    EditText edNome_, edDuracao_;
    Button btCria;

    FirebaseAuth mAuthCria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_criar_filme);
        edNome_  = findViewById(R.id.editCriaNome);
        edDuracao_  = findViewById(R.id.editCriaDuracao);
        btCria  = findViewById(R.id.buttonCria);
        iniciarFirebase();
        mAuthCria = FirebaseAuth.getInstance();

        btCria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adicionarFilme(edDuracao_.getText().toString(), edNome_.getText().toString());
            }
        });

    }

    private void adicionarFilme(String nome, String duracao) {

        if(edNome_.getText().toString().equals("")){
            edNome_.setError("Preencha corretamente");
            edNome_.requestFocus();
            return;
        }

        if(edDuracao_.getText().toString().equals("")){
            edDuracao_.setError("Preencha corretamente");
            edDuracao_.requestFocus();
            return;
        }
        Filme filme = new Filme();
        filme.setDuracao(edDuracao_.getText().toString());
        filme.setNome(edNome_.getText().toString());
        filme.setId(1);

        databaseReference.child("Filme").
                child(filme.getNome()).
                setValue(filme);

        Toast.makeText(getApplicationContext(), "Filme criado com sucesso", Toast.LENGTH_LONG).show();
        Intent i = new Intent(CriarFilme.this, Listagem.class);
        startActivity(i);

    }

    private void iniciarFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
}