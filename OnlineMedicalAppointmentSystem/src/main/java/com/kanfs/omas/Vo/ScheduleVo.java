package com.kanfs.omas.Vo;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ScheduleVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Long doctorId;

    private String doctorName;

    private String grade;

    private String title;

    private String doctorGoodat;

    private Integer availableNum;

    private Date workDate;

    private Boolean status;

    private Double price;

}

