package com.example.gym.models;

import java.time.LocalDate;

public class Trainee extends User {
    private LocalDate dateOfBirth;
    private String address;

    public Trainee() {}

    public Trainee(Long id, String firstName, String lastName, String username, String password,
                   boolean isActive, LocalDate dateOfBirth, String address) {
        super(id, firstName, lastName, username, password, isActive);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static class Builder extends User.Builder<Builder> {
        private LocalDate dateOfBirth;
        private String address;

        public Builder dateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        @Override
        public Trainee build() {
            return new Trainee(
                    super.id,
                    super.firstName,
                    super.lastName,
                    super.username,
                    super.password,
                    super.isActive,
                    this.dateOfBirth,
                    this.address
            );
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}