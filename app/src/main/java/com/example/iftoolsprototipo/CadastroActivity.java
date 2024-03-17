package com.example.iftoolsprototipo;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.iftoolsprototipo.databinding.ActivityCadastroBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CadastroActivity extends AppCompatActivity {
    private ActivityCadastroBinding binding;
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();

        binding.backCadastro.setOnClickListener(view -> finish());
        binding.cadastroBotao.setOnClickListener(view -> {
            if (validarDados()) {
                Toast.makeText(this, "Cadastrando...", Toast.LENGTH_SHORT).show();
                binding.progress.setVisibility(View.VISIBLE);
                cadastrar();
            }
        });
    }

    public boolean validarDados() {
        String nome, telefone, email, senha;
        nome = binding.nome.getText().toString();
        telefone = binding.telefone.getText().toString();
        email = binding.email.getText().toString();
        senha = binding.senha.getText().toString();

        if (!nome.isEmpty() && !telefone.isEmpty() && !email.isEmpty() && senha.length() >= 6)
            return true;

        if (nome.isEmpty()) {
            binding.nome.setError("Nome Vazio");
        }
        if (telefone.isEmpty()) {
            binding.telefone.setError("Telefone Vazio");
        }
        if (email.isEmpty()) {
            binding.email.setError("Email Vazio");
        }
        if (senha.length() < 6) {
            binding.senha.setError("Senha menor que 6 caracteres");
        }
        return false;
    }

    public void salvarDados(Usuario user) {
        try {
            db.collection("users").add(user)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "Documento adicionado com id: " + documentReference.getId());
                        Intent intent = new Intent(CadastroActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }).addOnFailureListener(e -> {
                        Log.w(TAG, "Falha ao adicionar documento", e);
                    });
        } catch (Exception e) {
            Toast.makeText(this, "Excessão" + e, Toast.LENGTH_SHORT).show();
        }
    }

    public void cadastrar() {
        String nome, telefone, email, senha;
        nome = binding.nome.getText().toString();
        telefone = binding.telefone.getText().toString();
        email = binding.email.getText().toString();
        senha = binding.senha.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                String idUser = firebaseUser.getUid().toString();
                Log.d(TAG, "signInWithEmail:success");
                Usuario user = new Usuario(nome, telefone, email, senha, idUser);
                salvarDados(user);
            } else {
                Toast.makeText(CadastroActivity.this, "Autenticação falhou.", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "signInWithEmail:failure", task.getException());
            }
        });
    }
}