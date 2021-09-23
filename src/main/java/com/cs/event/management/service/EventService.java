package com.cs.event.management.service;

import com.cs.event.management.entity.EventLog;
import com.cs.event.management.model.EventState;
import com.cs.event.management.repository.EventRepository;
import com.cs.event.management.exception.EventException;
import com.cs.event.management.model.Event;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    @Value("${event.file.location}")
    private String fileLocation;

    public void readLogFile() {
        try {
            log.info("Event capture service started");
            ObjectMapper objectMapper = new ObjectMapper();
            List<Event> eventList = new ArrayList<>();
            log.info("Initiating log reading activity");
            Files.lines(new File(fileLocation).toPath())
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .forEach(line -> {
                        try {
                            Event event = objectMapper.readValue(line, Event.class);
                            eventList.add(event);
                            log.debug("Event added in List : Event Details : " + event);
                        } catch (IOException exception) {
                            log.error("Error Reading Log File - Reason: " + exception.getMessage());
                            throw new EventException("Error in Log File / File Path");
                        }
                    });

            log.info("Total Entries Found : " + eventList.size());
            delayCheck(eventList);
        } catch (IOException exception) {
            log.error("Error Reading Log File - Reason: " + exception.getMessage());
            throw new EventException("Error in Log File");
        }
    }

    public List<EventLog> getEventList() {
        List<EventLog> eventLogList = new ArrayList<>();
        Iterable<EventLog> eventLogIterable = eventRepository.findAll();
        Iterator<EventLog> eventLogIterator = eventLogIterable.iterator();
        while (eventLogIterator.hasNext()) {
            eventLogList.add(eventLogIterator.next());
        }

        return eventLogList;
    }

    private void delayCheck(List<Event> eventList) {
        log.info("Initiate calculating event time duration");
        List<Event> tempEventList = eventList.stream()
                .filter(distinctByEventId(Event::getId))
                .collect(Collectors.toList());

        log.info("Total number of distinct events in logfile : " + tempEventList.size());
        for(Event event : tempEventList) {
            List<Event> eventIdList = eventList.stream()
                    .filter(event1 -> event1.getId().equals(event.getId()))
                    .collect(Collectors.toList());

            EventLog eventLog = createLogData(eventIdList);
            log.info(eventLog.toString());
            log.info("Storing event log in Database");
            eventRepository.save(eventLog);
            log.info("Event logged successfully in Database");
        }
    }

    private EventLog createLogData(List<Event> eventList) {
        log.info("Start creating database record entries for Event Id : " + eventList.get(0).getId());
        EventLog eventLog = new EventLog();
        eventLog.setEventId(eventList.get(0).getId());
        eventLog.setEventHost(eventList.get(0).getHost() == null || eventList.get(0).getHost().equals("") ?
                "" : eventList.get(0).getHost());
        eventLog.setEventType(eventList.get(0).getType() == null || eventList.get(0).getType().name().equals("") ?
                "" : eventList.get(0).getType().name());
        Long duration = calculateEventTimeDuration(eventList);
        eventLog.setEventDuration(String.valueOf(duration));
        eventLog.setAlert(duration > 4);

        log.info("database record entry created successfully for Event Id : " + eventList.get(0).getId());
        return eventLog;
    }

    private static <T> Predicate<T> distinctByEventId(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> uniqueMap = new ConcurrentHashMap<>();
        return t -> uniqueMap.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    private Long calculateEventTimeDuration(List<Event> eventList) {
        log.info("Event time details : Id - " + eventList.get(0).getId());
        String startTime = eventList.stream()
                .filter(e -> e.getState() == EventState.STARTED)
                .findAny()
                .orElse(new Event())
                .getTimestamp();

        String endTime = eventList.stream()
                .filter(e -> e.getState() == EventState.FINISHED)
                .findAny()
                .orElse(new Event())
                .getTimestamp();

        if(startTime != null && endTime != null) {
            return Long.parseLong(endTime) - Long.parseLong(startTime);
        } else {
            log.info("Event was not FINISHED in record - Returning 0 time duration for Event id - " + eventList.get(0).getId());
            return 0L;
        }
    }
}