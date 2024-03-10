package com.example.iftoolsprototipo;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
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

        binding.cadastroBotao.setOnClickListener(view -> {
            Toast.makeText(this, "Cadastrando...", Toast.LENGTH_SHORT).show();

            if (validarDados()) {
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

        if (!nome.isEmpty() && !telefone.isEmpty() && !email.isEmpty() && !senha.isEmpty()) {
            return true;
        }
        return false;
    }

    public void salvarDados(String nome, String telefone, String email, String senha, String id) {
        Map<String, Object> user = new HashMap<>();
        user.put("Nome", nome);
        user.put("Telefone", telefone);
        user.put("Email", email);
        user.put("Senha", senha);
        user.put("Id", id);

        try {
            db.collection("users").add(user).addOnSuccessListener(documentReference -> {
                Toast.makeText(CadastroActivity.this, "Documento foi escrito com sucesso com o id: " + documentReference.getId(), Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(CadastroActivity.this, "Erro ao adicionar documento" + e, Toast.LENGTH_SHORT).show();
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
                FirebaseUser user = mAuth.getCurrentUser();
                String idUser = user.getUid().toString();
                Toast.makeText(this, "Usuário criado com id: " + idUser, Toast.LENGTH_SHORT).show();
                salvarDados(nome, telefone, email, senha, idUser);
            } else {
                Toast.makeText(CadastroActivity.this, "Autenticação falhou.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}