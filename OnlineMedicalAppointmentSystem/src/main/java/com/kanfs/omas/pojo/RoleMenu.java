package com.kanfs.omas.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

/**
 * @TableName role_menu
 */
@TableName(value ="role_menu")
@Data
public class RoleMenu implements Serializable {
    @TableId(type = IdType.AUTO)
    private int id;

    private Long roleId;

    private Long menuId;

    public RoleMenu(long roleId, long menuId){
        this.roleId = roleId;
        this.menuId = menuId;
    }

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", roleId=").append(roleId);
        sb.append(", menuId=").append(menuId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}