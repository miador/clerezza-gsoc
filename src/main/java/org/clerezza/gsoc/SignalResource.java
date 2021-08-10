package org.clerezza.gsoc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.clerezza.signal.file.FileOperations;
import org.apache.clerezza.signal.graph.SignalGraph;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path( "/signal/messages" )
@Produces( MediaType.APPLICATION_JSON )
@Consumes( MediaType.APPLICATION_JSON )
public class SignalResource {

    public static String filename = System.getProperty( "user.dir" ) + "/signal.ttl";

    @POST
    public void saveMessage( String data ) throws IOException {
        var json = new ObjectMapper().readValue( data, ObjectNode.class );
        FileOperations.createFileIfNotExist( filename );
        SignalGraph.buildGraph( json, filename );

    }

    @GET
    public String printGraph() {
        return FileOperations.printGraph( filename );
    }

}