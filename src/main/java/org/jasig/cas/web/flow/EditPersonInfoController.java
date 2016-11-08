package org.jasig.cas.web.flow;

import net.sf.json.JSONObject;
import org.jasig.cas.thrift.server.UserSupDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

/**
 * Created by SyJun on 2016/9/27.
 */
public class EditPersonInfoController extends AbstractController {

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
        String username = httpServletRequest.getParameter("username");
        String name = httpServletRequest.getParameter("name");
        String email = httpServletRequest.getParameter("email");
        String mobile = httpServletRequest.getParameter("mobile");
        username = username.isEmpty()?"": URLEncoder.encode(username,"utf-8");
        name = name.isEmpty()?"":new String(name.getBytes("iso8859-1"),"utf-8");
        email = email.isEmpty()?"":new String(email.getBytes("iso8859-1"),"utf-8");
        mobile = mobile.isEmpty()?"":URLEncoder.encode(mobile,"utf-8");
        JSONObject jo = new JSONObject();
        try {
            boolean isExistUser = userSupDao.existUser(username);
            if(isExistUser) {
                boolean isTrue = userSupDao.editUserInfo(username,name,email,mobile);
                if(isTrue)
                    jo.element("state",true);
                else{
                    jo.element("state",false);
                    jo.element("errMsg","未知异常");
                }
            }else{
                jo.element("state",false);
                jo.element("errMsg","账号不存在");
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
