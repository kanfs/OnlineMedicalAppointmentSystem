package com.kanfs.omas.Vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderVo {
    private int id;
    private int patientId;
    private int scheduleId;
    private int status;
    private int number;
    private double price;
    private Date creationTime;
    private int doctorId;
    private Date workDate;
    private int userId;
    private String patientName;
    private String identifyCardNumber;
    private String phone;
    private String doctorName;
    private String departmentName;
    private String hospitalName;
    private String transactionId;
}
