package com.rabbiter.hotel.dto;

/**
 * @author：rabbiter
 * @date：2022/01/01 12:17
 * Description：
 */
public class LoginDTO {

    private String email;
    private String password;
    private String phone;

    public LoginDTO() {
    }

    public LoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginDTO{" +
                "email='" + email + '\'' +
                "phone='"+phone+'\''+
                ", password='" + password + '\'' +
                '}';
    }
}

