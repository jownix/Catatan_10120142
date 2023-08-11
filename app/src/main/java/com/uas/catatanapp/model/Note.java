/**
 *   NIM     : 10120142
 *   Nama    : Jhonathan Kenzo
 *   Kelas   : IF4
 */

package com.uas.catatanapp.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Note {
    public String user_id;
    public String title;
    public String description;
    public String category;
    public String created_at;
    public String updated_at;

    public Note() {
        // Default constructor required for calls to DataSnapshot.getValue(Note.class)
    }

    public Note(String user_id, String title, String description, String category, String created_at, String updated_at) {
        this.user_id = user_id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("user_id", this.user_id);
        result.put("title", this.title);
        result.put("description", this.description);
        result.put("category", this.category);
        result.put("created_at", this.created_at);
        result.put("updated_at", this.updated_at);

        return result;
    }
}

