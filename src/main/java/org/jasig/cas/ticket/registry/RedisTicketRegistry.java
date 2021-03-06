/*
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.cas.ticket.registry;

import org.jasig.cas.ticket.ServiceTicket;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.springframework.beans.factory.DisposableBean;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Key-value ticket registry implementation that stores tickets in redis keyed on the ticket ID.
 *
 * @author Scott Battaglia
 * @author Marvin S. Addison
 * @since 3.3
 */
public final class RedisTicketRegistry extends AbstractDistributedTicketRegistry implements DisposableBean {

    private final static String TICKET_PREFIX = "TICKETGRANTINGTICKET:";

    /** redis client. */
    @NotNull
    private final TicketRedisTemplate client;

    /**
     * TGT cache entry timeout in seconds.
     */
    @Min(0)
    private final int tgtTimeout;

    /**
     * ST cache entry timeout in seconds.
     */
    @Min(0)
    private final int stTimeout;


    /**
     * Creates a new instance using the given redis client instance, which is presumably configured via
     * <code>net.spy.redis.spring.redisClientFactoryBean</code>.
     *
     * @param client                      redis client.
     * @param ticketGrantingTicketTimeOut TGT timeout in seconds.
     * @param serviceTicketTimeOut        ST timeout in seconds.
     */
    public RedisTicketRegistry(final TicketRedisTemplate client, final int ticketGrantingTicketTimeOut,
                               final int serviceTicketTimeOut) {
        this.tgtTimeout = ticketGrantingTicketTimeOut;
        this.stTimeout = serviceTicketTimeOut;
        this.client = client;
    }

    protected void updateTicket(final Ticket ticket) {
        logger.debug("Updating ticket {}", ticket);
        try {
            this.client.boundValueOps(TICKET_PREFIX+ticket.getId()).set(ticket,getTimeout(ticket), TimeUnit.SECONDS);
        } catch (final Exception e) {
            logger.error("Failed updating {}", ticket, e);
        }
    }

    public void addTicket(final Ticket ticket) {
        logger.debug("Adding ticket {}", ticket);
        try {
            this.client.boundValueOps(TICKET_PREFIX+ticket.getId()).set(ticket,getTimeout(ticket),TimeUnit.SECONDS);
        }catch (final Exception e) {
            logger.error("Failed adding {}", ticket, e);
        }
    }

    public boolean deleteTicket(final String ticketId) {
        logger.debug("Deleting ticket {}", ticketId);
        try {
            this.client.delete(TICKET_PREFIX+ticketId);
            return true;
        } catch (final Exception e) {
            logger.error("Failed deleting {}", ticketId, e);
        }
        return false;
    }

    public Ticket getTicket(final String ticketId) {
        try {
            final Ticket t = (Ticket) this.client.boundValueOps(TICKET_PREFIX+ticketId).get();
            if (t != null) {
                return getProxiedTicketInstance(t);
            }
        } catch (final Exception e) {
            logger.error("Failed fetching {} ", ticketId, e);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * This operation is not supported.
     *
     * @throws UnsupportedOperationException if you try and call this operation.
     */
    @Override
    public Collection<Ticket> getTickets() {
        Set<Ticket> tickets = new HashSet<Ticket>();
        Set<String> keys = this.client.keys(TICKET_PREFIX + "*");
        for (String key:keys){
            Ticket ticket = this.client.boundValueOps(TICKET_PREFIX+key).get();
            if(ticket==null){
                this.client.delete(TICKET_PREFIX+key);
            }else{
                tickets.add(ticket);
            }
        }
        return tickets;
    }

    public void destroy() throws Exception {
        //do nothing
    }

    /**
     * @param sync set to true, if updates to registry are to be synchronized
     * @deprecated As of version 3.5, this operation has no effect since async writes can cause registry consistency issues.
     */
    @Deprecated
    public void setSynchronizeUpdatesToRegistry(final boolean sync) {}

    @Override
    protected boolean needsCallback() {
        return true;
    }

    private int getTimeout(final Ticket t) {
        if (t instanceof TicketGrantingTicket) {
            return this.tgtTimeout;
        } else if (t instanceof ServiceTicket) {
            return this.stTimeout;
        }
        throw new IllegalArgumentException("Invalid ticket type");
    }

}
