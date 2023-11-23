package APP.appdemo.entity;

import lombok.Data;

@Data
public class User {
    private static final long serialVersionUID = 1L;

    private Long id;

    //网名
    private String username;

    //密码
    private String password;

    //性别 0 女 1 男
    private String sex;

    //头像
    private String avatar;


    //个性签名
    private String motto;

}
