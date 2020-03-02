
package response;

import resource.*;
import java.io.IOException;
import java.io.OutputStream;

public class NOTMODresponse extends Response {

  public NOTMODresponse(Resource resource) throws IOException {
    this.resource = resource;
    this.request = resource.getRequest();
    this.statusCode = 304;
    this.reasonPhrase = "NOT MODIFIED";
  }

  public void send(OutputStream out) throws IOException {
    out.write(this.getResponseHeaders());
    out.flush();
    out.close();
  }
}
