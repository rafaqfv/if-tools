package com.example.iftoolsprototipo;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.iftoolsprototipo.databinding.ActivityRecuperaContaBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;

public class RecuperaContaActivity extends AppCompatActivity {
    private ActivityRecuperaContaBinding binding;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecuperaContaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backRecupera.setOnClickListener(view -> finish());

        binding.recuperarBtn.setOnClickListener(view -> {
            String email = binding.email.getText().toString().trim();
            if (email.isEmpty()) {
                binding.email.setError("Vazio");
                return;
            }

            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(this, task -> {
               if (task.isSuccessful()) {
                   Toast.makeText(this, "Verifique o seu email.", Toast.LENGTH_SHORT).show();
               }
            });
        });

    }
}