package com.example.iftoolsprototipo;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    private String senhaFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlteraContaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        senhaFinal = intent.getStringExtra("senha");

        binding.backConta.setOnClickListener(view -> finish());

        binding.salvarBtn.setOnClickListener(view -> {
            if (valida()) {
                atualizaDB();
                atualizaConta();
            }
        });


    }

    public boolean valida() {
        String email = binding.email.getText().toString();
        String senha = binding.senha.getText().toString();

        if (!email.isEmpty() && senha.length() >= 6) {
            return true;
        }

        if (email.isEmpty()) binding.email.setError("Vazio");
        if (senha.length() < 6) binding.senha.setError("Vazio");

        return false;
    }

    public void atualizaDB() {
        Toast.makeText(this, "Atualizando...", Toast.LENGTH_SHORT).show();
        String email = binding.email.getText().toString();
        String senha = binding.senha.getText().toString();
        String uId = mAuth.getUid();
        Query query = db.collection("users").whereEqualTo("id", uId);

        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                Map<String, Object> updateUser = new HashMap<>();
                updateUser.put("email", email);
                updateUser.put("senha", senha);

                documentSnapshot.getReference().update(updateUser).addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Documento atualizado com sucesso", Toast.LENGTH_SHORT).show();
                    binding.email.setText("");
                    binding.senha.setText("");
                }).addOnFailureListener(e -> Toast.makeText(AlteraContaActivity.this, "Não foi possível atualizar", Toast.LENGTH_SHORT).show());
            }
        });
    }

    public void atualizaConta() {
        String email = binding.email.getText().toString();
        String senha = binding.senha.getText().toString();
        AuthCredential credential = EmailAuthProvider
                .getCredential(mAuth.getEmail().toString(), senhaFinal);

        mAuth.reauthenticate(credential).addOnCompleteListener(task -> {
            mAuth.updateEmail(email)
                    .addOnCompleteListener(taskEmail -> {
                        if (taskEmail.isSuccessful()) {
                            Toast.makeText(this, "Atualizou email", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Não conseguiu alterar email", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        mAuth.updatePassword(senha).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Atualizou senha", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Não conseguiu alterar Senha", Toast.LENGTH_SHORT).show();
            }
        });

    }

}