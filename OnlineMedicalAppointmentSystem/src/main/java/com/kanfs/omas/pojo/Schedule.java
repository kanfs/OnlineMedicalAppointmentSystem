package com.kanfs.omas.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @TableName schedule
 */
@TableName(value ="schedule")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Schedule implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField("doctorId")
    private Long doctorId;
    @TableField("workDate")
    private Date workDate;
    @TableField("availableNum")
    private Integer availableNum;
    @TableField("status")
    private Boolean status;
    @TableField("price")
    private Double price;
    @TableField("creationTime")
    private Date creationTime;

    private static final long serialVersionUID = 1L;

    public void setStatus(Boolean status) {
        this.status = status;
    }

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
        Schedule other = (Schedule) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getDoctorId() == null ? other.getDoctorId() == null : this.getDoctorId().equals(other.getDoctorId()))
            && (this.getWorkDate() == null ? other.getWorkDate() == null : this.getWorkDate().equals(other.getWorkDate()))
            && (this.getAvailableNum() == null ? other.getAvailableNum() == null : this.getAvailableNum().equals(other.getAvailableNum()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getPrice() == null ? other.getPrice() == null : this.getPrice().equals(other.getPrice()))
            && (this.getCreationTime() == null ? other.getCreationTime() == null : this.getCreationTime().equals(other.getCreationTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getDoctorId() == null) ? 0 : getDoctorId().hashCode());
        result = prime * result + ((getWorkDate() == null) ? 0 : getWorkDate().hashCode());
        result = prime * result + ((getAvailableNum() == null) ? 0 : getAvailableNum().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getPrice() == null) ? 0 : getPrice().hashCode());
        result = prime * result + ((getCreationTime() == null) ? 0 : getCreationTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", doctorId=").append(doctorId);
        sb.append(", workDate=").append(workDate);
        sb.append(", availableNum=").append(availableNum);
        sb.append(", status=").append(status);
        sb.append(", price=").append(price);
        sb.append(", creationTime=").append(creationTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}