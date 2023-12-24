//UserService类
package com.example.springcloud_provider.service;
import com.example.springcloud_provider.bean.UserBean;

public interface UserService {
    UserBean logIn(String name,String password);
    UserBean logInByEmailAndPhone(String email, String phone);

}