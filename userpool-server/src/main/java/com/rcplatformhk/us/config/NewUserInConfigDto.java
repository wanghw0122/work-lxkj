package com.rcplatformhk.us.config;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.rcplatformhk.utils.SerializeUtils;
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
public class NewUserInConfigDto extends ConfigDto {

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