/**
 *   NIM     : 10120142
 *   Nama    : Jhonathan Kenzo
 *   Kelas   : IF4
 */

package com.uas.catatanapp.ui.notes;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uas.catatanapp.data.helper.DBHelper;
import com.uas.catatanapp.data.helper.FCMHelper;
import com.uas.catatanapp.data.token;
import com.uas.catatanapp.databinding.ActivityAddNotesBinding;
import com.uas.catatanapp.ui.MainActivity;

public class AddNotesActivity extends AppCompatActivity {

    private DatabaseReference DB;
    private FirebaseAuth Auth;
    private String previousActivity;
    private String category;
    private ActivityAddNotesBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DB = FirebaseDatabase.getInstance(token.getDB_URL()).getReference();
        Auth = FirebaseAuth.getInstance();

        Intent i = getIntent();
        this.previousActivity = i.getStringExtra("backTo");
        this.category = i.getStringExtra("category");


        // Set category
        binding.addNoteCategory.setText(this.category);

        // Save button on click
        binding.addNoteSaveBtn.setOnClickListener(v -> {
            DBHelper.saveNotes(DB,
                    Auth.getCurrentUser().getUid(),
                    binding.addNoteTitle.getText().toString(),
                    binding.addNoteCategory.getText().toString(),
                    binding.addNoteDescription.getText().toString()
            );

            // Mengambil input dari EditText
            String title = binding.addNoteTitle.getText().toString();
            String category = binding.addNoteCategory.getText().toString();

            // Melakukan validasi input
            if (TextUtils.isEmpty(title)) {
                binding.addNoteTitle.setError("Judul tidak boleh kosong");
                return;
            }
            if (TextUtils.isEmpty(category)) {
                binding.addNoteCategory.setError("Kategori tidak boleh kosong");
                return;
            }

            // Make alert
            Toast.makeText(AddNotesActivity.this, "Catatan berhasil dibuat !",
                    Toast.LENGTH_SHORT).show();

            // Make notification
            String token = FCMHelper.getToken(this);
            FCMHelper.sendNotifNewNote(token, binding.addNoteTitle.getText().toString(), binding.addNoteCategory.getText().toString());

            if(this.previousActivity.compareTo("note_category") == 0) {
                goToMainActivity();
            } else {
                goToNoteDetail(this.category);
            }
        });

        // Back button on click
        binding.addNoteBackBtn.setOnClickListener(v -> {
            if(this.previousActivity.compareTo("note_category") == 0) {
                goToMainActivity();
            } else {
                goToNoteDetail(this.category);
            }
        });
    }

    public void goToMainActivity() {
        Intent intent = new Intent(AddNotesActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void goToNoteDetail(String category) {
        Intent intent = new Intent(AddNotesActivity.this, DetailNotesActivity.class);
        intent.putExtra("category", category);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}