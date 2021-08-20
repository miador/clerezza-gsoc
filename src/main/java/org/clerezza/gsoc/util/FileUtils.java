package org.clerezza.gsoc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger( FileUtils.class );

    /**
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

    /**
     * @param filename, with the given filename, will build a String and print it as graph.
     * @return {@link String} as representation of the graph.
     */
    public static String printGraph( String filename ) {

        StringBuilder sb = new StringBuilder();
        try ( BufferedReader br = new BufferedReader( new FileReader( filename ) ) ) {
            String line;
            while ( ( line = br.readLine() ) != null ) {
                sb.append( line ).append( "\n" );
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
