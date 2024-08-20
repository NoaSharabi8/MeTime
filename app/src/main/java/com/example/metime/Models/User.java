package com.example.metime.Models;
import java.util.ArrayList;
import java.util.HashMap;

public class User {
        private String id;
        private String name;
        private String email;
        private String image;
        private HashMap<String, Integer> appointments = new HashMap<>();
        private HashMap<String, Integer> favorites = new HashMap<>();

        public User() {}

        public User(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public User setId(String id) {
            this.id = id;
            return this;
        }

        public String getName() {
            return name;
        }

        public User setName(String name) {
            this.name = name;
            return this;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getEmail() {
            return email;
        }

        public User setEmail(String email) {
            this.email = email;
            return this;
        }

        public HashMap<String, Integer> getAppointments() {
            return appointments;
        }

        public void setPurchased(HashMap<String, Integer> appointments) {
            this.appointments = appointments;
        }

        public HashMap<String, Integer> getFavorites() {
            return favorites;
        }

        public void setFavorites(HashMap<String, Integer> favorites) {
            this.favorites = favorites;
        }

        public User addFavorite(String gameId) {
            this.favorites.put(gameId, 1);
            return this;
        }


        public User addAppointment(String appointId) {
            this.appointments.put(appointId, 1);
            return this;
        }
    }
