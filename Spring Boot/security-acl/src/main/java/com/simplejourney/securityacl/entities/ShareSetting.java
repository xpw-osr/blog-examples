package com.simplejourney.securityacl.entities;

import lombok.Data;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;

@Data
public class ShareSetting {
    private Sid sid;
    private Permission permissions;
}
