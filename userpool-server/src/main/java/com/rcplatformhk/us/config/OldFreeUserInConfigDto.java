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
public class OldFreeUserInConfigDto extends ConfigDto implements Serializable {

    @NotNull(message = "dayScope can not be null")
    @Min(1)
    private Integer dayScope;

    @NotNull(message = "dayActive can not be null")
    @Min(1)
    private Integer dayActive;

    @NotNull(message = "minuteDelay can not be null")
    @Min(1)
    private Integer minuteDelay;
}