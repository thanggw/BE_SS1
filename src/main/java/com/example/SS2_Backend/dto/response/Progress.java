package com.example.SS2_Backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Progress {
    private String message;
    private boolean inProgress;
    private boolean firstRun;
    private Double runtime;
    private Integer generation;
}
