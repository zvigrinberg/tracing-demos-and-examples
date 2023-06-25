package org.zgrinber.tracing.microservice2.api;


import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.jboss.resteasy.reactive.ResponseStatus;
import org.zgrinber.tracing.common.dto.CarDto;
import org.zgrinber.tracing.common.exceptions.RestApiException;
import org.zgrinber.tracing.common.service.CarService;

import java.util.List;

@Path("/car")
public class CarApi {

    @Inject
    private CarService carService;

    @GET
    @ResponseStatus(200)
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public CarDto getOneCar(@PathParam("id") String id) throws RestApiException {
        return carService.getOneCar(id);
    }

    @GET
    @ResponseStatus(200)
    @Produces(MediaType.APPLICATION_JSON)
    public List<CarDto> getAllCars() throws RestApiException {
        return carService.getAllCars();
    }

    @POST
    @ResponseStatus(201)
    @Produces(MediaType.APPLICATION_JSON)
    public void createCar(@RequestBody CarDto car) throws RestApiException {
       carService.createCar(car);
    }
    @PUT
    @ResponseStatus(200)
    @Produces(MediaType.APPLICATION_JSON)
    public void updateCar(@RequestBody CarDto car) throws RestApiException {
       carService.updateCar(car);
    }

    @DELETE
    @Path("/{id}")
    @ResponseStatus(204)
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteCar(@PathParam("id") String id) throws RestApiException {
       carService.deleteCar(id);
    }

}
