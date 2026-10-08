package com.virginholidays.backend.test.service;

import com.virginholidays.backend.test.configuration.DataSourceConfiguration;
import com.virginholidays.backend.test.repository.FlightInfoRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ResourceLoader;

import java.net.URL;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The FlightInfoServiceImpl unit tests
 *
 * @author Geoff Perks
 */
@ExtendWith(MockitoExtension.class)
class FlightInfoServiceImplTest {

    // FIXED by applicant

    @InjectMocks
    FlightInfoRepositoryImpl flightInfoRepository;

    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private DataSourceConfiguration dataSourceConfiguration;

    @Test
    void findFlightByDate() {
        ClassLoader classLoader = mock(ClassLoader.class);
        URL resource = getClass().getResource("/flights.csv");

        when(dataSourceConfiguration.getCsvLocation()).thenReturn("flights.csv");

        when(resourceLoader.getClassLoader()).thenReturn(classLoader);
        when(classLoader.getResource(anyString())).thenReturn(resource);
        flightInfoRepository.findAll();
    }

}