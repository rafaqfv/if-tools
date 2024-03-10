package com.example.iftoolsprototipo;

import android.content.Intent;
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
import com.google.firebase.auth.AuthResult;
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
        mAuth = FirebaseAuth.getInstance();

        binding.criarConta.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
            startActivity(intent);
        });

        binding.loginButton.setOnClickListener(view -> {
            if (validaCampos()) {
                Toast.makeText(this, "Logando", Toast.LENGTH_SHORT).show();
                signIn();
            } ;
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

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Falha ao tentar autenticar.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}