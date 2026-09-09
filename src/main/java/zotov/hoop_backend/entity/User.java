package zotov.hoop_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Boolean active = true;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private UserRole role;

    public User() {
    }

    public User(UserRole role) {
        this.role = role;
        this.active = true;
    }

    public Integer getId() {
        return id;
    }

    public Boolean getActive() {
        return active;
    }

    public UserRole getRole() {
        return role;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
