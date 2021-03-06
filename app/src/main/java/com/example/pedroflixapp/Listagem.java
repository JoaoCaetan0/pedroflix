package com.example.pedroflixapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;

public class Listagem extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    TextView tvAddFilme;

    ArrayList<Filme> filmeList = new ArrayList<>();

    ListView listView;
    ArrayAdapter<Filme> filmeArrayAdapter;
    Intent i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_listagem);


        tvAddFilme = findViewById(R.id.textViewAdicionaFilme);
        listView  = findViewById(R.id.listView1);
        tvAddFilme.setOnClickListener(this);
        iniciarFirebase();

        Filme filme = new Filme();
        filme.setDuracao("3h");
        filme.setNome("Vingadores: Ultimato");
        filme.setId(1);

        Filme filme1 = new Filme();
        filme.setDuracao("3h");
        filme1.setNome("Vingadores: Guerra Infinita");
        filme1.setId(1);

        Filme filme2 = new Filme();
        filme.setDuracao("3h");
        filme2.setNome("Vingadores: A era de Ultron");
        filme2.setId(1);

        Filme filme3 = new Filme();
        filme.setDuracao("3h");
        filme3.setNome("Vingadores");
        filme3.setId(1);

        //Inserir
        databaseReference.child("Filme").
                child(filme.getNome()).
                setValue(filme);

        databaseReference.child("Filme").
                child(filme1.getNome()).
                setValue(filme1);

        databaseReference.child("Filme").
                child(filme2.getNome()).
                setValue(filme2);

        databaseReference.child("Filme").
                child(filme3.getNome()).
                setValue(filme3);
//        Listar:
        String palavra = "";
        pesquisarPalavra(palavra);

        //remover
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Filme f1 = filmeList.get(i);

                //se n??o limpar o array, n??o renderiza a lista corretamente
                //ouvinte addValueEventListener repopula ele com add.
                filmeList.clear();

                databaseReference.child("Filme").child(f1.getNome()).removeValue();

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewAdicionaFilme:
                i = new Intent(Listagem.this, CriarFilme.class);
                startActivity(i);
                break;

        }
    }


    private void pesquisarPalavra(String palavra) {
        Query query;

        if (palavra.equals("")) {
            query = databaseReference.child("Filme").orderByChild("nome");
        }
        else{
            query = databaseReference.child("Filme").orderByChild("nome").
                    startAt(palavra).endAt(palavra + "\uf8ff");
        }
        filmeList.clear();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot objDataSnapshot1: dataSnapshot.getChildren()) {
                    Filme f = objDataSnapshot1.getValue(Filme.class);
                    filmeList.add(f);
                }

                filmeArrayAdapter = new ArrayAdapter<>(Listagem.this,
                        android.R.layout.simple_list_item_1, filmeList);

                //n??o esquecer do toString na model pois o arraylist ?? de objetos.
                //n??o esquecer de definir altura para a lista no xml.
                listView.setAdapter(filmeArrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void iniciarFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }
}