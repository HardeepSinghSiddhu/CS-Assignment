package com.cs.event.management.controller;

import com.cs.event.management.entity.EventLog;
import com.cs.event.management.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping(value = "/event")
    public ResponseEntity<Object> startEvent() {
        eventService.readLogFile();
        return ResponseEntity.ok()
                .body(Collections.singletonMap("message", "Data Uploaded Successfully"));
    }

    @GetMapping(value = "/event")
    public ResponseEntity<Object> getEventData() {
        List<EventLog> eventLogList = eventService.getEventList();
        return ResponseEntity.ok()
                .body(eventLogList.size() > 0 ? eventLogList : Collections.singletonList("No Record Found"));
    }
}