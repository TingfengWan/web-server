
package worker;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.ZoneId;

import request.*;
import resource.*;
import response.*;
import authentication.*;
import logger.*;

public class ClientWorker implements Runnable {

  private Socket clientSocket;
  private Logger logger;

  private Request request;
  private Resource resource;
  private Response response;
  private String username;

  public ClientWorker(Socket clientSocket) {
    this.clientSocket = clientSocket;
    this.logger = new Logger();
  }

  public void run() {
    clearFields();

    try {
      talkToClient();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void talkToClient() throws IOException {
    request = new Request(clientSocket);
    resource = new Resource(request);

    if (request.isBadRequest()) {
      response = new BadRequestResponse(resource);
      this.sendResponse(response);
      return;
    }

    if (request.isPopulated()) {
      if (resource.isProtected()) {
        String authInfo = request.getHeader("Authorization");

        if (authInfo != Request.KEY_NOT_FOUND) {
          String accessPath = resource.getHtaccessPath();
          Authenticator authenticator = new Authenticator(accessPath);

          username = authenticator.getUsername(authInfo);

          if (!authenticator.isAuthorized(authInfo)) {
            response = new ForbiddenResponse(resource);
            this.sendResponse(response);
            return;
          }

        } else {
          response = new UnauthorizedResponse(resource);
          this.sendResponse(response);
          return;
        }
      }

      String ims = request.getHeader("If-Modified-Since");
      if (ims != Request.KEY_NOT_FOUND) {
        LocalDateTime lastModified = resource.getLastModified().toLocalDateTime();
        LocalDateTime imsDateTime = this.parseIMS(ims).toLocalDateTime();
        if (imsDateTime.isAfter(lastModified)) {
          response = new NOTMODresponse(resource);
          this.sendResponse(response);
          return;
        }
      }
      if(resource.isScripted()){
        System.out.println(resource.absolutePath());
        String s;
        try {  
          Process p = Runtime.getRuntime().exec(resource.absolutePath());
          BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
          BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
          // read the output from the command
          System.out.println("Standard Out:\n");
          while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
          }
                
          // read any errors from the attempted command
          System.out.println("Standard Error:\n");
          while ((s = stdError.readLine()) != null) {
            System.out.println(s);
          }
          response = new HEADResponse(resource);
          this.sendResponse(response);
        }
        catch (IOException e) {
          response = new ServerErrorResponse(resource);
          this.sendResponse(response);
          System.out.println("Exceptions: ");
          e.printStackTrace();
          System.exit(-1);
        }
      }
      response = new GETResponse(resource);
      this.sendResponse(response);
    }
  }

  private ZonedDateTime parseIMS(String ims) {
    String tokens[] = ims.split(" ");
    if (tokens.length != 6) {
      System.out.println("tokens " + tokens.length);
      return ZonedDateTime.now();
    }

    tokens[0] = tokens[0].replace(",", "").trim();
    String hourMinSec[] = tokens[4].split(":");
    if (hourMinSec.length != 3) {
      System.out.println("hms " + hourMinSec.length);
      return ZonedDateTime.now();
    }

    int month = this.switchMonth(tokens[2].toUpperCase());
    return ZonedDateTime.of(Integer.parseInt(tokens[3]), month,
     Integer.parseInt(tokens[1]), Integer.parseInt(hourMinSec[0]),
     Integer.parseInt(hourMinSec[1]), Integer.parseInt(hourMinSec[2]),
     0, ZoneId.of(tokens[5]));
  }

  private void clearFields() {
    this.request  = null;
    this.resource = null;
    this.response = null;
    this.username = "UNKNOWN_USER";
  }

  private void sendResponse(Response response) throws IOException {
    response.send(clientSocket.getOutputStream());
    logger.log(request, response, username);
    closeConnection();
  }

  private void closeConnection() throws IOException {
    clientSocket.close();
    printConnectionClosed();
  }

  private void printConnectionClosed() {
    final String HR = "-----------------";
    System.out.printf("%17s%25s%17s\n", HR, "    Connection Closed    ", HR);
  }

  private int switchMonth(String monthShort) {
    int monthInt;

    switch (monthShort) {
        case "JAN":  monthInt = 1;
                 break;
        case "FEB":  monthInt = 2;
                 break;
        case "MAR":  monthInt = 3;
                 break;
        case "APR":  monthInt = 4;
                 break;
        case "MAY":  monthInt = 5;
                 break;
        case "JUN":  monthInt = 6;
                 break;
        case "JUL":  monthInt = 7;
                 break;
        case "AUG":  monthInt = 8;
                 break;
        case "SEP":  monthInt = 9;
                 break;
        case "OCT": monthInt = 10;
                 break;
        case "NOV": monthInt = 11;
                 break;
        case "DEC": monthInt = 12;
                 break;
        default: monthInt = 0;
                 break;
    }

    return monthInt;
  }

}
