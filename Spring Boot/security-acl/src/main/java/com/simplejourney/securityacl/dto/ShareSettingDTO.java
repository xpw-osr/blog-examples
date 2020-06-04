package com.simplejourney.securityacl.dto;

import lombok.Data;

@Data
public class ShareSettingDTO {
    private int entityType; // 0 - user, 1 - group, 2 - public
    private long entityId;
    private int permissions;
}
