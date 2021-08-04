package org.clerezza.gsoc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.clerezza.*;
import org.apache.clerezza.implementation.TripleImpl;
import org.apache.clerezza.implementation.in_memory.SimpleGraph;
import org.apache.clerezza.implementation.literal.PlainLiteralImpl;
import org.apache.clerezza.representation.Parser;
import org.apache.clerezza.representation.Serializer;
import org.apache.clerezza.representation.SupportedFormat;
import org.apache.clerezza.representation.UnsupportedFormatException;
import org.clerezza.gsoc.model.FOAF;
import org.clerezza.gsoc.model.RDF;
import org.clerezza.gsoc.model.SIGNAL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.*;

@Path( "/signal/messages" )
@Produces( MediaType.APPLICATION_JSON )
@Consumes( MediaType.APPLICATION_JSON )
public class SignalResource {

    private static final Logger logger = LoggerFactory.getLogger( SignalResource.class );

    //TODO: Write JavaDocs
    //TODO: Write Tests
    //TODO: Divide into methods
    //TODO: Use from library

//    BlankNode conversationNode = new BlankNode();
//    BlankNode personNode = new BlankNode();

    BlankNode messageNode = new BlankNode();
    Graph incomingGraph;

    @POST
    public void saveMessage( String data ) throws IOException {
        store( data );
    }

    private void store( String data ) throws IOException {

        createFileIfNotExist();

        //read message from file
        InputStream inputStream = SignalResource.class.getClassLoader().getResourceAsStream( "signal.ttl" );

        var json = new ObjectMapper().readValue( data, ObjectNode.class );
        //saveToGraph( json );
        Graph mergedGraph = new SimpleGraph();
        BlankNodeOrIRI pNode = null;
        BlankNodeOrIRI cNode = null;

        var username = new PlainLiteralImpl( json.get( "Person" ).get( "username" ).textValue() );
        var conversationName = new PlainLiteralImpl( json.get( "Conversation" ).get( "conversationName" ).textValue() );

        if ( inputStream.available() > 1 ) {
            //for existingGraph
            ImmutableGraph existingGraph = parseGraph( inputStream ); //file contains existing messages.

            //var incomingPObject = json.get( "Person" ).get( "username" ).textValue();
            if ( existingGraph != null ) {
                mergedGraph.addAll( existingGraph );
            }
            var iterator = mergedGraph.filter( null, RDF.type, FOAF.Person );
            Triple triple;
            while ( iterator.hasNext() ) {
                triple = iterator.next();
                var pIterator = mergedGraph.filter( triple.getSubject(), SIGNAL.username, username );
                if ( pIterator.hasNext() ) {
                    pNode = triple.getSubject();
                    break;
                }
            }
            iterator = mergedGraph.filter( null, RDF.type, SIGNAL.Conversation );
            while ( iterator.hasNext() ) {
                triple = iterator.next();
                var cIterator = mergedGraph.filter( triple.getSubject(), SIGNAL.conversationName, conversationName );
                if ( cIterator.hasNext() ) {
                    cNode = triple.getSubject();
                    break;
                }
            }
            //mergedGraph.addAll( existingGraph ); //add new data to existingGraph
        }
        if ( pNode == null ) {
            pNode = new BlankNode();
            mergedGraph.add( new TripleImpl( pNode, RDF.type, FOAF.Person ) );
            mergedGraph.add( new TripleImpl( pNode, SIGNAL.username, username ) );
        }
        if ( cNode == null ) {
            cNode = new BlankNode();
            mergedGraph.add( new TripleImpl( cNode, RDF.type, SIGNAL.Conversation ) );
            mergedGraph.add( new TripleImpl( cNode, SIGNAL.conversationName, conversationName ) );

        }
        var messageNode = new BlankNode();
        //var pObjectString = pObject.toString().replace( "\"", "" );
        mergedGraph.add( new TripleImpl( messageNode, RDF.type, SIGNAL.Message ) );
        mergedGraph.add( new TripleImpl( messageNode, SIGNAL.text, new PlainLiteralImpl( json.get( "Message" ).get( "text" ).textValue() ) ) );
        mergedGraph.add( new TripleImpl( messageNode, SIGNAL.timeStamp, new PlainLiteralImpl( json.get( "Message" ).get( "timestamp" ).textValue() ) ) );
        mergedGraph.add( new TripleImpl( pNode, SIGNAL.post, messageNode ) );
        mergedGraph.add( new TripleImpl( cNode, SIGNAL.consistOf, messageNode ) );

        saveGraph( mergedGraph );
        //incomingGraph.clear();
    }

