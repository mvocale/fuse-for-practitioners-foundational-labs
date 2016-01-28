package org.fuse.usecase.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.globex.Account;

@Path("/customerservice/")
public interface CustomerRest {

    @POST
    @Path("/enrich")
    @Consumes("application/json")
    Account enrich(Account customer);

}
