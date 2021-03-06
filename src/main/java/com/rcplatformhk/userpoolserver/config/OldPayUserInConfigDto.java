package com.rcplatformhk.userpoolserver.config;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Builder
@Data
public class OldPayUserInConfigDto extends ConfigDto implements Serializable {

    @NotNull(message = "dayScope can not be null")
    @Min(1)
    private Integer dayScope;

    @NotNull(message = "dayActive can not be null")
    @Min(1)
    private Integer dayActive;

    @NotNull(message = "hourScope can not be null")
    @Min(1)
    private Integer hourScope;

    @NotNull(message = "minuteDelay can not be null")
    @Min(1)
    private Integer minuteDelay;
}