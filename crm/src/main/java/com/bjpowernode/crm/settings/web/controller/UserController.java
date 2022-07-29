package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.commons.bean.ResultObject;
import com.bjpowernode.crm.commons.constans.Constants;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(){
        return "settings/qx/user/login";
    }

    @ResponseBody
    @RequestMapping(value = "/settings/qx/user/login.do" ,method = RequestMethod.POST)
    public Object toIndex(String loginAct, String loginPwd, String idRemAct, HttpServletRequest request, HttpServletResponse response,HttpSession session){

        Map<String, Object> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        //查询用户信息
        User user = null;
       try {
           user = userService.queryUserByActAndPwd(map);
       }catch (Exception e) {
           e.printStackTrace();
       }
        String nowDate = DateUtils.getStringAsDateTime(new Date());

        if(user == null){
            //用户不存在或者账户密码错误
            System.out.println("用户不存在或者账户密码错误");
            return new ResultObject(Constants.RETURN_OBJECT_CODE_FAIL,"用户不存在或者账户密码错误");
        } else if (nowDate.compareTo(user.getExpireTime()) > 0){
            //账户已过期，请重新登录
            System.out.println("账户已过期，请重新注册");
            return new ResultObject(Constants.RETURN_OBJECT_CODE_FAIL,"账户已过期，请重新注册");
        }else if(!user.getAllowIps().contains(request.getRemoteAddr())){
            //账户ip受限
            System.out.println("账户ip受限");
            return new ResultObject(Constants.RETURN_OBJECT_CODE_FAIL,"账户ip受限");
        }else {
            session.setAttribute(Constants.SESSION_USER_KEY,user);
            if("true".equals(idRemAct)){
                System.out.println("fanhui3");
                Cookie c1 = new Cookie("loginAct",user.getLoginAct());
                c1.setMaxAge(10*24*60*60);
                response.addCookie(c1);
                Cookie c2 = new Cookie("loginPwd",user.getLoginPwd());
                c1.setMaxAge(10*24*60*60);
                response.addCookie(c2);
            }else {
                Cookie c1 = new Cookie("loginAct","1");
                c1.setMaxAge(0);
                response.addCookie(c1);
                Cookie c2 = new Cookie("loginPwd","1");
                c2.setMaxAge(0);
                System.out.println("fanhui2");
                response.addCookie(c2);
            }
            System.out.println("fanhui1");
            return new ResultObject(Constants.RETURN_OBJECT_CODE_SUCCESS,user);
        }
    }

    @RequestMapping("/settings/qx/user/toLogout.do")
    public String toLogout(HttpSession session,HttpServletResponse response){
        //删除cookie
        Cookie c1 = new Cookie("loginAct","1");
        c1.setMaxAge(0);
        response.addCookie(c1);
        Cookie c2 = new Cookie("loginPwd","1");
        c2.setMaxAge(0);
        response.addCookie(c2);
        //删除session
        session.invalidate();
        //重定向页面
        return "redirect:/";
    }
}
