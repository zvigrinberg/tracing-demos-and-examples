package org.zgrinber.tracing.microservice1.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.HttpStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.zgrinber.tracing.common.dto.CarDto;
import org.zgrinber.tracing.common.exceptions.RestApiException;
import org.zgrinber.tracing.microservice1.exceptions.RestApiExceptionHandler;

import java.util.List;

@Default
@ApplicationScoped
public class CarServiceImpl implements CarService{

    @Inject
    private Client restClient;
    @ConfigProperty(name = "microservices.tracing.databaseAppUrl")
    private String dbServiceUrl;
    private final Logger LOG = Logger.getLogger(CarServiceImpl.class);
    @Override
    public CarDto getOneCar(String carId) throws RestApiException {
        WebTarget msURL = restClient.target(dbServiceUrl + "/car/" + carId);
        Response response = msURL.request().get();
        if (response.getStatus()!= HttpStatus.SC_OK)
        {
            throw new RestApiException("Failed to get the car object of car id=" + carId , response.getStatus(),formatOriginalMessage(response));
        }

        return (CarDto)response.getEntity();
    }

    @Override
    public List<CarDto> getAllCars() throws RestApiException {
        WebTarget msURL = restClient.target(dbServiceUrl + "/car");
        Response response = msURL.request().get();
        if (response.getStatus()!= HttpStatus.SC_OK)
        {
            throw new RestApiException("Failed to get list of cars",response.getStatus(), formatOriginalMessage(response));
        }
        List<CarDto> cars = (List<CarDto>) response.getEntity();
        return cars;
    }
    @Override
    public void createCar(CarDto car) throws RestApiException {
        WebTarget msURL = restClient.target(dbServiceUrl + "/car");
        Response response = msURL.request().post(Entity.json(car));
        if (response.getStatus()!= HttpStatus.SC_CREATED)
        {
            throw new RestApiException("Failed to create car, kindly checks logs or turn to system admin for further details",response.getStatus(),formatOriginalMessage(response));
        }

    }

    @Override
    public void updateCar(CarDto car) throws RestApiException {
        WebTarget msURL = restClient.target(dbServiceUrl + "/car");
        Response response = msURL.request().put(Entity.json(car));
        if (response.getStatus()!= HttpStatus.SC_OK)
        {
            throw new RestApiException("Failed to update car, kindly checks logs or turn to system admin for further details",response.getStatus(),formatOriginalMessage(response));
        }

    }

    @Override
    public void deleteCar(String carId) {

    }

    private String formatOriginalMessage(Response response) {
        return response.getEntity() == null ? "" : decodeResponse(response.getEntity());
    }

    private String decodeResponse(Object entity) {
        ObjectMapper om;
        String result;
        if(entity instanceof String) {
            result =  (String) entity;
        }
        else
        {
            om = new ObjectMapper();
            try {
                result = om.writerWithDefaultPrettyPrinter().writeValueAsString(entity);
            } catch (JsonProcessingException e) {
                LOG.warnf("Couldn't decode response object from server, error message of deserialization = %s, original error message of deserialization = %s",e.getMessage(),e.getOriginalMessage());
                result ="";
            }
        }
        return result;
    }


}
