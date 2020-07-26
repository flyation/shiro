package com.southwind.realm;

import com.southwind.entity.Account;
import com.southwind.service.AccountService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;

public class AccountRealm extends AuthorizingRealm {
    @Autowired
    private AccountService accountService;

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Subject subject = SecurityUtils.getSubject();
        Account account = (Account) subject.getPrincipal();
        // 设置角色
        HashSet<String> roles = new HashSet<>();
        roles.add(account.getRole());
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo(roles);
        // 设置权限
        simpleAuthorizationInfo.addStringPermission(account.getPerms());
        return simpleAuthorizationInfo;
    }

    /**
     * 认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        // 在数据库中通过用户名验证
        Account account = accountService.findByUsername(usernamePasswordToken.getUsername());
        if (account != null) {
            // shiro自己完成密码验证，若密码错误则抛出异常IncorrectCredentialsException
            // TODO: 2020/7/26 这个构造方法是如何完成密码验证的？
            return new SimpleAuthenticationInfo(account, account.getPassword(), getName()); //通过方法参数传入的真实密码和token中封装的密码自动进行验证
        }
        // 抛出异常UnknownAccountException
        return null;
    }
}
