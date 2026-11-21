package com.mall.controller;

import com.mall.common.PageResult;
import com.mall.common.Result;
import com.mall.dto.UserDto;
import com.mall.entity.User;
import com.mall.service.UserService;
import com.mall.util.UserHolder;
import com.mall.vo.LoginVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 操作员（后台用户）管理接口。
 * <p>
 * 提供分页查询、详情、增删改以及「当前登录用户」信息查询。
 * 所有接口均返回不含密码字段的用户数据。
 *
 * @author wwty
 */
@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    /** 分页默认页码 */
    private static final int DEFAULT_PAGE = 1;

    /** 分页默认每页条数 */
    private static final int DEFAULT_PAGE_SIZE = 10;

    private final UserService userService;

    /**
     * 分页查询操作员。
     *
     * @param page     页码，从 1 开始
     * @param pageSize 每页条数
     * @return 分页结果
     */
    @GetMapping
    public Result<PageResult<User>> page(@RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "10") int pageSize) {
        // 兜底非法分页参数，避免 page=0 或超大 pageSize 直接打到数据库
        int safePage = Math.max(page, DEFAULT_PAGE);
        int safeSize = pageSize <= 0 ? DEFAULT_PAGE_SIZE : Math.min(pageSize, 100);
        return Result.success(userService.page(safePage, safeSize));
    }

    /**
     * 查询所有操作员（不分页）。
     *
     * @return 操作员列表，已剔除密码字段
     */
    @GetMapping("/all")
    public List<User> getAllUsers() {
        List<User> users = userService.list();
        // 密码是敏感字段，绝不能随列表返回
        users.forEach(u -> u.setPassword(null));
        return users;
    }

    /**
     * 查询当前登录用户信息（不含令牌）。
     *
     * @return 当前用户信息；用户已被删除时返回 null
     */
    @GetMapping("/info")
    public LoginVo getCurrentUser() {
        Long userId = UserHolder.getUserId();
        if (userId == null) {
            // 理论上拦截器已保证登录，这里做防御性判断，避免测试或放行路径下出现空指针
            return null;
        }
        User user = userService.getById(userId);
        if (user == null) {
            return null;
        }
        LoginVo loginVo = new LoginVo();
        loginVo.setId(user.getId());
        loginVo.setName(user.getName());
        loginVo.setUsername(user.getUsername());
        loginVo.setAvatar(user.getAvatar());
        loginVo.setRoleLabel(user.getRoleLabel());
        return loginVo;
    }

    /**
     * 根据 id 查询操作员。
     *
     * @param id 用户 id
     * @return 用户信息（不含密码）
     */
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    /**
     * 新增操作员。
     *
     * @param userDto 用户信息
     * @return 成功响应
     */
    @PostMapping("")
    public Result<Void> addUser(@RequestBody UserDto userDto) {
        userService.addUser(userDto);
        log.info("新增操作员 username={}", userDto.getUsername());
        return Result.success();
    }

    /**
     * 修改操作员。
     *
     * @param userDto 用户信息，必须携带 id
     * @return 成功响应
     */
    @PutMapping("")
    public Result<Void> updateUser(@RequestBody UserDto userDto) {
        userService.updateUser(userDto);
        log.info("修改操作员 id={}", userDto.getId());
        return Result.success();
    }

    /**
     * 删除操作员。
     *
     * @param id 用户 id
     * @return 成功响应
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.removeById(id);
        log.info("删除操作员 id={}", id);
        return Result.success();
    }
}