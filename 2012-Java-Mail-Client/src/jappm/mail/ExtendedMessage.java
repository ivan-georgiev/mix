/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jappm.mail;

import javax.mail.Message;

/**
 *
 * @author Ivan
 */
public class ExtendedMessage {

    Message message;
    Long uidId;

    public ExtendedMessage(Message message, Long uid) {
        this.message = message;
        this.uidId = uid;
    }

    public Message getMessage() {
        return this.message;
    }

    public Long getUID() {
        return this.uidId;
    }
}