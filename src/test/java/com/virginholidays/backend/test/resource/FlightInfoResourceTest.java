package com.virginholidays.backend.test.resource;

import com.virginholidays.backend.test.configuration.DataSourceConfiguration;
import com.virginholidays.backend.test.repository.FlightInfoRepositoryImpl;
import com.virginholidays.backend.test.service.FlightInfoService;
import com.virginholidays.backend.test.service.FlightInfoServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;

import java.net.URL;
import java.util.concurrent.CompletionStage;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The FlightInfoResource unit tests
 *
 * @author Geoff Perks
 */
@ExtendWith(MockitoExtension.class)
class FlightInfoResourceTest {

    // FIXED by applicant

    @InjectMocks
    FlightInfoRepositoryImpl flightInfoRepository;

    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private DataSourceConfiguration dataSourceConfiguration;


    @InjectMocks
    FlightInfoResource flightInfoResource;


    @Test
    void getResultsEmptyDate() {
        CompletionStage<ResponseEntity<?>> stage = flightInfoResource.getResults("");
        ResponseEntity<?> responseEntity = stage.toCompletableFuture().join();
        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(400, responseEntity.getStatusCodeValue());
    }

    @Test
    void getResultsInvalidDate() {
        CompletionStage<ResponseEntity<?>> stage = flightInfoResource.getResults("abc");
        ResponseEntity<?> responseEntity = stage.toCompletableFuture().join();
        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(400, responseEntity.getStatusCodeValue());
    }

    @Test
    void getResultsSuccess() {
        ClassLoader classLoader = mock(ClassLoader.class);
        URL resource = getClass().getResource("/flights.csv");

        when(dataSourceConfiguration.getCsvLocation()).thenReturn("flights.csv");

        when(resourceLoader.getClassLoader()).thenReturn(classLoader);
        when(classLoader.getResource(anyString())).thenReturn(resource);

        FlightInfoService flightInfoService = new FlightInfoServiceImpl(flightInfoRepository);
        FlightInfoResource flightInfoResource = new FlightInfoResource(flightInfoService);

        CompletionStage<ResponseEntity<?>> stage = flightInfoResource.getResults("2026-10-08");
        ResponseEntity<?> responseEntity = stage.toCompletableFuture().join();
        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(200, responseEntity.getStatusCodeValue());
    }
}

