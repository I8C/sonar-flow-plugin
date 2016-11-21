package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.utils;

import java.io.File;

public class FlowUtils {

	public static String getQualifiedName(File f){
		return f.getAbsolutePath().replaceAll("(.*[/\\\\]ns[/\\\\])(.*)", "$2").replaceAll("[/\\\\]", ".").replaceAll("(.*)\\.(.*)\\.(.*)\\.(.*)", "$1:$2");
	}
}
