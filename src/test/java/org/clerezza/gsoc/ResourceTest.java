package org.clerezza.gsoc;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class ResourceTest {

    @Test
    public void shouldReturn201() {
        given()
                .header( "Content-Type", MediaType.APPLICATION_JSON )
                .body( "{\"Person\":{\"username\":\"testUser\"},\"Message\":{\"text\":\"Test Message\",\"timestamp\":\"1629474568\"},\"Conversation\":{\"conversationName\":\"TestConversation\"}}" )
                .when()
                .post( "/signal/messages" )
                .then()
                .statusCode( 201 );
    }

    @Test
    public void shouldReturn200() {
        given()
                .when()
                .get( "/signal/messages" )
                .then()
                .statusCode( 200 );
    }

}