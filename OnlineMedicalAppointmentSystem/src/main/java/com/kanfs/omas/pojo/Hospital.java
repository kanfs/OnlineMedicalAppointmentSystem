package com.kanfs.omas.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 
 * @TableName hospital
 */
@TableName(value ="hospital")
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Hospital implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private String province;

    /**
     * 
     */
    private String city;

    /**
     * 
     */
    private String name;

    /**
     * 医院性质
     */
    private String style;

    /**
     * 医院等级
     */
    private String level;

    /**
     * 医院类型
     */
    private String type;

    /**
     * 
     */
    private String address;

    /**
     * 
     */
    @TableField("departmentNum")
    private Integer departmentNum;

    /**
     * 
     */
    @TableField("doctorNum")
    private Integer doctorNum;

    /**
     * 
     */
    private String telephone;

    /**
     * 
     */
    private String intro;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Hospital other = (Hospital) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getProvince() == null ? other.getProvince() == null : this.getProvince().equals(other.getProvince()))
            && (this.getCity() == null ? other.getCity() == null : this.getCity().equals(other.getCity()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getStyle() == null ? other.getStyle() == null : this.getStyle().equals(other.getStyle()))
            && (this.getLevel() == null ? other.getLevel() == null : this.getLevel().equals(other.getLevel()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(other.getAddress()))
            && (this.getDepartmentNum() == null ? other.getDepartmentNum() == null : this.getDepartmentNum().equals(other.getDepartmentNum()))
            && (this.getDoctorNum() == null ? other.getDoctorNum() == null : this.getDoctorNum().equals(other.getDoctorNum()))
            && (this.getTelephone() == null ? other.getTelephone() == null : this.getTelephone().equals(other.getTelephone()))
            && (this.getIntro() == null ? other.getIntro() == null : this.getIntro().equals(other.getIntro()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getProvince() == null) ? 0 : getProvince().hashCode());
        result = prime * result + ((getCity() == null) ? 0 : getCity().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getStyle() == null) ? 0 : getStyle().hashCode());
        result = prime * result + ((getLevel() == null) ? 0 : getLevel().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
        result = prime * result + ((getDepartmentNum() == null) ? 0 : getDepartmentNum().hashCode());
        result = prime * result + ((getDoctorNum() == null) ? 0 : getDoctorNum().hashCode());
        result = prime * result + ((getTelephone() == null) ? 0 : getTelephone().hashCode());
        result = prime * result + ((getIntro() == null) ? 0 : getIntro().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", province=").append(province);
        sb.append(", city=").append(city);
        sb.append(", name=").append(name);
        sb.append(", style=").append(style);
        sb.append(", level=").append(level);
        sb.append(", type=").append(type);
        sb.append(", address=").append(address);
        sb.append(", departmentNum=").append(departmentNum);
        sb.append(", doctorNum=").append(doctorNum);
        sb.append(", telephone=").append(telephone);
        sb.append(", intro=").append(intro);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}