package APP.appdemo.controller;


import APP.appdemo.common.R;
import APP.appdemo.entity.User;
import APP.appdemo.service.Impl.UserServiceImpl;
import APP.appdemo.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /*
     * 用户注册功能
     * */
    @PostMapping("/save")
    public R<String> save(@RequestBody User user) {
        return new UserServiceImpl().saveUser(user) ? R.success("注册成功") : R.error("注册失败");
    }

    /*
     * 用户登录功能
     * */
    @PostMapping("/login")
    public R<User> login(HttpServletRequest request, @RequestBody User user) {
        //1.md5加密
        String password = user.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2.根据页面提交的user查询数据库
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, user.getEmail());
        User user1 = userService.getOne(queryWrapper);
        if (user1 == null) {
            return R.error("登录失败，用户未注册");
        }
        if (!password.equals(user1.getPassword())) {
            return R.error("密码错误，请重新输入");
        }
        request.getSession().setAttribute("user", user1.getId());
        return R.success(user1);
    }


    /*
     * 修改用户密码
     * */
    @PutMapping
    public R<String> update(HttpServletRequest request, String oldPassword, String newPassword) {
        //根据登录成功时存入的id查询数据库获取密码与旧密码就行比对
        String id = (String) request.getSession().getAttribute("user");

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId, id);

        User user = userService.getOne(queryWrapper);
        oldPassword = DigestUtils.md5DigestAsHex(oldPassword.getBytes());

        if (oldPassword.equals(user.getPassword())) {
            user.setPassword(DigestUtils.md5DigestAsHex(newPassword.getBytes()));
            userService.update(user, queryWrapper);
            return R.success("密码修改成功");
        } else {
            return R.error("旧密码错误，请重新输入");
        }
    }


    /*
     * 用户退出
     * */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.removeAttribute("user");
        return R.success("退出成功");
    }
}
