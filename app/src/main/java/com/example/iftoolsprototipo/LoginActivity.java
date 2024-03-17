package com.example.iftoolsprototipo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.iftoolsprototipo.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        boolean viuSplash = preferences.getBoolean("viu_splash", false);

        if (!viuSplash) {
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        mAuth = FirebaseAuth.getInstance();

        binding.criarConta.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
            startActivity(intent);
        });

        binding.recuperaConta.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RecuperaContaActivity.class));
        });

        binding.loginBtn.setOnClickListener(view -> {
            if (validaCampos()) {
                binding.progress.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Logando", Toast.LENGTH_SHORT).show();
                signIn();
            }
        });
    }

    public boolean validaCampos() {
        String email = binding.email.getText().toString();
        String password = binding.senha.getText().toString();

        if (!email.isEmpty() && password.length() >= 6) return true;

        if (email.isEmpty()) {
            binding.email.setError("Vazio");
        }
        if (password.length() < 6) {
            binding.senha.setError("Menor do que 6 caracteres");
        }
        return false;
    }

    public void signIn() {
        String email = binding.email.getText().toString();
        String password = binding.senha.getText().toString();
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
            } else {
                binding.progress.setVisibility(View.INVISIBLE);
                Toast.makeText(LoginActivity.this, "Falha ao tentar autenticar.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}