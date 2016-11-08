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
public class RemoveUserController extends AbstractController {
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
        try {
            String username = httpServletRequest.getParameter("username");
            username = username.isEmpty()?"": URLEncoder.encode(username,"utf8");
            boolean isExistUser = userSupDao.existUser(username);
            if(isExistUser) {
                userSupDao.removeUser(username);
                jo.element("state",true);
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
