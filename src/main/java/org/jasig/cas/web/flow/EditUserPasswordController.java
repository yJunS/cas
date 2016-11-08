package org.jasig.cas.web.flow;

import com.sun.deploy.net.URLEncoder;
import net.sf.json.JSONObject;
import org.jasig.cas.thrift.server.UserSupDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by SyJun on 2016/9/27.
 */
public class EditUserPasswordController extends AbstractController{

    @Autowired
    private UserSupDao userSupDao;

    public UserSupDao getUserSupDao() {
        return userSupDao;
    }

    public void setUserSupDao(UserSupDao userSupDao) {
        this.userSupDao = userSupDao;
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        JSONObject jo = new JSONObject();
        String username = httpServletRequest.getParameter("username");
        String password = httpServletRequest.getParameter("password");
        String newPassword = httpServletRequest.getParameter("newPassword");
        try {
            username = username.isEmpty()?"":URLEncoder.encode(username,"utf8");
            password = password.isEmpty()?"":URLEncoder.encode(password,"utf8");
            newPassword = newPassword.isEmpty()?"":URLEncoder.encode(newPassword,"utf8");
            boolean isExistUser = userSupDao.existUser(username);
            if(isExistUser && !newPassword.equals("")) {
                boolean isTrue = userSupDao.editPassword(username,password,newPassword);
                if(isTrue)
                    jo.element("state", true);
                else{
                    jo.element("state",false);
                    jo.element("errMsg","旧密码与新密码相同");
                }
            }else if(!isExistUser){
                jo.element("state",false);
                jo.element("errMsg","账号不存在");
            }else{
                jo.element("state",false);
                jo.element("errMsg","新密码不能为空");
            }
        } catch (Exception e) {
            jo.element("state",false);
            jo.element("errMsg",e.getMessage());
            e.printStackTrace();
        }
        httpServletResponse.setContentType("text/json");
        httpServletResponse.setCharacterEncoding("UTF-8");
        PrintWriter out = null;
        try {
            out = httpServletResponse.getWriter();
            out.write(jo.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
        return modelAndView;
    }
}
