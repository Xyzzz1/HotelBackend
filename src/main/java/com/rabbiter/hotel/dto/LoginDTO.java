package com.rabbiter.hotel.dto;

/**
 * @author：hejiaqi
 * @date：2024/5/8
 * Description：
 * 用户登陆界面时存储的信息，存储邮箱，密码，电话三个信息。
 * 将用户的账号密码记录，用于登陆界面
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

