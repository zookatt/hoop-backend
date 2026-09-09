package zotov.hoop_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_characteristics")
public class UserCharacteristics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public UserCharacteristics() {
    }

    public UserCharacteristics(String name, User user) {
        this.name = name;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public User getUser() {
        return user;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
