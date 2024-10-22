package vip.yeee.memoo.demo.springcloud.webauth.server.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memoo.base.websecurityoauth2.constant.SecurityUserTypeEnum;
import vip.yeee.memoo.demo.springcloud.webauth.server.model.bo.FrontUserBo;
import vip.yeee.memoo.demo.springcloud.webauth.server.model.bo.SystemUserBo;
import vip.yeee.memoo.base.model.exception.BizException;
import vip.yeee.memoo.base.websecurityoauth2.constant.MessageConstant;
import vip.yeee.memoo.common.platformauth.server.service.AbstractCustomUserDetailsService;
import vip.yeee.memoo.common.domain.entity.front.User;
import vip.yeee.memoo.common.domain.entity.sys.SysRole;
import vip.yeee.memoo.common.domain.entity.sys.SysUser;
import vip.yeee.memoo.common.domain.entity.sys.SysUserRole;
import vip.yeee.memoo.common.domain.mapper.front.UserMapper;
import vip.yeee.memoo.common.domain.mapper.sys.SysRoleMapper;
import vip.yeee.memoo.common.domain.mapper.sys.SysUserMapper;
import vip.yeee.memoo.common.domain.mapper.sys.SysUserRoleMapper;
import vip.yeee.memoo.base.websecurityoauth2.model.AuthUser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/11/16 17:34
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class CustomUserDetailsService extends AbstractCustomUserDetailsService {

    private final SysUserMapper sysUserMapper;
    private final UserMapper userMapper;
    private final SysRoleMapper sysRoleMapper;
    private final SysUserRoleMapper sysUserRoleMapper;

    @Override
    public AuthUser getUserByUserTypeAndUsername(String userType, String username) {
        // cache
        if (SecurityUserTypeEnum.SYSTEM_USER.getType().equals(userType)) {
            return this.getSystemUserByUsername(username);
        } else if (SecurityUserTypeEnum.FRONT_USER.getType().equals(userType)) {
            return this.getFrontUserByUsername(username);
        }
        return null;
    }

    public AuthUser getSystemUserByUsername(String username) {
        LambdaQueryWrapper<SysUser> userQuery = Wrappers.lambdaQuery();
        userQuery.eq(SysUser::getUsername, username);
        SysUser sysUser = sysUserMapper.selectOne(userQuery);
        if (sysUser == null) {
            throw new BizException(MessageConstant.USER_NOT_EXIST);
        }
        // find roles
        LambdaQueryWrapper<SysUserRole> userRoleQuery = Wrappers.lambdaQuery();
        userRoleQuery.eq(SysUserRole::getUserId, sysUser.getId());
        List<SysUserRole> userRoles = sysUserRoleMapper.selectList(userRoleQuery);
        if (CollectionUtil.isEmpty(userRoles)) {
            log.warn(MessageConstant.USER_NO_ROLES);
//            throw new BizException(MessageConstant.USER_NO_ROLES);
        }
        Set<String> roles = Sets.newHashSet();
        userRoles.forEach(ur -> {
            LambdaQueryWrapper<SysRole> roleQuery = Wrappers.lambdaQuery();
            roleQuery.eq(SysRole::getId, ur.getRoleId());
            SysRole role = sysRoleMapper.selectOne(roleQuery);
            if (role != null) {
                roles.add(role.getCode());
            }
        });
        SystemUserBo userBo = new SystemUserBo();
        userBo.setUserId(sysUser.getId().toString());
        userBo.setUsername(sysUser.getUsername());
        userBo.setPassword(sysUser.getPassword());
        userBo.setState(sysUser.getState());
        userBo.setRoles(roles);
        HashSet<String> permissions = Sets.newHashSet();
        permissions.add("yeee:test:aaa");
        userBo.setPermissions(permissions);
        return userBo;
    }

    public AuthUser getFrontUserByUsername(String username) {
        LambdaQueryWrapper<User> userQuery = Wrappers.lambdaQuery();
        userQuery.eq(User::getUsername, username);
        User user = userMapper.selectOne(userQuery);
        if (user == null) {
            throw new BizException(MessageConstant.USER_NOT_EXIST);
        }
        FrontUserBo userBo = new FrontUserBo();
        userBo.setUserId(user.getId().toString());
        userBo.setUsername(user.getUsername());
        userBo.setPassword(user.getPassword());
        userBo.setState(0);
        return userBo;
    }
}
