package dmit2015.restclient;

import dmit2015.model.Student;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;
import java.util.Optional;

/**
 * The baseUri for the web MpRestClient be set in either microprofile-config.properties (recommended)
 * or in this file using @RegisterRestClient(baseUri = "http://server/path").
 * <p>
 * To set the baseUri in microprofile-config.properties:
 * 1) Open src/main/resources/META-INF/microprofile-config.properties
 * 2) Add a key/value pair in the following format:
 * package-name.ClassName/mp-rest/url=baseUri
 * For example:
 * package-name:    dmit2015.restclient
 * ClassName:       StudentDtoMpRestClient
 * baseUri:         http://localhost:8080/contextName
 * The key/value pair you need to add is:
 * <code>
 * dmit2015.restclient.StudentDtoMpRestClient/mp-rest/url=http://localhost:8080/contextName
 * </code>
 * <p>
 * To use the client interface from an environment does support CDI, add @Inject and @RestClient before the field declaration such as:
 * <code>
 *
 * @Inject
 * @RestClient private StudentDtoMpRestClient _studentdtoMpRestClient;
 * </code>
 * <p>
 * To use the client interface from an environment that does not support CDI, you can use the RestClientBuilder class to programmatically build an instance as follows:
 * <code>
 * URI apiURI = new URI("http://sever/contextName");
 * StudentDtoMpRestClient _studentdtoMpRestClient = RestClientBuilder.newBuilder().baseUri(apiURi).build(StudentDtoMpRestClient.class);
 * </code>
 */
@RequestScoped
@RegisterProvider(BadRequestResponseMapper.class)
@RegisterRestClient(baseUri = "http://localhost:8090/restapi/StudentDtos")
public interface StudentMpRestClient {

    @POST
    Response create(@HeaderParam("Authorization") String authorizationHeader,Student newStudent);

    @GET
    List<Student> findAll(@HeaderParam("Authorization") String authorizationHeader);

    @GET
    @Path("/{id}")
    Optional<Student> findById(@HeaderParam("Authorization") String authorizationHeader,@PathParam("id") Long id);

    @PUT
    @Path("/{id}")
    Student update(@HeaderParam("Authorization") String authorizationHeader,@PathParam("id") Long id, Student updatedStudent);

    @DELETE
    @Path("/{id}")
    void delete(@HeaderParam("Authorization") String authorizationHeader,@PathParam("id") Long id);

}