package com.simplejourney.securityacl.config;

import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.sql.DataSource;


/**
 * JdbcMutableAclService and BasicLookupStrategy has issues when query contains the 'acl_object_identity.object_id_identity'
 * in DB table, it is 'BIGINT' (see [Spring Security: Security Database Schema](https://docs.spring.io/spring-security/site/docs/3.0.x/reference/appendix-schema.html)), but the sql statements of these 2 classes use 'varchar' for query
 * so, we MUST override it.
 * Otherwise, exception will be thrown.
 * Refer to [Spring Security ACL: No operator matches the given name and argument type #5508](https://github.com/spring-projects/spring-security/issues/5508)
 *
 * For example: following exception is thrown at Line 341 of JdbcMutableAclService
 * ----------------------------------------------------------
 * org.postgresql.util.PSQLException: ERROR: operator does not exist: bigint = character varying
 *   Hint: No operator matches the given name and argument types. You might need to add explicit type casts.
 *   Position: 190
 * 	at org.postgresql.core.v3.QueryExecutorImpl.receiveErrorResponse(QueryExecutorImpl.java:2533) ~[postgresql-42.2.12.jar:42.2.12]
 * 	at org.postgresql.core.v3.QueryExecutorImpl.processResults(QueryExecutorImpl.java:2268) ~[postgresql-42.2.12.jar:42.2.12]
 * 	at org.postgresql.core.v3.QueryExecutorImpl.execute(QueryExecutorImpl.java:313) ~[postgresql-42.2.12.jar:42.2.12]
 * 	at org.postgresql.jdbc.PgStatement.executeInternal(PgStatement.java:448) ~[postgresql-42.2.12.jar:42.2.12]
 * 	at org.postgresql.jdbc.PgStatement.execute(PgStatement.java:369) ~[postgresql-42.2.12.jar:42.2.12]
 * 	at org.postgresql.jdbc.PgPreparedStatement.executeWithFlags(PgPreparedStatement.java:159) ~[postgresql-42.2.12.jar:42.2.12]
 * 	at org.postgresql.jdbc.PgPreparedStatement.executeQuery(PgPreparedStatement.java:109) ~[postgresql-42.2.12.jar:42.2.12]
 * 	at com.zaxxer.hikari.pool.ProxyPreparedStatement.executeQuery(ProxyPreparedStatement.java:52) ~[HikariCP-3.4.3.jar:na]
 * 	at com.zaxxer.hikari.pool.HikariProxyPreparedStatement.executeQuery(HikariProxyPreparedStatement.java) ~[HikariCP-3.4.3.jar:na]
 * 	at org.springframework.jdbc.core.JdbcTemplate$1.doInPreparedStatement(JdbcTemplate.java:678) ~[spring-jdbc-5.2.6.RELEASE.jar:5.2.6.RELEASE]
 * 	at org.springframework.jdbc.core.JdbcTemplate.execute(JdbcTemplate.java:617) ~[spring-jdbc-5.2.6.RELEASE.jar:5.2.6.RELEASE]
 * 	at org.springframework.jdbc.core.JdbcTemplate.query(JdbcTemplate.java:669) ~[spring-jdbc-5.2.6.RELEASE.jar:5.2.6.RELEASE]
 * 	at org.springframework.jdbc.core.JdbcTemplate.query(JdbcTemplate.java:700) ~[spring-jdbc-5.2.6.RELEASE.jar:5.2.6.RELEASE]
 * 	at org.springframework.jdbc.core.JdbcTemplate.query(JdbcTemplate.java:712) ~[spring-jdbc-5.2.6.RELEASE.jar:5.2.6.RELEASE]
 * 	at org.springframework.jdbc.core.JdbcTemplate.queryForObject(JdbcTemplate.java:783) ~[spring-jdbc-5.2.6.RELEASE.jar:5.2.6.RELEASE]
 * 	at org.springframework.jdbc.core.JdbcTemplate.queryForObject(JdbcTemplate.java:809) ~[spring-jdbc-5.2.6.RELEASE.jar:5.2.6.RELEASE]
 * 	at org.springframework.security.acls.jdbc.JdbcMutableAclService.retrieveObjectIdentityPrimaryKey(JdbcMutableAclService.java:341) ~[spring-security-acl-5.2.4.RELEASE.jar:5.2.4.RELEASE]
 * 	at org.springframework.security.acls.jdbc.JdbcMutableAclService.createAcl(JdbcMutableAclService.java:107) ~[spring-security-acl-5.2.4.RELEASE.jar:5.2.4.RELEASE]
 * 	at com.simplejourney.securityacl.controllers.NoteController.save(NoteController.java:66) ~[classes/:na]
 * 	at com.simplejourney.securityacl.controllers.NoteController$$FastClassBySpringCGLIB$$d4cbf884.invoke(<generated>) ~[classes/:na]
 * 	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218) ~[spring-core-5.2.6.RELEASE.jar:5.2.6.RELEASE]
 * 	...
 */


