package com.virginholidays.backend.test.resource;

import com.virginholidays.backend.test.api.Flight;
import com.virginholidays.backend.test.service.FlightInfoService;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static org.springframework.http.CacheControl.noCache;
import static org.springframework.http.ResponseEntity.status;

/**
 * @author Geoff Perks
 *
 * The FlightInfoResource
 */
@RestController
public class FlightInfoResource {

    private final FlightInfoService flightInfoService;

    /**
     * The constructor
     *
     * @param flightInfoService the flightInfoService
     */
    public FlightInfoResource(FlightInfoService flightInfoService) {
        this.flightInfoService = flightInfoService;
    }

    /**
     * Resource method for returning flight results
     *
     * @param date the chosen date
     * @return flights for the day of the chosen date
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{date}/results")
    public CompletionStage<ResponseEntity<?>> getResults(@PathVariable("date") @NotEmpty String date) {
        if (StringUtils.isEmpty(date)) {
            return (CompletableFuture.supplyAsync(() -> {
                return (status(HttpStatus.BAD_REQUEST).cacheControl(noCache()).body("Date field is empty"));
            }));
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = null;

        try {
            localDate = LocalDate.parse(date, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            return (CompletableFuture.supplyAsync(() -> {
                return (status(HttpStatus.BAD_REQUEST).cacheControl(noCache()).body("Invalide value for date field. Please enter in yyyy-MM-dd format."));
            }));
        }

        return flightInfoService.findFlightByDate(localDate).thenApply(maybeResults -> {

            // no results, no content
            if (maybeResults.isEmpty()) {
                return status(HttpStatus.NO_CONTENT).cacheControl(noCache()).build();
            }

            List<Flight> results = maybeResults.get();

            return status(HttpStatus.OK).cacheControl(noCache()).body(results);
        });
    }
}
