package com.kanfs.omas.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 
 * @TableName administer
 */
@TableName(value ="administer")
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Administer implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String username;

    private String name;

    private String password;

    private String phone;

    @TableField("headUrl")
    private String headUrl;

    private boolean status;

    @TableLogic
    @TableField("isDeleted")
    private boolean isDeleted;

    @TableField("createTime")
    private Date createTime;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Administer that = (Administer) o;

        if (isStatus() != that.isStatus()) return false;
        if (isDeleted() != that.isDeleted()) return false;
        if (!getId().equals(that.getId())) return false;
        if (!getUsername().equals(that.getUsername())) return false;
        if (!getName().equals(that.getName())) return false;
        if (!getPassword().equals(that.getPassword())) return false;
        if (!getPhone().equals(that.getPhone())) return false;
        if (!getHeadUrl().equals(that.getHeadUrl())) return false;
        return getCreateTime().equals(that.getCreateTime());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getUsername().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getPassword().hashCode();
        result = 31 * result + getPhone().hashCode();
        result = 31 * result + getHeadUrl().hashCode();
        result = 31 * result + (isStatus() ? 1 : 0);
        result = 31 * result + (isDeleted() ? 1 : 0);
        result = 31 * result + getCreateTime().hashCode();
        return result;
    }


}