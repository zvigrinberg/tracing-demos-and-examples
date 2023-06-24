package org.zgrinber.tracing.microservice1.api;

import jakarta.annotation.Resource;
import jakarta.enterprise.inject.Typed;
import jakarta.inject.Qualifier;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.zgrinber.tracing.common.dto.CarDto;
import org.zgrinber.tracing.common.exceptions.RestApiException;
import org.zgrinber.tracing.microservice1.service.CarService;

@Path("/car")
public class CarApi {

    @Typed
    private CarService carService;

    @GET
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public CarDto getOneCar(@PathParam("id") String id) throws RestApiException {
        return carService.getOneCar(id);
    }

}
