/**
 *   NIM     : 10120142
 *   Nama    : Jhonathan Kenzo
 *   Kelas   : IF4
 */

package com.uas.catatanapp.ui.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uas.catatanapp.R;
import com.uas.catatanapp.data.helper.DBHelper;
import com.uas.catatanapp.data.token;
import com.uas.catatanapp.model.Note;
import com.uas.catatanapp.ui.notes.UpdateNotesActivity;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {
    private Context ctx;
    private ArrayList<Note> list_note;
    private DatabaseReference DB;
    private FirebaseAuth Auth;
    private String category;

    public NotesAdapter(Context context, ArrayList<Note> listNote, String category) {
        this.ctx = context;
        this.list_note = listNote;
        this.category = category;
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.MyViewHolder holder, int position) {
        DB = FirebaseDatabase.getInstance(token.getDB_URL()).getReference();
        Auth = FirebaseAuth.getInstance();
        holder.fTitle.setText(list_note.get(position).title);
        holder.fNote.setText(list_note.get(position).description);
        holder.fCreatedAt.setText(list_note.get(position).created_at);

        // Delete button onclick
        holder.bDelete.setOnClickListener(v -> {
            // Alert notification
            AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
            alert.setTitle("Hapus");
            alert.setMessage("Apakah Anda Yakin Akan Menghapus data Catatan Secara Permanen?");
            alert.setPositiveButton("Ya", (dialog, which) -> {
                // Delete data
                DBHelper.deleteNote(DB, Auth.getUid(), list_note.get(position).category, list_note.get(position).title);

                Toast.makeText(ctx, "Menghapus Catatan Berhasil !!",
                        Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            });

            alert.setNegativeButton("Tidak", (dialog, which) -> dialog.dismiss());

            alert.show();
        });

        // Update button onclick
        holder.bUpdate.setOnClickListener(v -> {
            // Move activity
            Intent intent = new Intent(ctx, UpdateNotesActivity.class);
            // Pass the data
            intent.putExtra("userId", Auth.getUid());
            intent.putExtra("title", list_note.get(position).title);
            intent.putExtra("category", list_note.get(position).category);
            intent.putExtra("description", list_note.get(position).description);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            ctx.startActivity(intent);
        });
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView fTitle;
        TextView fCreatedAt;
        TextView fNote;
        Button bUpdate;
        Button bDelete;
        ConstraintLayout lNote;
        public MyViewHolder(@NonNull View v) {
            super(v);
            fTitle = v.findViewById(R.id.note_title);
            fCreatedAt = v.findViewById(R.id.note_created_at);
            fNote = v.findViewById(R.id.note_note);
            bUpdate = v.findViewById(R.id.note_update);
            bDelete = v.findViewById(R.id.note_delete);
            lNote = v.findViewById(R.id.notes_layout);
        }
    }

    @NonNull
    @Override
    public NotesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.list_notes, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return list_note.size();
    }

}

