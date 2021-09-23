package com.cs.event.management.repository;

import com.cs.event.management.entity.EventLog;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<EventLog, String> {}