@Configuration
public class AclConfiguration {
    @Autowired
    private AclService aclService;

    @Bean
    public JdbcMutableAclService aclService() {
        JdbcMutableAclService aclService = new JdbcMutableAclService(
                dataSource(),
                lookupStrategy(),
                aclCache()
        );

        aclService.setClassIdentityQuery("select currval(pg_get_serial_sequence('acl_class', 'id'))");
        aclService.setSidIdentityQuery("select currval(pg_get_serial_sequence('acl_sid', 'id'))");
        aclService.setObjectIdentityPrimaryKeyQuery("select acl_object_identity.id from acl_object_identity, acl_class where acl_object_identity.object_id_class = acl_class.id and acl_class.class=? and acl_object_identity.object_id_identity = cast(? as bigint)");
        aclService.setFindChildrenQuery("select obj.object_id_identity as obj_id, class.class as class from acl_object_identity obj, acl_object_identity parent, acl_class class where obj.parent_object = parent.id and obj.object_id_class = class.id and parent.object_id_identity = cast(? as bigint) and parent.object_id_class = (select id FROM acl_class where acl_class.class = ?)");
        aclService.setInsertObjectIdentitySql("insert into acl_object_identity (object_id_class, object_id_identity, owner_sid, entries_inheriting) values (?, cast(? as bigint), ?, ?)");

        return aclService;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public AclCache aclCache() {
        EhCacheFactoryBean factoryBean = new EhCacheFactoryBean();
        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();

        cacheManagerFactoryBean.setAcceptExisting(true);
        cacheManagerFactoryBean.setCacheManagerName(CacheManager.getInstance().getName());
        cacheManagerFactoryBean.afterPropertiesSet();

        factoryBean.setName("aclCache");
        factoryBean.setCacheManager(cacheManagerFactoryBean.getObject());
        factoryBean.setMaxBytesLocalHeap("10M");
        factoryBean.setMaxEntriesLocalHeap(0L);
        factoryBean.afterPropertiesSet();

        return new EhCacheBasedAclCache(factoryBean.getObject(), grantingStrategy(), aclAuthorizationStrategy());
    }

    @Bean
    public AuditLogger auditLogger() {
        return new ConsoleAuditLogger();
    }

    @Bean
    public LookupStrategy lookupStrategy() {
        BasicLookupStrategy lookupStrategy = new BasicLookupStrategy(
                dataSource(),
                aclCache(),
                aclAuthorizationStrategy(),
                grantingStrategy()
        );

        lookupStrategy.setLookupObjectIdentitiesWhereClause("(acl_object_identity.object_id_identity = cast(? as bigint) and acl_class.class = ?)");
        return lookupStrategy;
    }

    @Bean
    public PermissionGrantingStrategy grantingStrategy() {
        /**
         * Use build-in DefaultPermissionGrantingStrategy
         */
//        return new DefaultPermissionGrantingStrategy(auditLogger());

        /**
         * Use customized PermissionGrantingStrategy
         */
        return new DemoPermissionGrantingStrategy();
    }

    @Bean
    public AclAuthorizationStrategy aclAuthorizationStrategy() {
        return new AclAuthorizationStrategyImpl(
                new SimpleGrantedAuthority("ROLE_ADMIN"),       // authority which is used to change owner
                new SimpleGrantedAuthority("EDITOR"),      // authority which is used to change authorization
                new SimpleGrantedAuthority("EDITOR")  // authority which is used to chage other information. Changes ACL will use this.
        );
    }

    /**
     * If we want to customize how to check security expression, we can implement our own MethodSecurityExpressionHandler,
     * and create it here
     */

    @Bean
    public MethodSecurityExpressionHandler expressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(aclPermissionEvaluator);
        return expressionHandler;
    }

    @Autowired
    private DemoAclPermissionEvaluator aclPermissionEvaluator;

    @Bean
    public DemoAclPermissionEvaluator aclPermissionEvaluator() {
        /**
         * Use build-in AclPermissionEvaluator and BasePermission
         */
//        AclPermissionEvaluator aclPermissionEvaluator = new AclPermissionEvaluator(aclService);
//        aclPermissionEvaluator.setPermissionFactory(new DefaultPermissionFactory(BasePermission.class));
//        return aclPermissionEvaluator;

        /**
         * Use customized AclPermissionEvaluator and build-in BasePermission
         */
        DemoAclPermissionEvaluator aclPermissionEvaluator = new DemoAclPermissionEvaluator(aclService);
        aclPermissionEvaluator.setPermissionFactory(new DefaultPermissionFactory(BasePermission.class));
        return aclPermissionEvaluator;

        /**
         * Use customized AclPermissionEvaluator and Permission
         */
//        DemoAclPermissionEvaluator aclPermissionEvaluator = new DemoAclPermissionEvaluator(aclService);
//        aclPermissionEvaluator.setPermissionFactory(new DefaultPermissionFactory(DemoBasePermission.class));
//        return aclPermissionEvaluator;
    }
}