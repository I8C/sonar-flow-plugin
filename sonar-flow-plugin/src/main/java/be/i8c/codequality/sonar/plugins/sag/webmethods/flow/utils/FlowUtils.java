package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.utils;

import java.io.File;
import java.net.URI;

public class FlowUtils {

  public static String getQualifiedName(File f) {
    return getQualifiedName(f.getAbsolutePath());
  }

  public static String getQualifiedName(URI flowURI) {
    return getQualifiedName(flowURI.getPath());
  }

  private static String getQualifiedName(String path) {
    return path.replaceAll("(.*[/\\\\]ns[/\\\\])(.*)", "$2")
        .replaceAll("[/\\\\]flow\\.xml", "")
        .replaceAll("[/\\\\]node\\.ndf", "")
        .replaceAll("[/\\\\]", ".")
        .replaceAll("(.*)\\.(.*)$", "$1:$2");
  }
}
