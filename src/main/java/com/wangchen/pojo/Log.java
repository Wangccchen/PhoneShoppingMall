package com.wangchen.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Log {
    private int logID;
    private int userID;
    private String operationType;
    private LocalDateTime operTime;
}
