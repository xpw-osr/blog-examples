package com.simplejourney.securityacl.controllers;

import com.simplejourney.securityacl.common.Constants;
import com.simplejourney.securityacl.config.DemoBasePermission;
import com.simplejourney.securityacl.dto.NoteDTO;
import com.simplejourney.securityacl.dto.ShareSettingDTO;
import com.simplejourney.securityacl.entities.Group;
import com.simplejourney.securityacl.entities.ShareSetting;
import com.simplejourney.securityacl.entities.User;
import com.simplejourney.securityacl.repositories.GroupRepository;
import com.simplejourney.securityacl.repositories.UserRepositoy;
import com.simplejourney.securityacl.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/note")
public class NoteController {
    @Autowired
    private NoteService noteService;

    @Autowired
    private UserRepositoy userRepositoy;

    @Autowired
    private GroupRepository groupRepository;

    @PostMapping
    public ResponseEntity<NoteDTO> save(@RequestBody NoteDTO noteDTO) throws Exception {
        return ResponseEntity.ok(noteService.save(noteDTO));
    }

    @PutMapping
    public ResponseEntity<NoteDTO> edit(@RequestBody NoteDTO noteDTO) throws Exception {
        return ResponseEntity.ok(noteService.edit(noteDTO));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity remove(@PathVariable long id) throws Exception {
        noteService.remove(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<NoteDTO>> list() throws Exception {
        return ResponseEntity.ok(noteService.list());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<NoteDTO> get(@PathVariable long id) throws Exception {
        return ResponseEntity.ok(noteService.getById(id));
    }

    @PostMapping(value = "/{id}/share")
    public ResponseEntity share(@PathVariable long id, @RequestBody List<ShareSettingDTO> settings) throws Exception {
        List<ShareSetting> shareSettings = settings.stream().map((ShareSettingDTO dto) -> {
            ShareSetting ss = new ShareSetting();
            ss.setPermissions(new DemoBasePermission(dto.getPermissions()));

            switch (dto.getEntityType()) {
                case 0: // user
                    Optional<User> user = userRepositoy.findById(dto.getEntityId());
                    if (user.isPresent()) {
                        ss.setSid(new PrincipalSid(Constants.USER_SID_PREFIX + user.get().getName()));
                    }
                    break;
                case 1: // group
                    Optional<Group> group = groupRepository.findById(dto.getEntityId());
                    if (group.isPresent()) {
                        ss.setSid(new PrincipalSid(Constants.GROUP_SID_PREFIX + group.get().getName()));
                    }
                    break;
                case 2:
                    ss.setSid(new PrincipalSid(Constants.OTHERS_SID_NAME));
                    break;
            }
            return ss;
        }).collect(Collectors.toList());

        if (noteService.share(id, shareSettings)) {
            return ResponseEntity.ok().build();
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
