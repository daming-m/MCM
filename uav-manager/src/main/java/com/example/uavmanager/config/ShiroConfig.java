package com.example.uavmanager.config;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.Filter;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroConfig {

    @Bean
    public Realm realm() {
        return new IniRealm("classpath:shiro.ini");
    }

    @Bean
    public SecurityManager securityManager(Realm realm) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(realm);
        return manager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager);
        bean.setLoginUrl("/login");

        Map<String, Filter> filters = new LinkedHashMap<>();
        bean.setFilters(filters);

        Map<String, String> chain = new LinkedHashMap<>();
        chain.put("/css/**", "anon");
        chain.put("/images/**", "anon");
        chain.put("/error", "anon");
        chain.put("/login", "anon");
        chain.put("/logout", "logout");
        chain.put("/**", "authc");
        bean.setFilterChainDefinitionMap(chain);

        return bean;
    }
}

