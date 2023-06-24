package org.zgrinber.tracing.microservice1.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.zgrinber.tracing.common.exceptions.RestApiException;
import org.jboss.logging.Logger;

@Provider
public class RestApiExceptionHandler implements ExceptionMapper<RestApiException> {
    private final Logger LOG = Logger.getLogger(RestApiExceptionHandler.class);
    @Override
    public Response toResponse(RestApiException exception) {
//        return Response.status(Response.Status.BAD_REQUEST).entity(exception.getMessage()).build();
        LOG.errorf("Got an error from rest service, original message = %s , http status= %s",exception.getOriginalMessage(),exception.getHttpStatusCode().toString());
        return Response.status(exception.getHttpStatusCode()).entity(exception.getMessage()).build();

    }
}
