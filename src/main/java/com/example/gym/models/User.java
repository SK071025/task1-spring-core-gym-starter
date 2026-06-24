package com.example.gym.models;

public abstract class User
{
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean isActive;

    public User() {}

    public User(Long id, String firstName, String lastName, String username, String password, boolean isActive) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static class Builder<T extends Builder<T>> {
        protected Long id;
        protected String firstName;
        protected String lastName;
        protected String username;
        protected String password;
        protected boolean isActive;

        public T id(Long id) { this.id = id; return (T) this; }
        public T firstName(String firstName) { this.firstName = firstName; return (T) this; }
        public T lastName(String lastName) { this.lastName = lastName; return (T) this; }
        public T username(String username) { this.username = username; return (T) this; }
        public T password(String password) { this.password = password; return (T) this; }
        public T isActive(boolean isActive) { this.isActive = isActive; return (T) this; }

        public User build() {
            return new User(id, firstName, lastName, username, password, isActive) {};
        }
    }
}
