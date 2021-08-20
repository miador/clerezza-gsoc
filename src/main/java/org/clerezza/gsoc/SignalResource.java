package org.clerezza.gsoc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.clerezza.signal.file.FileOperations;
import org.apache.clerezza.signal.graph.SignalGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;

@Path( "/signal/messages" )
@Produces( MediaType.APPLICATION_JSON )
@Consumes( MediaType.APPLICATION_JSON )
public class SignalResource {

    private static final Logger logger = LoggerFactory.getLogger( SignalResource.class );
    public static String filename = System.getProperty( "user.dir" ) + "/signal.ttl";

    @POST
    public void saveMessage( String data ) throws IOException {
        var json = new ObjectMapper().readValue( data, ObjectNode.class );
        createFileIfNotExist( filename );
        SignalGraph.buildGraph( json, filename );

    }

    @GET
    public String printGraph() {
        return FileOperations.printGraph( filename );
    }

    /**
     *
     * @param filename, with the given filename a new file will be created if file doesn't exist.
     *                  Will do nothing if file already exists.
     */
    public static void createFileIfNotExist( String filename ) {
        try {
            File file = new File( filename );
            if ( file.createNewFile() ) {
                logger.info( "File created at: " + "\"" + filename + "\"" );
            } else {
                logger.info( "Existing file at the directory: " + "\"" + filename + "\"" + " will be overwritten by the serializer" );
            }
        } catch ( Exception e ) {
            System.out.println( e.getLocalizedMessage() );
        }
    }
}