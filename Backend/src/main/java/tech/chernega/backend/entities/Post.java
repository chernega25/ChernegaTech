package tech.chernega.backend.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Instant;

@Entity
public class Post {

    @Id
    @GeneratedValue
    private long id;

    @Column
    private String header;

    @Column
    private String body;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "created_time")
    private Instant createdTime;

    public Post() {
        super();
    }

    public Post(String header, String body, long userId, Instant createdTime) {
        this.header = header;
        this.body = body;
        this.userId = userId;
        this.createdTime = createdTime;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
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

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public long getId() {
        return id;
    }
}
