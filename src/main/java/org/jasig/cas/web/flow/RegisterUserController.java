package org.jasig.cas.web.flow;

import net.sf.json.JSONObject;
import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.thrift.server.UserSupDao;
import org.jasig.cas.util.MD5;
import org.jasig.cas.web.support.CookieRetrievingCookieGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by SyJun on 2016/8/12.
 */
public class RegisterUserController extends AbstractController {

    private CentralAuthenticationService centralAuthenticationService;
    private CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;

    @Autowired
    private UserSupDao userSupDao;

    protected ModelAndView handleRequestInternal(HttpServletRequest request,
                                                 HttpServletResponse response) throws Exception
    {
        ModelAndView signinView=new ModelAndView();
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        String email = request.getParameter("email");
        String md5 = request.getParameter("md5");
        String mobile = request.getParameter("mobile");
        username = username!=null?username:"";
        password = password!=null?password:"";
        email = email!=null?email:"";
        mobile = mobile!=null?mobile:"";
        md5 = md5!=null?md5:"";
        boolean isExistUser = userSupDao.existUser(username);
        JSONObject jo = new JSONObject();
        boolean isTrue = false;
        if(!md5.isEmpty() && !username.isEmpty() && !password.isEmpty() && !email.isEmpty()) {
            String newmd5 = "";
            try {
                newmd5 = MD5.getMD5(("_"+username+password+email).getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(md5.toLowerCase().equals(newmd5.toLowerCase())) {
                if (!isExistUser) {
                    isTrue = userSupDao.addNewPerson(username, password, email, mobile);
                }
                if (!isExistUser && isTrue) {
                    jo.element("state", true);
                } else {
                    jo.element("state", false);
                    jo.element("errMsg", "此用户名已存在");
                }
            }else{
                jo.element("state",false);
                jo.element("errMsg","验证密钥不正确");
            }
        }else{
            jo.element("state",false);
            if(username.isEmpty()){
                jo.element("errMsg", "用户名不能为空");
            }else if (password.isEmpty()){
                jo.element("errMsg", "密码不能为空");
            }else if(email.isEmpty()){
                jo.element("errMsg", "邮箱不能为空");
            }else {
                jo.element("errMsg", "验证密钥不能为空");
            }
        }
        response.setContentType("text/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.write(jo.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
        /*String viewName=getSignInView(request);
        signinView.setViewName(viewName);*/
        return signinView;
    }

    /**
     * Get the signIn view URL.获取service参数并跳转页面
     * @param request the HttpServletRequest object.
     * @return redirect URL
     */
    protected String getSignInView(HttpServletRequest request) {
        String service = ServletRequestUtils.getStringParameter(request, "service", "");
        return ("redirect:login" + (service.length() > 0 ? "?service=" + service : ""));
    }

    public CentralAuthenticationService getCentralAuthenticationService() {
        return centralAuthenticationService;
    }

    public void setCentralAuthenticationService(CentralAuthenticationService centralAuthenticationService) {
        this.centralAuthenticationService = centralAuthenticationService;
    }

    public CookieRetrievingCookieGenerator getTicketGrantingTicketCookieGenerator() {
        return ticketGrantingTicketCookieGenerator;
    }

    public void setTicketGrantingTicketCookieGenerator(CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator) {
        this.ticketGrantingTicketCookieGenerator = ticketGrantingTicketCookieGenerator;
    }

    public UserSupDao getUserSupDao() {
        return userSupDao;
    }

    public void setUserSupDao(UserSupDao userSupDao) {
        this.userSupDao = userSupDao;
    }
}
