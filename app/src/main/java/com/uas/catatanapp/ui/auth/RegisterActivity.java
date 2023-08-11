/**
 *   NIM     : 10120142
 *   Nama    : Jhonathan Kenzo
 *   Kelas   : IF4
 */

package com.uas.catatanapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uas.catatanapp.R;
import com.uas.catatanapp.data.helper.DBHelper;
import com.uas.catatanapp.data.helper.EmailHelper;
import com.uas.catatanapp.data.token;
import com.uas.catatanapp.databinding.ActivityRegisterBinding;
import com.uas.catatanapp.ui.MainActivity;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityRegisterBinding binding;
    private DatabaseReference DB;
    private FirebaseAuth Auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Menghilangkan ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Create instance firebase
        DB = FirebaseDatabase.getInstance(token.getDB_URL()).getReference();
        Auth = FirebaseAuth.getInstance();

        // Btn on click action
        binding.tvRegister.setOnClickListener(this);
        binding.btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tvRegister) {
            back();
        } else if (i == R.id.btnRegister) {
            signUp();
        }
    }

    // Back To Login
    private void back() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    // Text Input Vallidation
    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(binding.etEmailRegister.getText().toString())) {
            binding.etEmailRegister.setError("Required");
            result = false;
        } else {
            binding.etEmailRegister.setError(null);
        }

        if (TextUtils.isEmpty(binding.etPasswordRegister.getText().toString())) {
            binding.etPasswordRegister.setError("Required");
            result = false;
        } else {
            binding.etPasswordRegister.setError(null);
        }

        // Min 6
        if(binding.etPasswordRegister.getText().toString().length() < 6) {
            Toast.makeText(RegisterActivity.this, "Password min 6 karakter",
                    Toast.LENGTH_SHORT).show();
        }

        // Must contain @
        if(!binding.etEmailRegister.getText().toString().contains("@")) {
            Toast.makeText(RegisterActivity.this, "Email tidak sesuai dengan ketentuan",
                    Toast.LENGTH_SHORT).show();
        }

        return result;
    }

    // Register
    private void signUp() {
        if (!validateForm()) return;

        String email = binding.etEmailRegister.getText().toString();
        String password = binding.etPasswordRegister.getText().toString();

        Auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if(task.isSuccessful()) {
                        onAuthSuccess(task.getResult().getUser());
                    } else {
                        Toast.makeText(RegisterActivity.this, "Register Gagal",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Auth Success
    private void onAuthSuccess(FirebaseUser user) {
        String name = EmailHelper.usernameFromEmail(user.getEmail());

        // Create User If Not Exist
        DBHelper.saveUser(DB, user.getUid(), name, user.getEmail());

        // Make alert
        Toast.makeText(RegisterActivity.this, "Register Berhasil !",
                Toast.LENGTH_SHORT).show();

        // Move to Main Activity
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        finish();
    }
}