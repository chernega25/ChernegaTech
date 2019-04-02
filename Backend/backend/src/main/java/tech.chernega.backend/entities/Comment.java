package tech.chernega.backend.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Instant;

@Entity
public class Comment {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "body")
    private String body;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "post_id")
    private long postId;

    @Column(name = "created_time")
    private String createdTime;

    public Comment() {
        super();
    }

    public Comment(String body, long userId, long postId, Instant createdTime) {
        this.body = body;
        this.userId = userId;
        this.postId = postId;
        this.createdTime = createdTime.toString();
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getUserId() {
        return userId;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public long getId() {
        return id;
    }

    public long getPostId() {
        return postId;
    }
}
