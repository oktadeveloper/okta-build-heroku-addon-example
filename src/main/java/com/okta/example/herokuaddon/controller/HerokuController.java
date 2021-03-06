package com.okta.example.herokuaddon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.okta.example.herokuaddon.model.HerokuProvisionRequest;
import com.okta.example.herokuaddon.model.HerokuProvisionResponse;
import com.okta.example.herokuaddon.service.HerokuProvisionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

import static com.okta.example.herokuaddon.controller.HerokuController.HEROKU_URI;

@RestController
@RequestMapping(HEROKU_URI)
public class HerokuController {

    @Autowired
    ObjectMapper mapper;

    static final String HEROKU_URI = "/heroku";
    static final String RESOURCES_URI = "/resources";

    private HerokuProvisionService herokuProvisionService;

    private static final Logger log = LoggerFactory.getLogger(HerokuController.class);

    public HerokuController(HerokuProvisionService herokuProvisionService) {
        this.herokuProvisionService = herokuProvisionService;
    }

    @PostMapping(RESOURCES_URI)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public @ResponseBody HerokuProvisionResponse provision(
        @RequestBody HerokuProvisionRequest request
    ) throws IOException, InterruptedException {
        log.info("Incoming Heroku request:\n{}", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request));

        herokuProvisionService.provision(request);

        return new HerokuProvisionResponse(request.getUuid(), "Provisioning Okta Org");
    }

    @PutMapping(RESOURCES_URI + "/{resource_uuid}")
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public @ResponseBody Map<String, String> planChange(
        @PathVariable("resource_uuid") String resourceUuid, @RequestBody Map<String, String> planRequest
    ) {
        return Map.of("message", "plan change not supported");
    }

    @DeleteMapping(RESOURCES_URI + "/{resource_uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deprovision(@PathVariable("resource_uuid") String resourceUuid) {
        log.error("Delete request received for heroku resource: {}. Delete not supported yet.", resourceUuid);
    }
}