
package response;

import resource.*;
import java.io.IOException;

public class ServerErrorResponse extends Response {

  public ServerErrorResponse(Resource resource) throws IOException {
    this.resource = resource;
    this.request = resource.getRequest();
    this.statusCode = 500;
    this.reasonPhrase = "INTERNAL SERVER ERROR";
  }

}