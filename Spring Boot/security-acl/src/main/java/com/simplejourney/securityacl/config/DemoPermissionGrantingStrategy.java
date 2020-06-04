package com.simplejourney.securityacl.config;

import com.simplejourney.securityacl.common.Constants;
import com.simplejourney.securityacl.entities.UserGroup;
import com.simplejourney.securityacl.repositories.UserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DemoPermissionGrantingStrategy implements PermissionGrantingStrategy {
    @Autowired
    private UserGroupRepository userGroupRepository;

    @Override
    public boolean isGranted(Acl acl, List<Permission> permissions, List<Sid> sids, boolean b) {
        String username = ((PrincipalSid)sids.get(0)).getPrincipal();

        for (int index = 1; index < sids.size(); index++) {
            if (GrantedAuthoritySid.class.isAssignableFrom(sids.get(index).getClass())) {
                if ("ADMIN".equals(((GrantedAuthoritySid) sids.get(index)).getGrantedAuthority())) {
                    return true;
                }
            }
        }

        // check user is owner or not
        String owner_name = ((PrincipalSid)acl.getOwner()).getPrincipal();
        if (username.equals(owner_name)) {
            return true;
        }

        // check aces
        int authorizedPermission = 0;
        List<AccessControlEntry> aces = acl.getEntries();
        for (AccessControlEntry ace : aces) {
            Sid sid = ace.getSid();

            if (PrincipalSid.class.isAssignableFrom(sid.getClass())) {
                if (((PrincipalSid)sid).getPrincipal().startsWith(Constants.USER_SID_PREFIX)) { // user
                    String authorized_user_name = ((PrincipalSid)sid).getPrincipal().substring(2);
                    if (authorized_user_name.equals(username)) {
                        authorizedPermission |= ace.getPermission().getMask();
                    }
                } else if (((PrincipalSid)sid).getPrincipal().startsWith(Constants.GROUP_SID_PREFIX)) { // group
                    String authorized_group_name = ((PrincipalSid)sid).getPrincipal().substring(2);
                    List<UserGroup> userGroups = userGroupRepository.findUserGroupByUserIsAndGroupIs(username, authorized_group_name);
                    if (null != userGroups) {
                        for (UserGroup ugr : userGroups) {
                            if (ugr.getUser().getName().equals(username)) {
                                authorizedPermission |= ace.getPermission().getMask();
                            }
                        }
                    }
                } else if (((PrincipalSid)sid).getPrincipal().equals(Constants.OTHERS_SID_NAME)) { // public
                    authorizedPermission |= ace.getPermission().getMask();
                }
            }
        }

        return checkPermission(permissions, authorizedPermission);
    }

    private boolean checkPermission(List<Permission> permissions, int mask) {
        for (Permission permission : permissions) {
            if (0 != (mask & permission.getMask())) {
                return true;
            }
        }
        return false;
    }
}
