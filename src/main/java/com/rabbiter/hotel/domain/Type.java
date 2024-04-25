package com.rabbiter.hotel.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @author：rabbiter
 * @date：2022/01/01 17:25
 * Description：
 */
@TableName(value = "type")
public class Type {

    @TableId(value = "id")
    private Integer id;
    @TableField(value = "type_name")
    private String typeName;
    @TableField(value = "price")
    private Double price;
    @TableField(value = "introduce")
    private String introduce;
    @TableField(value = "feature")
    private String feature;
    @TableField(value = "cover_image")
    private String coverImage;
    @TableField(value = "detail_url")
    private String detailUrl;

    public Type() {
    }

    public Type(Integer id, String typeName, Double price, String introduce, String feature, String coverImage, String detailUrl) {
        this.id = id;
        this.typeName = typeName;
        this.price = price;
        this.introduce = introduce;
        this.feature = feature;
        this.coverImage = coverImage;
        this.detailUrl = detailUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", typeName='" + typeName + '\'' +
                ", price=" + price +
                ", introduce='" + introduce + '\'' +
                ", feature='" + feature + '\'' +
                ", coverImage='" + coverImage + '\'' +
                ", detailUrl='" + detailUrl + '\'' +
                '}';
    }
}
