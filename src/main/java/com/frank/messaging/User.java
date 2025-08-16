package com.frank.messaging;

public class User { // encapsulation
    private String username;
    private String password;
    private String address;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) { // setter
        System.out.println("Setting username with: " + username);
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
