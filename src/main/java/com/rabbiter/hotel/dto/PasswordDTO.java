package com.rabbiter.hotel.dto;

/**
 * @author：hejiaqi
 * @date：2024/5/8
 * Description：
 * 该类只有旧密码和新密码两个属性
 * 用于更换密码时。
 * 该类使用的时候，应与LoginDTO进行对应使用，即同一个用户的登陆信息和旧密码，新密码信息一致。
 * 也可将此处的旧密码信息属性增加到LoginDTO，舍弃该类
 *
 */
public class PasswordDTO {

    private String oldPassword;//旧密码
    private String newPassword;//新密码

    public PasswordDTO() {
    }

    public PasswordDTO(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "PasswordDTO{" +
                "oldPassword='" + oldPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }
}
