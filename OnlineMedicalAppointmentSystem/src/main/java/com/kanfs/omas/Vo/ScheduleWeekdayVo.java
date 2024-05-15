package com.kanfs.omas.Vo;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ScheduleWeekdayVo {

    private Long total;

    private Long page;

    private Long limit;

    private List<ScheduleWeekdayRo> scheduleWeekdayRoList;

}
