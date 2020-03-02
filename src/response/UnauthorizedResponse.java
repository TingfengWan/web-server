
package response;

import resource.*;
import java.io.IOException;

public class UnauthorizedResponse extends Response {

  public UnauthorizedResponse(Resource resource) throws IOException {
    this.resource = resource;
    this.request = resource.getRequest();
    this.statusCode = 401;
    this.reasonPhrase = "UNAUTHORIZED";
  }
}
