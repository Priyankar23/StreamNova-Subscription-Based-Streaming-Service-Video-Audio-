package com.example.demo.model;



import jakarta.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;
    private Long PhoneNo;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserSubscription> subscriptions;

    public List<UserSubscription> getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(List<UserSubscription> subscriptions) {
		this.subscriptions = subscriptions;
	}

	public Long getPhoneNo() {
		return PhoneNo;
	}

	public void setPhoneNo(Long phoneNo) {
		PhoneNo = phoneNo;
	}

	@ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public User() {}

    public User(String username, String password, String email, Set<Role> roles,Long PhoneNo ) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
        this.PhoneNo=PhoneNo;
    }

    // Getters and setters
    public String getRole() {
        if (this.roles != null && !this.roles.isEmpty()) {
            // Assuming roles are stored with role names like "ROLE_ADMIN" or "ROLE_USER"
            return this.roles.iterator().next().getName();  // Get the name of the first role in the set
        }
        return null;  // No role found
    }
}