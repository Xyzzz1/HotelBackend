package com.rabbiter.hotel.dto;

/**
 * @author：hejiaqi
 * @date：2024/5/8
 * Description：
 * 该类的功能为注册，即新用户会先填写注册信息。
 * 存储 邮箱，用户名，密码，性别和电话
 * 该类应搭配PasswordDTO进行使用。
 */
public class RegisterDTO {

    private String email;
    private String userName;
    private String password;
    private Integer sex;
    private String phone;

    public RegisterDTO() {
    }

    public RegisterDTO(String email, String userName, String password, Integer sex, String phone) {
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.sex = sex;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "RegisterDTO{" +
                "email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", sex=" + sex +
                ", phone='" + phone + '\'' +
                '}';
    }
}
