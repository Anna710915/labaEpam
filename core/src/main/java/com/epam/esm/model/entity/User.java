package com.epam.esm.model.entity;

import com.epam.esm.model.audit.AuditListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@EntityListeners(AuditListener.class)
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column(name = "username")
    private String username;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Order> orders = new ArrayList<>();

    public User(){}

    public User(long userId){
        this.userId = userId;
    }

    public User(String username){
        this.username = username;
    }

    public User(long userId, String username){
        this.userId = userId;
        this.username = username;
    }

    public long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (userId != user.userId) return false;
        if (username != null ? !username.equals(user.username) : user.username != null) return false;
        return orders != null ? orders.equals(user.orders) : user.orders == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (orders != null ? orders.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("userId=").append(userId);
        sb.append(", username='").append(username).append('\'');
        sb.append(", orders=").append(orders);
        sb.append('}');
        return sb.toString();
    }
}
