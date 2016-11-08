package org.jasig.cas.ticket.registry.util.serialize;

import org.apache.log4j.Logger;

import java.io.Closeable;

/**
 * @author lumz
 *
 * @date 2016-7-12 10:44:07
 */
public abstract class SerializeTranscoder {

    protected static Logger logger = Logger.getLogger(SerializeTranscoder.class);

    public abstract byte[] serialize(Object value);

    public abstract Object deserialize(byte[] in);

    public void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                logger.info("Unable to close " + closeable, e);
            }
        }
    }
}
