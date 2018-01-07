package si.fri.rso.samples.customers.models;

import org.eclipse.persistence.annotations.UuidGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "comment")
@NamedQueries(value =
        {
                @NamedQuery(name = "Comment.getAll", query = "SELECT c FROM comment c")
        })
@UuidGenerator(name = "idGenerator")
public class Comment {

    @Id
    @GeneratedValue(generator = "idGenerator")
    private String id;

    @Column(name = "comment_content")
    private String commentContent;

    @Column(name = "date_posted")
    private Date datePosted;

    @Column(name = "customer_id")
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