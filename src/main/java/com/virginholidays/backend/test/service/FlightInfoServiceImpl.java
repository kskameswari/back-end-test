package com.virginholidays.backend.test.service;

import com.virginholidays.backend.test.api.Flight;
import com.virginholidays.backend.test.repository.FlightInfoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * The service implementation of FlightInfoService
 *
 * @author Geoff Perks
 */
@Service
public class FlightInfoServiceImpl implements FlightInfoService {

    private final FlightInfoRepository flightInfoRepository;

    private final CompletionStage<Optional<List<Flight>>> flightDetails;

    /**
     * The constructor
     *
     * @param flightInfoRepository the flightInfoRepository
     */
    public FlightInfoServiceImpl(FlightInfoRepository flightInfoRepository) {
        this.flightInfoRepository = flightInfoRepository;
        flightDetails = flightInfoRepository.findAll();
    }

    @Override
    public CompletionStage<Optional<List<Flight>>> findFlightByDate(LocalDate outboundDate) {

        return flightDetails.thenApply(optList ->
                optList.map(list -> list.stream()
                        .filter(flight -> {
                            if (flight.days().contains(outboundDate.getDayOfWeek())) {
                                return true;
                            } else {
                                return false;
                            }
                        })
                        .sorted(Comparator.comparing(Flight::departureTime))
                        .collect(Collectors.toList())
                )
        );
    }
}
