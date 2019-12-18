package com.rcplatformhk.userpoolserver.config;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Builder
@Data
public class NewUserOutConfigDto extends ConfigDto implements Serializable {

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

    @NotNull(message = "matchVideoCount can not be null")
    @Min(1)
    private Integer matchVideoCount;

    @NotNull(message = "friendCount can not be null")
    @Min(1)
    private Integer friendCount;

    @NotNull(message = "stayMinute can not be null")
    @Min(1)
    private Integer stayMinute;
}

