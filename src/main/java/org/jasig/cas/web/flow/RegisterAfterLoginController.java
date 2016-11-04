package org.jasig.cas.web.flow;

import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.jasig.cas.ticket.TicketException;
import org.jasig.cas.web.support.CookieRetrievingCookieGenerator;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;

/**
 *
 *
 * 功能：注册后自动登录处理类
 *
 * @ClassName: RegisterAfterLoginController
 * @version V1.0
 * @date 2016年7月5日
 * @author [url=mailto:6637152@qq.com]zqb[/url]
 */
public class RegisterAfterLoginController extends AbstractController {

    private CentralAuthenticationService centralAuthenticationService;
    private CookieRetrievingCookieGenerator  ticketGrantingTicketCookieGenerator;

    /**
     *
     *
     * 功能：获取用户名密码,验证有效性,生成相关票据并绑定注册,添加cookie
     *
     * @author [url=mailto:engineer03@financegt.com]zqb[/url]
     * @date 2016年7月5日
     * @param request
     * @param response
     * @return
     * @throws Exception
     * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ModelAndView handleRequestInternal(HttpServletRequest request,
                                                 HttpServletResponse response) throws Exception
    {
        ModelAndView signinView=new ModelAndView();
        String username=request.getParameter("username");
        String password=request.getParameter("password");

        /*try {
            username = new String(new BASE64Decoder().decodeBuffer(username));  //解密后
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            password = new String(new BASE64Decoder().decodeBuffer(password));
        } catch (IOException e) {
            e.printStackTrace();
        }*/

//        System.out.println("解密后的账号:"+username);
//        System.out.println("解密后的密码:"+password);
//        username = EncryptUrlPara.decrypt("username",username);
//        password = EncryptUrlPara.decrypt("password",password);

        bindTicketGrantingTicket(username, password, request, response);
        String viewName=getSignInView(request);
        signinView.setViewName(viewName);
        return signinView;
    }


    /**
     * Invoke generate validate Tickets and add the TGT to cookie.
     * @param loginName     the user login name.
     * @param loginPassword the user login password.
     * @param request       the HttpServletRequest object.
     * @param response      the HttpServletResponse object.
     */
    /**
     *
     *
     * 功能：具体生成相关票据并绑定注册,添加cookie实现方法
     *
     * @author [url=mailto:engineer03@financegt.com]zqb[/url]
     * @date 2016年7月5日
     * @param loginName
     * @param loginPassword
     * @param request
     * @param response
     */
    protected void bindTicketGrantingTicket(String loginName, String loginPassword, HttpServletRequest request, HttpServletResponse response){
        try {
            //UsernamePasswordCredentials credentials = new UsernamePasswordCredentials();  //4.0之前
            UsernamePasswordCredential credentials = new UsernamePasswordCredential();
            credentials.setUsername(loginName);
            credentials.setPassword(loginPassword);
            String ticketGrantingTicket = centralAuthenticationService.createTicketGrantingTicket(credentials);
            ticketGrantingTicketCookieGenerator.addCookie(request, response, ticketGrantingTicket);
        } catch (TicketException te) {
            logger.error("Validate the login name " + loginName + " failure, can't bind the TGT!", te);
        } catch (Exception e){
            logger.error("bindTicketGrantingTicket has exception.", e);
        }
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


    public CentralAuthenticationService getCentralAuthenticationService()
    {
        return centralAuthenticationService;
    }


    public void setCentralAuthenticationService(
            CentralAuthenticationService centralAuthenticationService)
    {
        this.centralAuthenticationService = centralAuthenticationService;
    }


    public CookieRetrievingCookieGenerator getTicketGrantingTicketCookieGenerator()
    {
        return ticketGrantingTicketCookieGenerator;
    }


    public void setTicketGrantingTicketCookieGenerator(
            CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator)
    {
        this.ticketGrantingTicketCookieGenerator = ticketGrantingTicketCookieGenerator;
    }
}
