package com.demo.app.repository;

import com.demo.app.domain.Shipper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipperRepository extends JpaRepository<Shipper, Long> {
    Shipper findByName(String name);
}
