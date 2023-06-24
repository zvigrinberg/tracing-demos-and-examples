package org.zgrinber.tracing.microservice1.beans;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;

@ApplicationScoped
public class SharedBeans {


@Produces
@ApplicationScoped
public Client restClient()
{
    return ClientBuilder.newClient();
}


}
