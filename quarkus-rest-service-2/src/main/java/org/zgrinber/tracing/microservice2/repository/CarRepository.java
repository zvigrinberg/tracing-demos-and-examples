package org.zgrinber.tracing.microservice2.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.zgrinber.tracing.microservice2.entity.Car;

@ApplicationScoped
public class CarRepository implements PanacheMongoRepositoryBase<Car,String> {


    @Override
    public Car findById(String id) {
        return find("id",id).firstResult();
    }


//    public void deleteByCarId(String id) {
//         delete("id",id);
//    }
}
