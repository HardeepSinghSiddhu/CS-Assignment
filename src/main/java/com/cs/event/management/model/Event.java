package com.cs.event.management.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Event {
    private String id;
    private EventState state;
    private String host;
    private String timestamp;
    private EventType type;
}
