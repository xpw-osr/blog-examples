package com.simplejourney.securityacl.dto;

import com.simplejourney.securityacl.entities.Note;
import lombok.Data;

import java.io.Serializable;

@Data
public class NoteDTO implements Serializable {
    private Long id;
    private String title;
    private String content;
    private long createDate;
    private long authorId;

    public static NoteDTO from(Note note) {
        NoteDTO dto = new NoteDTO();
        dto.setId(note.getId());
        dto.setTitle(note.getTitle());
        dto.setContent(note.getContent());
        dto.setAuthorId(note.getAuthorId());
        dto.setCreateDate(note.getCreateDate());
        return dto;
    }

    public Note toNote() {
        Note note = new Note();
        note.setId(this.getId());
        note.setTitle(this.getTitle());
        note.setContent(this.getContent());
        note.setAuthorId(this.getAuthorId());
        note.setCreateDate(this.getCreateDate());

        return note;
    }
}
