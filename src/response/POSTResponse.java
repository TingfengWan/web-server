
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
    this.statusCode = 200;
    this.reasonPhrase = "OK";
  }

  public void send(OutputStream out) throws IOException {

    if(this.validFile() && this.body.equals("")) {
      out.write(this.getResponseHeaders());
      out.write(this.getResource());
      out.write(this.bodyBytes);
      out.flush();
      out.close();
    } else if(this.validFile()) {
      out.write(this.getResponseHeaders());//201
      out.write(this.getResource());
      out.flush();
      out.close();
    } else {
      out.write(this.getResponseHeaders());//404
      out.flush();
      out.close();
    }
  }
}
