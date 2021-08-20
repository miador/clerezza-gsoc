package org.clerezza.gsoc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.clerezza.signal.graph.SignalGraph;
import org.clerezza.gsoc.util.FileUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;

@Path( "/signal/messages" )
@Produces( MediaType.APPLICATION_JSON )
@Consumes( MediaType.APPLICATION_JSON )
public class SignalResource {

    public static String filename = System.getProperty( "user.dir" ) + "/signal.ttl";

    @POST
    public Response saveMessage( String data, @Context UriInfo uriInfo ) throws IOException {
        var json = new ObjectMapper().readValue( data, ObjectNode.class );
        FileUtils.createFileIfNotExist( filename );
        SignalGraph.buildGraph( json, filename );

        return Response.created( uriInfo.getAbsolutePathBuilder().build() ).build();
    }

    @GET
    public String printGraph() {
        return FileUtils.printGraph( filename );
    }

}