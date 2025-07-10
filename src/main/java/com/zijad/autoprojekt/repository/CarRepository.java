package com.zijad.autoprojekt.repository;

import com.zijad.autoprojekt.model.Car;
import com.zijad.autoprojekt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findByUser(User user);
    int countByUser(User user);
}


