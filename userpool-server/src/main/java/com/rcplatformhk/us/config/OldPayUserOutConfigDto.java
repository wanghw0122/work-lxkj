package com.rcplatformhk.us.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OldPayUserOutConfigDto extends ConfigDto implements Serializable {

    @NotNull(message = "videoCount1 can not be null")
    @Min(1)
    private Integer videoCount1;

    @NotNull(message = "videoTime can not be null")
    @Min(1)
    private Integer videoTime;

    @NotNull(message = "videoCount2 can not be null")
    @Min(1)
    private Integer videoCount2;

    @NotNull(message = "videoCalledCount can not be null")
    @Min(1)
    private Integer videoCalledCount;

    @NotNull(message = "stayMinute can not be null")
    @Min(1)
    private Integer stayMinute;
}