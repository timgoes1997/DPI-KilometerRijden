package com.github.timgoes1997.web.beans;

import javax.ejb.Stateless;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Stateless
@Path("region")
public class RegionBean {

    @POST
    @Path("create")
    public void Create(@FormParam("name") String names){

    }

}
