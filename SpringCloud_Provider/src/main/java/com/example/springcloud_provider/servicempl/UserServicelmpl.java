package com.example.springcloud_provider.servicempl;
import com.example.springcloud_provider.bean.UserBean;
import com.example.springcloud_provider.service.UserService;
import org.springframework.stereotype.Service;
import com.example.springcloud_provider.mapper.Usermapper;
import javax.annotation.Resource;
@Service
public class UserServicelmpl implements UserService {
    @Resource
    private Usermapper userMapper;
    public UserBean logIn(String name, String password)
    {
        return userMapper.getInfo(name,password);
    }
    public UserBean logInByEmailAndPhone(String email, String phone) {
        return userMapper.getInfoByEmailAndPhone(email, phone);
    }

}
