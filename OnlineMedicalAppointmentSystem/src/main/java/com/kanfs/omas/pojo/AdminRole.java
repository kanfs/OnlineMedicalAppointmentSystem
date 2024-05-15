package com.kanfs.omas.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

/**
 * @TableName admin_role
 */
@TableName(value ="admin_role")
@Builder
@Data
public class AdminRole implements Serializable {
    @TableField("adminRoleId")
    @TableId(type = IdType.AUTO)
    private Integer adminRoleId;
    @TableField("adminId")
    private Integer adminId;
    @TableField("roleId")
    private Integer roleId;

    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdminRole adminRole = (AdminRole) o;

        if (!getAdminRoleId().equals(adminRole.getAdminRoleId())) return false;
        if (!getAdminId().equals(adminRole.getAdminId())) return false;
        return getRoleId().equals(adminRole.getRoleId());
    }

    @Override
    public int hashCode() {
        int result = getAdminRoleId().hashCode();
        result = 31 * result + getAdminId().hashCode();
        result = 31 * result + getRoleId().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "AdminRole{" +
                "adminRoleId=" + adminRoleId +
                ", adminId=" + adminId +
                ", roleId=" + roleId +
                '}';
    }
}