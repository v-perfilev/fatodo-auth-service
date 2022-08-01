package com.persoff68.fatodo.client;

import com.persoff68.fatodo.client.configuration.FeignSystemConfiguration;
import com.persoff68.fatodo.model.dto.CreateEventDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "event-service", primary = false,
        configuration = {FeignSystemConfiguration.class},
        qualifiers = {"feignEventServiceClient"})
public interface EventServiceClient {

    @PostMapping(value = "/api/event/default")
    void addDefaultEvent(@RequestBody CreateEventDTO createEventDTO);

}