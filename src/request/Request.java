
package request;

import java.util.HashMap;
import java.util.Map;
import java.net.Socket;
import java.io.IOException;
import java.net.InetAddress;

public class Request {

  private String method     = "No Method Received";
  private String identifier = "No Identifier Received";
  private String version    = "No Version Received";
  private HashMap<String, String> headers;
  private String body = null;
  private InetAddress inetAddress;

  private boolean badRequest = false;

  public Request(Socket client) throws IOException {
    headers = new HashMap<String, String>();

    RequestParser requestParser = new RequestParser(client, this);
    requestParser.parseHttpRequest();
    printDataFields();
  }

  private void printDataFields() {
    final String CB = ": ";
    final String HR = "- - - - - - - - - - - - -";

    if (isPopulated()) {
      System.out.printf("%-25s%-9s%25s\n", HR, " Request ", HR);
      System.out.printf("%-20s%2s%S\n", "Method", CB, method);
      System.out.printf("%-20s%2s%S\n", "Identifier", CB, identifier);
      System.out.printf("%-20s%2s%S\n", "Version", CB, version);

      if (this.headers.isEmpty()) {
        System.out.printf("%-20s%2s%S\n", "Headers", ": ", "no headers received");

      } else {
        System.out.printf("%-25s%-9s%25s\n", HR, " Headers ", HR);

        for(Map.Entry<String, String> entry : headers.entrySet()) {
          System.out.printf("%-20s%2s%S\n", entry.getKey(), CB, entry.getValue());
        }
      }

      if (this.body == null) {
        System.out.printf("%-20s%2s%S\n", "Body", ": ", "no body received");

      } else {
        System.out.printf("%-25s%-9s%25s\n", HR, " Body ", HR);
        System.out.println(body);
      }
    }
  }

  protected void setMethod(String method) {
    this.method = method;
  }

  protected void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  protected void setVersion(String version) {
    this.version = version;
  }

  protected void setBody(String body) {
    this.body = body;
  }

  protected void setInetAddress(InetAddress inetAddress) {
    this.inetAddress = inetAddress;
  }

  protected void setBadRequest(boolean badRequest) {
    this.badRequest = badRequest;
  }

  protected void putHeader(String key, String value) {
    this.headers.put(key, value);
  }

  public String getMethod() {
    return this.method;
  }

  public String getIdentifier() {
    return this.identifier;
  }

  public String getVersion() {
    return this.version;
  }

  public String getBody() {
    return this.body;
  }

  public InetAddress getInetAddress() {
    return this.inetAddress;
  }

  public String getHeader(String key) {
    return this.headers.getOrDefault(key, "KEY_NOT_FOUND");
  }

  public boolean isBadRequest() {
    return this.badRequest;
  }

  public boolean isPopulated() {

    if (this.method == "No Method Received") {
      return false;
    }
    if (this.identifier == "No Identifier Received") {
      return false;
    }
    if (this.version == "No Version Received") {
      return false;
    }

    return true;
  }

}
