package com.rcplatformhk.us.config;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Builder
@Data
public class NewUserInConfigDto extends ConfigDto implements Serializable {

    @NotNull(message = "minuteDelay can not be null")
    @Min(1)
    private Integer minuteDelay;

    @NotNull(message = "matchCount can not be null")
    @Min(1)
    private Integer matchCount;

    @NotNull(message = "friendCount can not be null")
    @Min(1)
    private Integer friendCount;
}