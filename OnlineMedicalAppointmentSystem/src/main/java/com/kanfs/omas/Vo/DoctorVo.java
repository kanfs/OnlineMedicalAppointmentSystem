package com.kanfs.omas.Vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DoctorVo {
    private long id;

    private long hospitalId;

    private long departmentId;

    private String doctorName;

    private String hospitalName;

    private String departmentName;

    private String grade;

    private String title;

    private String goodat;

    private double score;

    private String province;

}
