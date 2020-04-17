package com.xp.usermanager.service;

import com.xp.common.AndroidTool;
import com.xp.common.Tool;
import com.xp.usermanager.fragments.Login;
import com.xp.usermanager.po.User;

public class UserService {
    private  static User user;

    /**
     * 返回 用户 访问web时的跟路径
     *
     * @return
     */
    public static String getURLBasic() {
        return Tool.getHostAddress() + "user/";
    }

    public static void setUser(User user){
        UserService.user =user;
    }
    /**
     * 得到登录的 user ，如果没有登录的话，会跳转到登录
     */
    public static User getUser(){
        {
            //测试数据
            user = new User("123","123");
            user.setId(1L);
        }

        if(user == null){
            AndroidTool.replaceFrameLayout( new Login());
            return  null;
        }
        return user;
    }

    /**
     * 返回 user的id ，如果user 为null 返回 -1
     * @return
     */
    public static Long getUserId() {
        User user =getUser();
        if(user == null){
            return -1L;
        }
        return user.getId();
    }
}
