package si.fri.rso.samples.customers.models;

import org.eclipse.persistence.annotations.UuidGenerator;

import javax.persistence.*;
import java.util.Date;

public class Comment {

    private String id;
    private String commentContent;
    private Date datePosted;
    private String customerId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public Date getDatePosted() {
        return datePosted;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}