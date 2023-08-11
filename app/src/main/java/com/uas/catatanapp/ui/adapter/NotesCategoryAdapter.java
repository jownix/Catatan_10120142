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
import com.uas.catatanapp.model.NoteCategory;
import com.uas.catatanapp.ui.notes.DetailNotesActivity;

import java.util.ArrayList;

public class NotesCategoryAdapter extends RecyclerView.Adapter<NotesCategoryAdapter.MyViewHolder> {
    private Context ctx;
    private ArrayList<NoteCategory> list_category;
    private DatabaseReference DB;
    private FirebaseAuth Auth;

    public NotesCategoryAdapter(Context context, ArrayList<NoteCategory> list_category) {
        this.ctx = context;
        this.list_category = list_category;
    }

    @Override
    public void onBindViewHolder(@NonNull NotesCategoryAdapter.MyViewHolder holder, int position) {
        DB = FirebaseDatabase.getInstance(token.getDB_URL()).getReference();
        Auth = FirebaseAuth.getInstance();
        holder.fNoteCategoryTitle.setText(list_category.get(position).title);
        holder.fNoteCategoryTotal.setText(list_category.get(position).total + " Notes");

        // View on click redirect
        holder.lNoteCategory.setOnClickListener(v -> {
            Intent intent = new Intent(ctx, DetailNotesActivity.class);
            // Pass the category
            intent.putExtra("category", list_category.get(position).title);
            ctx.startActivity(intent);
        });

        // Delete button onclick
        holder.bNoteCategoryDelete.setOnClickListener(v -> {
            // Alert notification
            AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
            alert.setTitle("Hapus Catatan");
            alert.setMessage("Apakah Anda Yakin Akan Menghapus data Catatan Secara Permanen?");
            alert.setPositiveButton("Ya", (dialog, which) -> {
                // Delete data
                DBHelper.deleteNoteCategory(DB, Auth.getUid(), list_category.get(position).title);

                Toast.makeText(ctx, "Hapus Catatan Berhasil !!",
                        Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            });

            alert.setNegativeButton("Tidak", (dialog, which) -> dialog.dismiss());

            alert.show();
        });
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView fNoteCategoryTitle;
        TextView fNoteCategoryTotal;
        Button bNoteCategoryDelete;
        ConstraintLayout lNoteCategory;
        public MyViewHolder(@NonNull View v) {
            super(v);
            fNoteCategoryTitle = v.findViewById(R.id.category_notes_title);
            fNoteCategoryTotal = v.findViewById(R.id.category_notes_total);
            bNoteCategoryDelete = v.findViewById(R.id.category_notes_delete);
            lNoteCategory = v.findViewById(R.id.category_notes_layout);
        }
    }

    @NonNull
    @Override
    public NotesCategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.category_notes, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return list_category.size();
    }

}

