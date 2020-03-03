
package logger;

import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

import server.WebServer;
import response.*;
import request.*;

public class Logger {

  private String logFile;

  public Logger() {
    this.logFile = WebServer.httpdConf.lookUp("LogFile", "HTTPD_CONF");
  }

  public void log(Request request, Response response, String username)
    throws IOException {
      FileWriter fileWriter = new FileWriter(this.logFile, true);
      PrintWriter printWriter = new PrintWriter(fileWriter);

      printWriter.printf("%s %s %s [%s] \"%s %s %s\" %s %s\r\n",
        request.getInetAddress(), "-", username,
        this.getDateTime(ZonedDateTime.now()),
        request.getMethod(), request.getIdentifier(), request.getVersion(),
        response.getStatusCode(), response.getByteLength());
      
      System.out.printf("%s %s %s [%s] \"%s %s %s\" %s %s\r\n",
    	        request.getInetAddress(), "-", username,
    	        this.getDateTime(ZonedDateTime.now()),
    	        request.getMethod(), request.getIdentifier(), request.getVersion(),
    	        response.getStatusCode(), response.getByteLength());
      printWriter.close();
  }

  private String getDateTime(ZonedDateTime timeStamp) {
    return String.format("%02d/%s/%04d:%02d:%02d:%02d %s",
    timeStamp.getDayOfMonth(),
    timeStamp.getMonth().getDisplayName(TextStyle.SHORT,Locale.ENGLISH),
    timeStamp.getYear(), timeStamp.getHour(), timeStamp.getMinute(),
    timeStamp.getSecond(), timeStamp.getOffset());
  }

}
