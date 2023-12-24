//Usermapper类
package com.example.springcloud_provider.mapper;
import com.example.springcloud_provider.bean.UserBean;
import org.apache.ibatis.annotations.Param;
public interface Usermapper {
    //UserBean getInfo(String name,String password);
    UserBean getInfo(@Param("name") String name, @Param("password") String password);
    UserBean getInfoByEmailAndPhone(@Param("email") String email, @Param("phone") String phone);

}