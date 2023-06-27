package org.zgrinber.tracing.microservice1.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpResponseStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.zgrinber.tracing.common.dto.CarDto;
import org.zgrinber.tracing.common.exceptions.RestApiException;
import org.zgrinber.tracing.common.service.CarService;

import java.util.List;

@Default
@ApplicationScoped
public class CarServiceImpl implements CarService {


    private Client restClient= ClientBuilder.newClient();
    @ConfigProperty(name = "microservices.tracing.databaseAppUrl")
    private String dbServiceUrl;
    private final Logger LOG = Logger.getLogger(CarServiceImpl.class);
    @Override
    public CarDto getOneCar(String carId) throws RestApiException {
        WebTarget msURL = restClient.target(dbServiceUrl + "/car/" + carId);
        Response response = msURL.request().get();
        if (response.getStatus()!= HttpResponseStatus.OK.code())
        {
            throw new RestApiException("Failed to get the car object of car id=" + carId , response.getStatus(),formatOriginalMessage(response));
        }

        return response.readEntity(CarDto.class);
    }

    @Override
    public List<CarDto> getAllCars() throws RestApiException {
        WebTarget msURL = restClient.target(dbServiceUrl + "/car");
        Response response = msURL.request().get();
        if (response.getStatus()!= HttpResponseStatus.OK.code())
        {
            throw new RestApiException("Failed to get list of cars",response.getStatus(), formatOriginalMessage(response));
        }
        List<CarDto> cars = response.readEntity(List.class);
        return cars;
    }
    @Override
    public void createCar(CarDto car) throws RestApiException {
        WebTarget msURL = restClient.target(dbServiceUrl + "/car");
        Response response = msURL.request().post(Entity.json(car));
        if (response.getStatus()!= HttpResponseStatus.CREATED.code())
        {
            throw new RestApiException("Failed to create car, kindly checks logs or turn to system admin for further details",response.getStatus(),formatOriginalMessage(response));
        }
        else {
            int lastSlash = response.getHeaderString("Location").lastIndexOf("/");
            car.setId(response.getHeaderString("Location").substring(lastSlash + 1));
        }

    }

    @Override
    public void updateCar(CarDto car) throws RestApiException {
        WebTarget msURL = restClient.target(dbServiceUrl + "/car");
        Response response = msURL.request().put(Entity.json(car));
        if (response.getStatus()!= HttpResponseStatus.OK.code())
        {
            throw new RestApiException("Failed to update car, kindly checks logs or turn to system admin for further details",response.getStatus(),formatOriginalMessage(response));
        }

    }

    @Override
    public void deleteCar(String carId) throws RestApiException {
        WebTarget msURL = restClient.target(dbServiceUrl + "/car/" + carId);
        Response response = msURL.request().delete();
        if (response.getStatus()!= HttpResponseStatus.OK.code() && response.getStatus()!= HttpResponseStatus.NO_CONTENT.code())
        {
            throw new RestApiException("Failed to delete car, kindly checks logs or turn to system admin for further details",response.getStatus(),formatOriginalMessage(response));
        }

    }

    private String formatOriginalMessage(Response response) {
        return response.getEntity() == null ? "" : readResponse(response);
    }

    private String readResponse(Response resp) {

        String result="";
        result = resp.readEntity(String.class);
        return result;
    }


}
