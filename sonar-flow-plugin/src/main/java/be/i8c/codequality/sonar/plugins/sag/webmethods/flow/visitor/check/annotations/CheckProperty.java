package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.annotations;

import java.lang.annotation.*;

import org.sonar.api.PropertyType;

@Documented
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(CheckProperties.class)
public @interface CheckProperty {
  String key();
  String defaultValue();
  String name();
  PropertyType type();
  String category();
  String subCategory();
  String description();
  String onQualifiers();
}
