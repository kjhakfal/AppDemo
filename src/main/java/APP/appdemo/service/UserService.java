package APP.appdemo.service;

import APP.appdemo.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;


public interface UserService extends IService<User> {

    boolean saveUser(User user);
}
