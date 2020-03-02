
package response;

import resource.*;
import java.io.IOException;

public class ResponseWriter {

  public Response getResponse(Resource resource) throws IOException {
    String method = resource.getRequest().getMethod();

    switch(method) {
      case "GET":
        return new GETResponse(resource);
      case "POST":
        return new POSTResponse(resource);
      case "PUT":
        return new PUTResponse(resource);
      case "DELETE":
        return new DELETEResponse(resource);
      case "HEAD":
        return new HEADResponse(resource);
      default:
        return null;
    }
  }
}
