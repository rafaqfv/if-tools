package com.example.iftoolsprototipo;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.iftoolsprototipo.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();

        binding.logado.setOnClickListener(view -> {
            mAuth.signOut();
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        binding.alterarDados.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, AlterarDadosActivity.class);
            startActivity(intent);
        });

        binding.alteraConta.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, AlteraContaActivity.class);
            startActivity(intent);
        });

    }
}