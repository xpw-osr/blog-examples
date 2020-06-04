package com.simplejourney.securityacl.services.impl;

import com.simplejourney.securityacl.utils.AuthUtil;
import com.simplejourney.securityacl.dto.NoteDTO;
import com.simplejourney.securityacl.entities.ShareSetting;
import com.simplejourney.securityacl.entities.Note;
import com.simplejourney.securityacl.entities.User;
import com.simplejourney.securityacl.repositories.NoteRepository;
import com.simplejourney.securityacl.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class NoteServiceImpl implements NoteService {
    @Autowired
    private AclService aclService;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private AuthUtil authUtil;

    @Transactional
    public NoteDTO save(NoteDTO noteDTO) {
        User userEntity = authUtil.getUserEntity();

        Note note = noteDTO.toNote();
        note.setAuthorId(userEntity.getId());
        note.setCreateDate(System.currentTimeMillis());

        Note saved = noteRepository.save(note);

        ObjectIdentity oi = new ObjectIdentityImpl(Note.class, saved.getId());
        MutableAcl acl = ((MutableAclService) aclService).createAcl(oi);

        return NoteDTO.from(saved);
    }

    @PreAuthorize("hasPermission(#noteDTO.id, 'com.simplejourney.securityacl.entities.Note', 1) and hasPermission(#noteDTO.id, 'com.simplejourney.securityacl.entities.Note', 2)")
    public NoteDTO edit(NoteDTO noteDTO) {
        Optional<Note> exists = noteRepository.findById(noteDTO.getId());
        if (!exists.isPresent()) {
            throw new HttpServerErrorException(HttpStatus.NOT_FOUND);
        }

        Note saved = noteRepository.save(noteDTO.toNote());
        return NoteDTO.from(saved);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id, 'com.simplejourney.securityacl.entities.Note', 8)")
    public void remove(long id) {
        noteRepository.deleteById(id);
        this.removeACL(id);
    }

    @PostFilter("hasPermission(filterObject.id, 'com.simplejourney.securityacl.entities.Note', 1)")
    public List<NoteDTO> list() {
        Iterable<Note> noteIterable = noteRepository.findAll();
        if (null == noteIterable) {
            throw new HttpServerErrorException(HttpStatus.NOT_FOUND);
        }

        Iterator<Note> iterator = noteIterable.iterator();
        if (!iterator.hasNext()) {
            throw new HttpServerErrorException(HttpStatus.NOT_FOUND);
        }

        List<NoteDTO> noteDTOS = new ArrayList<>();
        while (iterator.hasNext()) {
            noteDTOS.add(NoteDTO.from(iterator.next()));
        }

        return noteDTOS;
    }

    @PreAuthorize("hasPermission(#id, 'com.simplejourney.securityacl.entities.Note', 1)")
    public NoteDTO getById(long id) {
        Optional<Note> note = noteRepository.findById(id);
        if (!note.isPresent()) {
            throw new HttpServerErrorException(HttpStatus.NOT_FOUND);
        }
        return NoteDTO.from(note.get());
    }

    @Transactional
    @PreAuthorize("hasPermission(#noteId, 'com.simplejourney.securityacl.entities.Note', 4)")
    public boolean share(Long noteId, List<ShareSetting> settings) {
        if (null == settings || settings.isEmpty()) {
            return false;
        }

        ObjectIdentity oi = new ObjectIdentityImpl(Note.class, noteId);
        MutableAcl acl = null;

        try {
            acl = (MutableAcl) aclService.readAclById(oi);
            if (acl != null) {
                clearACEs(acl);
            }
        } catch (NotFoundException ex){
            acl = ((MutableAclService) aclService).createAcl(oi);
        }

        for (ShareSetting setting : settings) {
            acl.insertAce(acl.getEntries().size(), setting.getPermissions(), setting.getSid(), true);
        }

        ((MutableAclService) aclService).updateAcl(acl);
        return true;
    }

    private void removeACL(long noteId) {
        ObjectIdentity oi = new ObjectIdentityImpl(Note.class, noteId);
        ((MutableAclService) aclService).deleteAcl(oi, true);
    }

    private void clearACEs(MutableAcl acl) {
        try {
            int count = acl.getEntries().size();
            for (int index = count - 1; index >= 0; index++) {
                acl.deleteAce(index);
            }
        } catch (NotFoundException ex) {
            return;
        }
    }
}
