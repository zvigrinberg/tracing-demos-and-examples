package org.zgrinber.tracing.microservice1.api;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.zgrinber.tracing.common.dto.CarDto;
import org.zgrinber.tracing.common.exceptions.RestApiException;
import org.zgrinber.tracing.common.service.CarService;
import java.util.List;

@ApplicationScoped
@Path("/car")
public class CarApi {

    @Inject
    private CarService carService;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public CarDto getOneCar(@PathParam("id") String id) throws RestApiException {
        return carService.getOneCar(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CarDto> getAllCars() throws RestApiException {
        return carService.getAllCars();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCar(@RequestBody CarDto car) throws RestApiException {
       carService.createCar(car);
       return Response.status(201).build();
    }
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCar(@RequestBody CarDto car) throws RestApiException {
       carService.updateCar(car);
        return Response.status(200).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCar(@PathParam("id") String id) throws RestApiException {
       carService.deleteCar(id);
        return Response.status(204).build();
    }

}
