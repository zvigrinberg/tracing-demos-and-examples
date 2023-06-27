package org.zgrinber.tracing.microservice2.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.zgrinber.tracing.microservice2.entity.Car;

@ApplicationScoped
public class CarRepository implements PanacheMongoRepositoryBase<Car,String> {


    public Car findByCarId(String id) {
        return find("_id",id).firstResult();
    }


//    public void deleteByCarId(String id) {
//         delete("id",id);
//    }
}
