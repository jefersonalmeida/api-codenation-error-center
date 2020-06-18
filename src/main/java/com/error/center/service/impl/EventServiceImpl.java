package com.error.center.service.impl;

import com.error.center.repository.EventRepository;
import com.error.center.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    EventRepository repository;
}
