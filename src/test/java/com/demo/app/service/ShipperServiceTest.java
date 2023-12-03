package com.demo.app.service;

import com.demo.app.domain.Shipper;
import com.demo.app.repository.ShipperRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShipperServiceTest {

    @InjectMocks
    private ShipperService shipperService;

    @Mock
    private ShipperRepository shipperRepository;

    @Test
    public void testFindByIdShipperFound(){

    }

}
