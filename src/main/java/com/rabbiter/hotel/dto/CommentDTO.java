package com.rabbiter.hotel.dto;

/**
 * @author：rabbiter
 * @date：2022/01/01 15:33
 * Description：
 */
public class CommentDTO {

    private String information;
    private Integer type;

    public CommentDTO() {
    }

    public CommentDTO(String information, Integer type) {
        this.information = information;
        this.type = type;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "CommentDTO{" +
                "information='" + information + '\'' +
                ", type=" + type +
                '}';
    }
}
