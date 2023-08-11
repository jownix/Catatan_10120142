/**
 *   NIM     : 10120142
 *   Nama    : Jhonathan Kenzo
 *   Kelas   : IF4
 */
package com.uas.catatanapp.data.helper;

import com.google.firebase.database.DatabaseReference;
import com.uas.catatanapp.model.Note;
import com.uas.catatanapp.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DBHelper {
    public static void saveUser(DatabaseReference DB, String userId, String name, String email) {
        User user = new User(name, email);

        DB.child("users")
                .child(userId)
                .setValue(user);
    }

    public static void saveNotes(DatabaseReference DB, String userId, String title, String category, String note) {
        SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'");
        String now = newDateFormat.format(new Date());

    // Ubah format tanggal untuk hanya tahun, tanggal, dan bulan
        SimpleDateFormat desiredDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = newDateFormat.parse(now);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = desiredDateFormat.format(date);

        Note notes = new Note(userId, title, note, category, formattedDate, formattedDate);


        DB.child("notes")
                .child(userId)
                .child(category)
                .child(title)
                .setValue(notes);
    }

    public static void deleteNoteCategory(DatabaseReference DB, String userId, String category) {
        DB.child("notes")
                .child(userId)
                .child(category)
                .removeValue();
    }

    public static void deleteNote(DatabaseReference DB, String userId, String category, String title) {
        DB.child("notes")
                .child(userId)
                .child(category)
                .child(title)
                .removeValue();
    }
}

