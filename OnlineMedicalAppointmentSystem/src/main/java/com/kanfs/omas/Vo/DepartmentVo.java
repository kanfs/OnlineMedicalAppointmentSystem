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
public class DepartmentVo {
    private long id;

    private String name;

    private String hospitalName;

    private String intro;

    private int doctorNum;
}
