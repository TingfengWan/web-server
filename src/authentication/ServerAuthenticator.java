
package authentication;

import java.util.Base64;
import java.nio.charset.Charset;
import java.security.MessageDigest;

import configuration.*;

public class ServerAuthenticator {

  Config htpasswd, htaccess;

  public ServerAuthenticator(String htaccessPath) {
    this.htaccess = new HTAccess(htaccessPath);

    String htpasswdPath = this.htaccess.lookUp("AuthUserFile", "HTACCESS");
    this.htpasswd = new HTPasswd(htpasswdPath);
  }

  public boolean isAuthorized(String authInfo) {
    String credentials = decodeAuthInfo(authInfo);
    String[] tokens = credentials.split(":");

    if (tokens.length == 2) {
      return verifyPassword(tokens[0], tokens[1]);
    }

    return false;
  }

  private boolean verifyPassword(String username, String password) {
    String givenPassword = encryptClearPassword(password);
    String storedPassword;

    try {
      storedPassword = htpasswd.lookUp(username, "PASSWORD");
      if (givenPassword.equals(storedPassword)) {
        return true;
      }
    } catch (NullPointerException e) {
      return false;
    }


    return false;
  }

  private String encryptClearPassword(String password) {
    try {
      MessageDigest mDigest = MessageDigest.getInstance("SHA-1");
      byte[] result = mDigest.digest(password.getBytes());
      return Base64.getEncoder().encodeToString( result );
    } catch(Exception e) {
      return "";
    }
  }

  public String getUsername(String authInfo) {
    String credentials = decodeAuthInfo(authInfo);
    String[] tokens = credentials.split(":");

    if (tokens.length > 0) {
      return tokens[0];
    }

    return "-";
  }

  private String decodeAuthInfo(String authInfo) {
    String[] tokens = authInfo.split(" ");
    if (tokens[0].compareToIgnoreCase("BASIC") == 0) {
      String credentials = new String(
        Base64.getDecoder().decode(tokens[1]),
        Charset.forName("UTF-8")
      );
      return credentials;
    }

    return authInfo;
  }
}
