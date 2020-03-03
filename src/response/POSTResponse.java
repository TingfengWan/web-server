
package response;

import resource.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.File;

public class POSTResponse extends Response {

  private String absolutePath;

  public POSTResponse(Resource resource) throws IOException {
    this.resource = resource;
    this.request = resource.getRequest();
    this.absolutePath = resource.absolutePath();
    this.file = new File(absolutePath);
    this.body = this.request.getBody();
    this.bodyBytes = this.body.getBytes();
  }

  public void send(OutputStream out) throws IOException {

    if(this.validFile() && this.body.equals("")) {
      this.statusCode = 200;
      this.reasonPhrase = "OK";
      out.write(this.getResponseHeaders());
      out.write(this.getResource());
      out.write(this.bodyBytes);
      out.flush();
      out.close();
    } else if(this.validFile()) {
      this.statusCode = 201;
      this.reasonPhrase = "CREATED";
      out.write(this.getResponseHeaders());
      out.write(this.getResource());
      out.flush();
      out.close();
    } else {
      this.statusCode = 404;
      this.reasonPhrase = "NOT FOUND";
      out.write(this.getResponseHeaders());
      out.flush();
      out.close();
    }
  }
}
