package org.jasig.cas.web.flow;

import net.sf.json.JSONObject;
import org.jasig.cas.thrift.server.UserSupDao;
import org.jasig.cas.util.MD5;
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
            String md5 = httpServletRequest.getParameter("md5");
            username = username!=null?username:"";
            md5 = md5!=null?md5:"";
            boolean isExistUser = userSupDao.existUser(username);
            if(!md5.equals("")&&!username.equals("")) {
                String newmd5 = "";
                try {
                    newmd5 = MD5.getMD5(("_"+username).getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(newmd5.toLowerCase().equals(md5.toLowerCase())) {
                    if (isExistUser) {
                        userSupDao.removeUser(username);
                        jo.element("state", true);
                    } else {
                        jo.element("state", false);
                        jo.element("errMsg", "账号不存在");
                    }
                }else{
                    jo.element("state",false);
                    jo.element("errMsg","验证密钥不正确");
                }
            }else{
                jo.element("state",false);
                jo.element("errMsg",username.equals("")?"用户名不能为空":"验证密钥不能为空");
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
