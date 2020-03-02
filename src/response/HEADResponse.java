
package response;

import resource.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.File;

public class HEADResponse extends Response {

  private String absolutePath;

  public HEADResponse(Resource resource) throws IOException {
    this.resource = resource;
    this.request = resource.getRequest();
    this.absolutePath = resource.absolutePath();
    this.file = new File(absolutePath);

    if(this.validFile()) {
      this.statusCode = 200;
      this.reasonPhrase = "OK";
    }
    else{
      this.statusCode = 404;
      this.reasonPhrase = "NOT FOUND";
    }
  }

  public void send(OutputStream out) throws IOException {
      out.write(this.getResponseHeaders());
      out.flush();
      out.close();
  }
}
