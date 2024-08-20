package com.example.metime.Models;

import com.google.firebase.database.DatabaseReference;

public class Client {

    private String id;
        private String name;
        private String email;
        private String password;

    public Client(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Client() {}

    public String getId() {
        return id;
    }


    public Client setId(String id) {
        this.id = id;
        return this;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getName() {
            return name;
        }
        public Client setName(String name) {
            this.name = name;
            return this;
        }


    public String getEmail() {
            return email;
        }

        public Client setEmail(String email) {
            this.email = email;
            return this;
        }
    }
