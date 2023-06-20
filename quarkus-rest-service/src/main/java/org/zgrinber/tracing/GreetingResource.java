package org.zgrinber.tracing;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.zgrinber.tracing.common.dto.CarDto;

@Path("/hello")
public class GreetingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        CarDto carDto;
        return "Hello from RESTEasy Reactive";
    }

}
