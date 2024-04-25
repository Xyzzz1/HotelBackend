package com.rabbiter.hotel.dto;

/**
 * @author：rabbiter
 * @date：2022/01/04 14:05
 * Description：
 */
public class AdminLoginDTO {

    private String userName;
    private String password;

    public AdminLoginDTO() {
    }

    public AdminLoginDTO(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "AdminLoginDTO{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
