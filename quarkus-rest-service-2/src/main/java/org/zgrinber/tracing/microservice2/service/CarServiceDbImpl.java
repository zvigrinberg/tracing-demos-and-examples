package org.zgrinber.tracing.microservice2.service;

import io.quarkus.mongodb.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.zgrinber.tracing.common.dto.CarDto;
import org.zgrinber.tracing.common.exceptions.RestApiException;
import org.zgrinber.tracing.common.service.CarService;
import org.zgrinber.tracing.microservice2.entity.Car;
import org.zgrinber.tracing.microservice2.repository.CarRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Default
@ApplicationScoped
public class CarServiceDbImpl implements CarService {

    Logger LOG = Logger.getLogger(CarServiceDbImpl.class);
    @Inject
    private CarRepository carRepository;
    Client client = ClientBuilder.newClient();

    @Override
    public CarDto getOneCar(String carId) throws RestApiException {
        CarDto newCarDtoInstance;
        Car car = carRepository.findByCarId(carId);
        if(car!= null) {

            newCarDtoInstance = CarDto.getNewCarDtoInstance();
            mapDbToDto(car,newCarDtoInstance);
        }

        else
        {

            throw new RestApiException(String.format("Cannot find car with id=%s", carId), 404, String.format("Cannot find car with id=%s in database",carId));
        }

        return newCarDtoInstance;
    }

    @Override

    public List<CarDto> getAllCars() throws RestApiException {
        List<Car> list;
        List<CarDto> dtoList;
        try {

            PanacheQuery<Car> allCars = carRepository.findAll();
            list = allCars.stream().toList();

            dtoList = new LinkedList<>();
            list.forEach(car -> {
                CarDto current = CarDto.getNewCarDtoInstance();
                mapDbToDto(car, current);
                dtoList.add(current);
            });
        }
        catch (Exception e)
        {
            throw new RestApiException(String.format("Cannot get all cars due to general error")  ,500, e.getMessage());
        }
        return dtoList;
    }

    @Override
    public void createCar(CarDto carDto) throws RestApiException {
        UUID uuid = UUID.randomUUID();
        carDto.setId(uuid.toString());
        Car newCar = new Car();
        mapDtoToDb(carDto,newCar);
        try {
            carRepository.persist(newCar);
            LOG.info("about to call notifications microservice");
            WebTarget msURL = client.target("http://localhost:8082" + "/notify");
            Response response = msURL.request().get();
        }
        catch (Exception e)
        {
            throw new RestApiException(String.format("Cannot create car due to general error")  ,500, e.getMessage());
        }

    }



    @Override
    public void updateCar(CarDto carDto) throws RestApiException {
        try {
            Car car = carRepository.findById(carDto.getId());
            if (car != null) {
                mapDtoToDb(carDto, car);
                carRepository.persist(car);
            } else {
                String formattedMessage = String.format("Car %s doesn't exists on database, can't update it", carDto);
                throw new RestApiException(String.format("Cannot find car with id=%s", carDto.getId()), 404, formattedMessage);
            }
            }
        catch (Exception e)
        {
            throw new RestApiException(String.format("Cannot update car due to general error")  ,500, e.getMessage());
        }
    }

    @Override
    public void deleteCar(String carId) throws RestApiException {
        try {
            carRepository.deleteById(carId);
        }
        catch (Exception e)
        {
            throw new RestApiException(String.format("Cannot delete car with id=%s due to general error",carId)  ,500, e.getMessage());
        }
    }

    private void mapDbToDto(Car car, CarDto newCarDtoInstance) {
        newCarDtoInstance.setColor(car.getColor());
        newCarDtoInstance.setCountry(car.getCountryOfOrigin());
        newCarDtoInstance.setCurrency(car.getCurrency());
        newCarDtoInstance.setId(car.getCarId());
        newCarDtoInstance.setManufacturer(car.getCompanyManufacturer());
        newCarDtoInstance.setModel(car.getModel());
        newCarDtoInstance.setPrice(car.getTotalPrice());
        newCarDtoInstance.setYear(car.getProductionYear());

    }

    private void mapDtoToDb(CarDto carDto, Car newCar) {
        newCar.setColor(carDto.getColor());
        newCar.setCompanyManufacturer(carDto.getManufacturer());
        newCar.setCountryOfOrigin(carDto.getCountry());
        newCar.setCurrency(carDto.getCurrency());
        newCar.setCarId(carDto.getId());
        newCar.setModel(carDto.getModel());
        newCar.setProductionYear(carDto.getYear());
        newCar.setTotalPrice(carDto.getPrice());

    }

}
