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

import com.example.iftoolsprototipo.databinding.ActivityAlteraContaBinding;
import com.example.iftoolsprototipo.databinding.ActivityAlterarDadosBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class AlteraContaActivity extends AppCompatActivity {
    private ActivityAlteraContaBinding binding;
    private FirebaseUser mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlteraContaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance().getCurrentUser();

        binding.email.setVisibility(View.INVISIBLE);

        binding.backConta.setOnClickListener(view -> finish());

        binding.salvarBtn.setOnClickListener(view -> {
            if (valida()) {
                atualizaDB();
                atualizaConta();
            }
        });


    }

    public boolean valida() {
        String senha = binding.senha.getText().toString();

        if (senha.length() >= 6) {
            return true;
        }

        if (senha.length() < 6) binding.senha.setError("Vazio");

        return false;
    }

    public void atualizaDB() {
        Toast.makeText(this, "Atualizando...", Toast.LENGTH_SHORT).show();
        String senha = binding.senha.getText().toString();
        String uId = mAuth.getUid();
        Query query = db.collection("users").whereEqualTo("id", uId);

        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                Map<String, Object> updateUser = new HashMap<>();
                updateUser.put("senha", senha);

                documentSnapshot.getReference().update(updateUser).addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Documento atualizado com sucesso", Toast.LENGTH_SHORT).show();
                    binding.senha.setText("");
                }).addOnFailureListener(e -> Toast.makeText(AlteraContaActivity.this, "Não foi possível atualizar", Toast.LENGTH_SHORT).show());
            }
        });
    }

    public void atualizaConta() {
        String senha = binding.senha.getText().toString();

        mAuth.updatePassword(senha).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Atualizou senha", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Não conseguiu alterar Senha", Toast.LENGTH_SHORT).show();
            }
        });

    }

}