package com.example.iftoolsprototipo;

import static android.content.ContentValues.TAG;

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

import com.example.iftoolsprototipo.databinding.ActivityAlterarDadosBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class AlterarDadosActivity extends AppCompatActivity {
    private ActivityAlterarDadosBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlterarDadosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance().getCurrentUser();

        binding.salvarBtn.setOnClickListener(view -> {
            if (valida()) {
                atualiza();
                binding.progress.setVisibility(View.INVISIBLE);
            }
        });

    }

    public boolean valida() {
        String nome = binding.nome.getText().toString();
        String telefone = binding.telefone.getText().toString();

        if (!nome.isEmpty() && !telefone.isEmpty()) {
            return true;
        }

        if (nome.isEmpty()) binding.nome.setError("Vazio");
        if (telefone.isEmpty()) binding.telefone.setError("Vazio");

        return false;
    }

    public void atualiza() {
        binding.progress.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Atualizando...", Toast.LENGTH_SHORT).show();
        String nome = binding.nome.getText().toString();
        String telefone = binding.telefone.getText().toString();
        String uId = mAuth.getUid();
        Query query = db.collection("users").whereEqualTo("id", uId);

        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                Map<String, Object> updateUser = new HashMap<>();
                updateUser.put("nome", nome);
                updateUser.put("telefone", telefone);

                documentSnapshot.getReference().update(updateUser).addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Documento atualizado com sucesso", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AlterarDadosActivity.this, "Não foi possível atualizar", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}