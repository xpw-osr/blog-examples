/*
 * ACL Tables
 * see '22.3 ACL Schema' from 'https://docs.spring.io/spring-security/site/docs/5.3.3.BUILD-SNAPSHOT/reference/html5/#dbschema-acl'
 */

-- ACL_SID table, allows us to universally identify any principle or authority in the system
CREATE TABLE acl_sid (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	sid VARCHAR(100) NOT NULL,
	principal BOOLEAN NOT NULL,
	CONSTRAINT unique_uk_1 UNIQUE(sid, principal)
);
COMMENT ON COLUMN acl_sid.principal IS 'if true, sid is instance of PrincipalSid which indicates username; otherwise, it is instance of GrantedAuthoritySid which indicates the role';
COMMENT ON COLUMN acl_sid.sid IS 'PrincipalSid (user) or GrantedAuthoritySid (role)';


-- ACL_CLASS table, store class name of the domain object
CREATE TABLE acl_class (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	class VARCHAR(100) NOT NULL,
	CONSTRAINT unique_uk_2 UNIQUE(class)
);
COMMENT ON COLUMN acl_class.class IS 'the class name of secured domain objects. for example: com.simplejourney.securityacl.entities.User';


-- ACL_OBJECT_IDENTITY table, which stores information for each unique domain object
CREATE TABLE acl_object_identity (
	id BIGSERIAL NOT NULL PRIMARY KEY, 
	object_id_class BIGINT NOT NULL,
	object_id_identity BIGINT NOT NULL,
	parent_object BIGINT,
	owner_sid BIGINT NOT NULL,
	entries_inheriting BOOLEAN NOT NULL,
	CONSTRAINT unique_uk_3 UNIQUE(object_id_class, object_id_identity),
	CONSTRAINT foreign_fk_1 FOREIGN KEY(parent_object) REFERENCES acl_object_identity(id),
	CONSTRAINT foreign_fk_2 FOREIGN KEY(object_id_class) REFERENCES acl_class(id),
	CONSTRAINT foreign_fk_3 FOREIGN KEY(owner_sid) REFERENCES acl_sid(id)
);
COMMENT ON COLUMN acl_object_identity.object_id_class IS 'define the domain object class, links to ACL_CLASS table';
COMMENT ON COLUMN acl_object_identity.object_id_identity IS 'domain objects can be stored in many tables depending on the class. Hence, this field store the target object primary key';
COMMENT ON COLUMN acl_object_identity.parent_object IS 'specify parent of this Object Identity within this table';
COMMENT ON COLUMN acl_object_identity.owner_sid IS 'ID of the object owner, links to ACL_SID table';
COMMENT ON COLUMN acl_object_identity.entries_inheriting IS 'whether ACL Entities of this object inherits from the parent object (ACL Entries are defined in ACL_ENTRY table';


-- the ACL_ENTRY store individual permission assigns to each SID on an Object Identity
CREATE TABLE acl_entry (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	acl_object_identity BIGINT NOT NULL,
	ace_order INTEGER NOT NULL,
	sid BIGINT NOT NULL,
	mask INTEGER NOT NULL,
	granting BOOLEAN NOT NULL,
	audit_success BOOLEAN NOT NULL,
	audit_failure BOOLEAN NOT NULL,
	CONSTRAINT unique_uk_4 UNIQUE(acl_object_identity, ace_order),
	CONSTRAINT foreign_fk_4 FOREIGN KEY(acl_object_identity) REFERENCES acl_object_identity(id),
	CONSTRAINT foreign_fk_5 FOREIGN KEY(sid) REFERENCES acl_sid(id)
);
COMMENT ON COLUMN acl_entry.acl_object_identity IS 'specify the object identity, links to ACL_OBJECT_IDENTITY table';
COMMENT ON COLUMN acl_entry.ace_order IS 'the order of current entry in the ACL entries list of corresponding Object Identity';
COMMENT ON COLUMN acl_entry.sid IS 'the target SID which the permission is granted to or denied from, links to ACL_SID table';
COMMENT ON COLUMN acl_entry.mask IS 'the integer bit mask that represents the actual permission being granted or denied';
COMMENT ON COLUMN acl_entry.audit_success IS 'for auditing purpose';
COMMENT ON COLUMN acl_entry.audit_failure IS 'for auditing purpose';

/*
 * User, Group, Role, Permission
 */

CREATE TABLE users (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	name VARCHAR(36) NOT NULL,
	password VARCHAR(128) NOT NULL,
	CONSTRAINT unique_uk_name UNIQUE(name)
);
INSERT INTO users (name, password) VALUES ('tom', '$2a$10$slYQmyNdGzTn7ZLBXBChFOCHQhUkTikWVg2V95lHK7HRj/LPjaZIa');
INSERT INTO users (name, password) VALUES ('jerry', '$2a$10$slYQmyNdGzTn7ZLBXBChFOCHQhUkTikWVg2V95lHK7HRj/LPjaZIa');
INSERT INTO users (name, password) VALUES ('nibbles', '$2a$10$slYQmyNdGzTn7ZLBXBChFOCHQhUkTikWVg2V95lHK7HRj/LPjaZIa');
INSERT INTO users (name, password) VALUES ('spike', '$2a$10$slYQmyNdGzTn7ZLBXBChFOCHQhUkTikWVg2V95lHK7HRj/LPjaZIa');

CREATE TABLE groups (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	name VARCHAR(36) NOT NULL
);
INSERT INTO groups (name) VALUES ('family');

CREATE TABLE user_group (
	id BIGSERIAL NOT NULL PRIMARY KEY,
	user_id BIGINT NOT NULL,
	group_id BIGINT NOT NULL,
	CONSTRAINT foreign_fk_user_id FOREIGN KEY(user_id) REFERENCES users(id),
	CONSTRAINT foreign_fk_group_id FOREIGN KEY(group_id) REFERENCES groups(id)
);
INSERT INTO user_group (user_id, group_id) VALUES (1, 1); -- add Tom to 'family' group
INSERT INTO user_group (user_id, group_id) VALUES (2, 1); -- add Jerry to 'family' group


/*
 * Business Data
 */

CREATE TABLE note (
	id BIGSERIAL PRIMARY KEY NOT NULL,
	title VARCHAR(64) NOT NULL,
	content TEXT NOT NULL,
	create_date BIGINT NOT NULL,
	author_id BIGINT NOT NULL,
	CONSTRAINT foreign_fk_author_id FOREIGN KEY(author_id) REFERENCES users(id)
);