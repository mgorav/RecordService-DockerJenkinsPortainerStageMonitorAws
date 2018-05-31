package com.gm.record.api;

import com.codahale.metrics.annotation.Metered;
import com.gm.record.exception.RecordNotFoundException;
import com.gm.record.model.Record;
import com.gm.record.service.RecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.stagemonitor.alerting.annotation.SLA;

import static java.lang.String.format;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Rest controller which implements Record APIs
 */
@Controller
@RequestMapping(value = "v1.0/api/")
@Api("Record APIs")
@Slf4j
public class RecordApis {

    @Autowired
    private RecordService recordService;

    @SLA()
    @Metered(name = "Search_Record_By_Name")
    @RequestMapping(method = GET, value = "/records", params = {"page", "size"}, produces = {APPLICATION_JSON_VALUE})
    @ResponseBody
    @ResponseStatus(OK)
    @ApiOperation(value = "Returns list of records based on the search criteria")
    @ApiResponse(code = 200, message = "Returns list of records based on the search criteria", response = PagedResources.class)
    public PagedResources<Record> findByCriteria(@RequestParam(value = "search") String search,
                                                 @RequestParam("page") int page,
                                                 @RequestParam("size") int size,
                                                 PagedResourcesAssembler pagedAssembler) {


        PagedResources<Record> resources = pagedAssembler.toResource(
                recordService.findByCriteria(search, page, size),
                linkTo(methodOn(this.getClass())
                        .findByCriteria(search, page, size, pagedAssembler))
                        .withSelfRel());


        if (resources.getContent().isEmpty()) {
            String msg = format("Cannot find matching record for criteria: %s, " +
                    "page: %s and size %s", search, page, size);
            log.debug(msg);
            throw new RecordNotFoundException(msg);
        }

        return resources;

    }

    @RequestMapping(method = GET, value = "/records/name/{name}", produces = {APPLICATION_JSON_VALUE})
    @ResponseBody
    @ResponseStatus(OK)
    @ApiOperation(value = "Returns record by name")
    @ApiResponse(code = 200, message = "Returns record by name", response = Record.class)
    public Record findByName(@PathVariable String name) {

        Record record = recordService.findByName(name);

        if (record == null) {
            String msg = format("Record with name = %s not found", name);
            throw new RecordNotFoundException(msg);
        }

        return record;
    }

    @RequestMapping(method = GET, value = "/records/phone/{phone}", produces = {APPLICATION_JSON_VALUE})
    @ResponseBody
    @ResponseStatus(OK)
    @ApiOperation(value = "Returns record by phone")
    @ApiResponse(code = 200, message = "Returns record by phone", response = Record.class)
    public Record findByPhone(@PathVariable String phone) {

        Record record = recordService.findByPhone(phone);

        if (record == null) {
            String msg = format("Record with phone = %s not found", phone);
            throw new RecordNotFoundException(msg);
        }

        return record;
    }


    @RequestMapping(method = POST, value = "/records/", produces = {APPLICATION_JSON_VALUE})
    @ResponseBody
    @ResponseStatus(CREATED)
    @ApiOperation(value = "Create a record")
    @ApiResponse(code = 201, message = "Create a record", response = Record.class)
    public Resource<Record> create(@RequestBody Record record) {

        Record createdRecord = recordService.create(record);

        Resource<Record> resource = new Resource(createdRecord);
        resource.add(linkTo(methodOn(this.getClass()).create(createdRecord))
                .slash(createdRecord.getId())
                .withSelfRel());

        return resource;

    }

    @RequestMapping(method = PUT, value = "/records/", produces = {APPLICATION_JSON_VALUE})
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    @ApiOperation(value = "Updated a record")
    @ApiResponse(code = 202, message = "Update a record", response = Record.class)
    public Resource<Record> update(@RequestBody Record record) {

        Record updatedRecord = recordService.update(record);

        Resource<Record> resource = new Resource(updatedRecord);
        resource.add(linkTo(methodOn(this.getClass()).update(updatedRecord))
                .slash(updatedRecord.getId())
                .withSelfRel());

        return resource;

    }


    @RequestMapping(method = DELETE, value = "/records/", produces = {APPLICATION_JSON_VALUE})
    @ResponseBody
    @ResponseStatus(NO_CONTENT)
    @ApiOperation(value = "Delete a record")
    @ApiResponse(code = 204, message = "Update a record", response = Record.class)
    public void delete(@RequestBody Record record) {

        recordService.delete(record);

    }

}
