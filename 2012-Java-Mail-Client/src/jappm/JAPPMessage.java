/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jappm;

import java.util.Date;

/**
 *
 * @author Ivan
 */
public class JAPPMessage {

    private Integer messageId;
    private Integer accountId = 1;
    private Integer folderId;
    private Long uid;
    private Boolean seen;
    private Integer priority;
    private String subject;
    private String from;
    private String to;
    private String cc;
    private String bcc;
    private Date sentDate;
    private String plaintextBody;
    private Boolean attachementsAvliable = false;
    private byte[] rawMessage;

    public JAPPMessage(Integer folderId,
            Long uid,
            String subject,
            String from,
            String to,
            String cc,
            Date dateSent) {

        this.folderId = folderId;
        this.uid = uid;
        this.subject = subject;
        this.from = from;
        this.to = to;
        this.cc = cc;
        this.sentDate = dateSent;
        this.seen = false;

    }

    public void setSeen() {
        this.seen = true;
    }

    public boolean getSeen() {
        return this.seen;
    }

    public void setPlaintextBody(String text) {
        this.plaintextBody = text;
    }

    public void setRawMessage(byte[] rawMessage) {

        this.rawMessage = rawMessage;
    }

    public void setAttachmentsAvaliable() {
        this.attachementsAvliable = true;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public boolean getAttachmentsAvaliable() {
        return this.attachementsAvliable;
    }

    public byte[] getRawMessage() {

        return this.rawMessage;
    }

    public String getSubject() {
        return this.subject;
    }

    public String getFrom() {
        return this.from;
    }

    public String getTo() {
        return this.to;
    }

    public String getCC() {
        return this.cc;
    }

    public Date getSentDate() {
        return this.sentDate;
    }

    public Integer getFolderId() {
        return this.folderId;
    }

    public Boolean attachmentsAvaliable() {
        return this.attachementsAvliable;
    }

    public Long getUID() {
        return this.uid;
    }

    public Integer getMessageId() {
        return this.messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public Integer getAccountId() {
        return this.accountId;
    }
}
