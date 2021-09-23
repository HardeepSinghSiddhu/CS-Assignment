package com.cs.event.management.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "EventLog")
public class EventLog {
    @Id
    @Column(name = "id")
    private String eventId;

    @Column(name = "timeSpame")
    private String eventDuration;

    @Column(name = "type")
    private String eventType;

    @Column(name = "host")
    private String eventHost;

    @Column(name = "alert")
    private boolean alert;
}
