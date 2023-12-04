package com.demo.app.service;

import com.demo.app.domain.Shipper;
import com.demo.app.repository.ShipperRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;



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
