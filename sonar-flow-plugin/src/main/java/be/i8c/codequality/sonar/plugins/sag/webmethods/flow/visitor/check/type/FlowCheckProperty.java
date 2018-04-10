package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.type;

import java.lang.annotation.*;

import org.sonar.api.PropertyType;

@Documented
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(FlowCheckProperties.class)
public @interface FlowCheckProperty {
  String key();
  String defaultValue();
  String name();
  PropertyType type();
  String category();
  String subCategory();
  String description();
  String onQualifiers();
}
