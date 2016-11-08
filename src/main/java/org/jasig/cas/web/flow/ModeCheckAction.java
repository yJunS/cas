package org.jasig.cas.web.flow;

import org.jasig.cas.web.support.WebUtils;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by SyJun on 2016/7/20.
 */
public class ModeCheckAction {
    public static final String NORMAL = "normal";
    public static final String RLOGIN = "rlogin";
    public void RLoginCheckAction() { }
    public Event check(final RequestContext context) {
        final HttpServletRequest request = WebUtils.getHttpServletRequest(context);
        //根据mode判断请求模式，如mode=rlogin，是AJAX登录模式，
        // 不存在是原模式，认证中心本地登录
        String mode = request.getParameter("mode");
        if(mode!=null&&mode.equals("rlogin")){
            context.getFlowScope().put("mode", mode);
            return new Event(this, RLOGIN);
        }
        return new Event(this, NORMAL);
    }

}
