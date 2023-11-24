package APP.appdemo.service.Impl;

import APP.appdemo.entity.User;
import APP.appdemo.mapper.UserMapper;
import APP.appdemo.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public boolean saveUser(User user) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, user.getEmail());
        int count =this.count(queryWrapper);
        if (count == 1) {
            return false;
        } else {
            //md5加密
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
            this.save(user);
        }
        return true;
    }
}
