package com.example.gym.models;

public class Trainer extends User {
    private String specialization;

    public Trainer() {}

    public Trainer(Long id, String firstName, String lastName, String username, String password,
                   boolean isActive, String specialization) {
        super(id, firstName, lastName, username, password, isActive);
        this.specialization = specialization;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public static class Builder extends User.Builder<Builder> {
        private String specialization;

        public Builder specialization(String specialization) {
            this.specialization = specialization;
            return this;
        }

        @Override
        public Trainer build() {
            return new Trainer(
                    super.id,
                    super.firstName,
                    super.lastName,
                    super.username,
                    super.password,
                    super.isActive,
                    this.specialization
            );
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
