
package response;

import resource.*;
import java.io.IOException;

public class BadRequestResponse extends Response {

  public BadRequestResponse(Resource resource) throws IOException {
    this.resource = resource;
    this.request = resource.getRequest();
    this.statusCode = 400;
    this.reasonPhrase = "BAD REQUEST";
  }

}
