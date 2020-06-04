package com.simplejourney.securityacl.services;

import com.simplejourney.securityacl.dto.NoteDTO;
import com.simplejourney.securityacl.entities.ShareSetting;

import java.util.List;

public interface NoteService {
    NoteDTO save(NoteDTO noteDTO) throws Exception;
    NoteDTO edit(NoteDTO noteDTO) throws Exception;
    void remove(long id) throws Exception;
    List<NoteDTO> list() throws Exception;
    NoteDTO getById(long id) throws Exception;
    boolean share(Long noteId, List<ShareSetting> settings) throws Exception;
}