    private void createFileIfNotExist() throws IOException {
        File signalFile = new File( "C:/Users/yusufkaradag2/Desktop/clerezza-gsoc/src/main/resources/signal.ttl" );
        if ( signalFile.createNewFile() ) {
            logger.info( "File has been created." );// if file already exists will do nothing
        } else {
            logger.info( "File already exists." );
        }
    }

    /**
     * @param inputStream
     * @return null or {@link ImmutableGraph}
     */
    private ImmutableGraph parseGraph( InputStream inputStream ) {
        // parse the graph using Jena parser into simple graph
        ImmutableGraph graph = null;
        Parser parser = Parser.getInstance();
        //parser.bindParsingProvider( new JenaParserProvider() );
        try {
            graph = parser.parse( inputStream, SupportedFormat.TURTLE );
        } catch ( UnsupportedFormatException ex ) {
            logger.warn( String.format( "%s is not supported by the used parser", SupportedFormat.TURTLE ) );
            logger.error( ex.getMessage() );
        }
        return graph;
    }

    private void saveGraph( Graph graph ) {
        //Serialize the graph into file
        Serializer serializer = Serializer.getInstance();
        //serializer.bindSerializingProvider( new JenaSerializerProvider() );

        try {
            FileOutputStream outputStream = new FileOutputStream( "C:/Users/yusufkaradag2/Desktop/clerezza-gsoc/src/main/resources/signal.ttl" );
            serializer.serialize( outputStream, graph, SupportedFormat.TURTLE );
        } catch ( FileNotFoundException ex ) {
            logger.warn( ex.getMessage() );
        } catch ( UnsupportedFormatException ex ) {
            logger.warn( String.format( "%s is not supported by the used serializer", SupportedFormat.TURTLE ) );
        }
    }

//    private void saveToGraph( ObjectNode json ) {
//
//        incomingGraph = new SimpleGraph();
//        // Personal conversation
//        // 1. BlankNode1 -> rdfType -> foaf.person
//        // 2. BlankNode1 -> SIGNAL.username -> plainLiteral "hasan"
//        // 3. BlankNode1 -> SIGNAL.post -> BlankNode2
//        TripleImpl personNodeType = new TripleImpl( personNode, RDF.type, FOAF.Person );
//        TripleImpl personNodeUserName = new TripleImpl( personNode, SIGNAL.username, new PlainLiteralImpl( json.get( "Person" ).get( "username" ).textValue() ) );
//
//        // 4. BlankNode2 -> rdf.type -> SIGNAL.Message
//        // 5. BlankNode2 -> SIGNAL.timestamp -> plainLiteral "121233442"
//        // 6. BlankNode2 -> SIGNAL.text -> plainLiteral "Hello"
//        TripleImpl messageNodeType = new TripleImpl( messageNode, RDF.type, SIGNAL.Message );
//        TripleImpl messageNodeTimestamp = new TripleImpl( messageNode, SIGNAL.timeStamp, new PlainLiteralImpl( json.get( "Message" ).get( "timestamp" ).textValue() ) );
//        TripleImpl messageNodeText = new TripleImpl( messageNode, SIGNAL.text, new PlainLiteralImpl( json.get( "Message" ).get( "text" ).textValue() ) );
//
//        TripleImpl personNodePost = new TripleImpl( personNode, SIGNAL.post, messageNode );
//        // 7. BlankNode3 -> rdf.type -> SIGNAL.Conversation
//        // 8. BlankNode3 -> SIGNAL.name -> plainLiteral "Yusuf-Hasan Conv."
//        // 9. BlankNode3 -> SIGNAL.consistOf -> BlankNode2
//        TripleImpl conversationNodeType = new TripleImpl( conversationNode, RDF.type, SIGNAL.Conversation );
//        TripleImpl conversationNodeName = new TripleImpl( conversationNode, SIGNAL.conversationName, new PlainLiteralImpl( json.get( "Conversation" ).get( "conversationName" ).textValue() ) );
//        TripleImpl conversationNodeConsistOf = new TripleImpl( conversationNode, SIGNAL.consistOf, messageNode );
//
//        //add all triples to incomingGraph
//        Collections.addAll( incomingGraph, personNodeType, personNodeUserName, personNodePost, messageNodeType, messageNodeTimestamp, messageNodeText, conversationNodeType, conversationNodeName, conversationNodeConsistOf );
//
//    }

}