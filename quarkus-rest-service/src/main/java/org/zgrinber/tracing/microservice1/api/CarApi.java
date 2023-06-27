package org.zgrinber.tracing.microservice1.api;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.jboss.logging.Logger;
import org.zgrinber.tracing.common.dto.CarDto;
import org.zgrinber.tracing.common.exceptions.RestApiException;
import org.zgrinber.tracing.common.service.CarService;

import java.net.URI;
import java.util.List;

@ApplicationScoped
@Path("/car")
public class CarApi {
    @Inject
    private CarService carService;
    private Logger LOG = Logger.getLogger(CarApi.class);

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public CarDto getOneCar(@PathParam("id") String id) throws RestApiException {
        LOG.infof("About to get car with id=%s",id);
        return carService.getOneCar(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CarDto> getAllCars() throws RestApiException {
        LOG.info("About to get all Cars");
        return carService.getAllCars();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCar(@RequestBody CarDto car,@HeaderParam("host") String host) throws RestApiException {
       LOG.infof("About to create car with the following details: %s",car.toString());
       carService.createCar(car);
        return Response.created(URI.create(String.format("http://%s/car/%s",host,car.getId()))).build();
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
        LOG.infof("About to delete car with id=%s",id);
       carService.deleteCar(id);
        return Response.status(204).build();
    }

}
