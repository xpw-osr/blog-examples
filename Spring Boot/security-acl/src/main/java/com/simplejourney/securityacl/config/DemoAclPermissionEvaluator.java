package com.simplejourney.securityacl.config;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.acls.domain.DefaultPermissionFactory;
import org.springframework.security.acls.domain.ObjectIdentityRetrievalStrategyImpl;
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.domain.SidRetrievalStrategyImpl;
import org.springframework.security.acls.model.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * If we want to make the data which has not ACL can be visited by any users,
 * we need override this interface
 * 'hasPermission' of PermissionEvaluator will be called before 'isGranted' of PermissionGrantingStrategy
 */

@Component
public class DemoAclPermissionEvaluator implements PermissionEvaluator {
    private AclService aclService;
    private ObjectIdentityRetrievalStrategy objectIdentityRetrievalStrategy = new ObjectIdentityRetrievalStrategyImpl();
    private ObjectIdentityGenerator objectIdentityGenerator = new ObjectIdentityRetrievalStrategyImpl();
    private SidRetrievalStrategy sidRetrievalStrategy = new SidRetrievalStrategyImpl();
    private PermissionFactory permissionFactory = new DefaultPermissionFactory();

    public DemoAclPermissionEvaluator(AclService aclService) {
        this.aclService = aclService;
    }

    public void setPermissionFactory(PermissionFactory permissionFactory) {
        this.permissionFactory = permissionFactory;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object domainObject, Object permission) {
        if (domainObject == null) {
            return false;
        } else {
            ObjectIdentity objectIdentity = this.objectIdentityRetrievalStrategy.getObjectIdentity(domainObject);
            return this.checkPermission(authentication, objectIdentity, permission);
        }
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        ObjectIdentity objectIdentity = this.objectIdentityGenerator.createObjectIdentity(targetId, targetType);
        return this.checkPermission(authentication, objectIdentity, permission);
    }

    private boolean checkPermission(Authentication authentication, ObjectIdentity oid, Object permission) {
        List<Sid> sids = this.sidRetrievalStrategy.getSids(authentication);
        List<Permission> requiredPermission = this.resolvePermission(permission);

        try {
            Acl acl = this.aclService.readAclById(oid, sids);
            if (acl.isGranted(requiredPermission, sids, false)) {
                return true;
            }
        } catch (NotFoundException var8) {
            /**
             * There is not acl data for object,
             * Return true, if data should be public for all visitors;
             * otherwise, false
             */
            return true;
        }

        return false;
    }

    List<Permission> resolvePermission(Object permission) {
        if (permission instanceof Integer) {
            return Arrays.asList(this.permissionFactory.buildFromMask((Integer)permission));
        } else if (permission instanceof Permission) {
            return Arrays.asList((Permission)permission);
        } else if (permission instanceof Permission[]) {
            return Arrays.asList((Permission[])((Permission[])permission));
        } else {
            if (permission instanceof String) {
                String permString = (String)permission;

                Permission p;
                try {
                    p = this.permissionFactory.buildFromName(permString);
                } catch (IllegalArgumentException var5) {
                    p = this.permissionFactory.buildFromName(permString.toUpperCase(Locale.ENGLISH));
                }

                if (p != null) {
                    return Arrays.asList(p);
                }
            }

            throw new IllegalArgumentException("Unsupported permission: " + permission);
        }
    }
}
