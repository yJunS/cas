package org.jasig.cas.web.flow;

import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.AuthenticationException;
import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.ticket.registry.TicketRegistry;
import org.jasig.cas.web.support.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.webflow.core.collection.LocalAttributeMap;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import javax.validation.constraints.NotNull;

/**
 * Created by SyJun on 2016/7/20.
 */
public class AuthenticationViaRFormAction  {

    public static final String SUCCESS = "success";

    @NotNull
    private CentralAuthenticationService centralAuthenticationService;

    public void setCentralAuthenticationService(CentralAuthenticationService centralAuthenticationService) {
        this.centralAuthenticationService = centralAuthenticationService;
    }

    @NotNull
    private TicketRegistry ticketRegistry;

    public void setTicketRegistry(TicketRegistry ticketRegistry) {
        this.ticketRegistry = ticketRegistry;
    }

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public final Event submit(final RequestContext context,
                              final Credential credential,
                              final MessageContext messageContext) throws Exception {

// Validate login ticket
        final String authoritativeLoginTicket =
                WebUtils.getLoginTicketFromFlowScope(context);
        final String providedLoginTicket =
                WebUtils.getLoginTicketFromRequest(context);
        if (!authoritativeLoginTicket.equals(providedLoginTicket)) {
            logger.warn("Invalid login ticket {}", providedLoginTicket);
            messageContext.addMessage(new MessageBuilder().code
                    ("error.invalid.loginticket").build());
            context.getFlowScope().put("ret","‐1");
            context.getFlowScope().put("msg", "LT过期，请重新登录!");
        }
        try {
            final String tgtId =
                    this.centralAuthenticationService.createTicketGrantingTicket(credential);
            WebUtils.putTicketGrantingTicketInFlowScope(context, tgtId);
            final Service service = WebUtils.getService(context);
            final String serviceTicketId =
                    this.centralAuthenticationService.grantServiceTicket(tgtId,service);
            WebUtils.putServiceTicketInRequestScope(context,serviceTicketId);
            context.getFlowScope().put("ticket", serviceTicketId);
            return newEvent(SUCCESS);
        } catch (final AuthenticationException e) {
            context.getFlowScope().put("ret","‐2");
            context.getFlowScope().put("msg",
                    "用户名密码错误，请重新登录!");
            return newEvent(SUCCESS);
        } catch (final Exception e) {
            context.getFlowScope().put("ret","‐3");
            context.getFlowScope().put("msg", "系统内部错误，请稍后登录!");
            return newEvent(SUCCESS);
        }
    }

    private Event newEvent(final String id) {
        return new Event(this, id);
    }

    private Event newEvent(final String id, final Exception error) {
        return new Event(this, id, new LocalAttributeMap("error", error));
    }
}
