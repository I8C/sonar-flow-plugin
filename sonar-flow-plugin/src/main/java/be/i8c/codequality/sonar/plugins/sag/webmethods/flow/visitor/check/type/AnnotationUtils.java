/**
 * 
 */
package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.type;

import java.lang.annotation.Annotation;
import java.util.List;

import org.apache.commons.lang.ClassUtils;

/**
 * @author DEWANST
 *
 */
public class AnnotationUtils {

  /**
   * 
   */
  public AnnotationUtils() {
  }

  /**
   * Get the annotation of a Class
   * 
   * @param objectOrClass
   * @param annotationClass
   * @return
   */
  @SuppressWarnings("unchecked")
  public static <A extends Annotation> A getAnnotation(Object objectOrClass,
      Class<A> annotationClass) {
    Class<?> initialClass = objectOrClass instanceof Class<?> ? (Class<?>) objectOrClass
        : objectOrClass.getClass();

    for (Class<?> aClass = initialClass; aClass != null; aClass = aClass.getSuperclass()) {
      A result = aClass.getAnnotation(annotationClass);
      if (result != null) {
        return result;
      }
    }

    for (Class<?> anInterface : (List<Class<?>>) ClassUtils.getAllInterfaces(initialClass)) {
      A result = anInterface.getAnnotation(annotationClass);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  /**
   * Get the list of annotations of a Class
   * @param <A>
   * 
   * @param objectOrClass
   * @param annotationClass
   * @return
   */
  @SuppressWarnings("unchecked")
  public static <A extends Annotation> A[] getAnnotations(Object objectOrClass,
      Class<A> annotationClass) {
    Class<?> initialClass = objectOrClass instanceof Class<?> ? (Class<?>) objectOrClass
        : objectOrClass.getClass();

    for (Class<?> aClass = initialClass; aClass != null; aClass = aClass.getSuperclass()) {
      A[] result = aClass.getAnnotationsByType(annotationClass);
      if (result != null) {
        return result;
      }
    }

    for (Class<?> anInterface : (List<Class<?>>) ClassUtils.getAllInterfaces(initialClass)) {
      A[] result = anInterface.getAnnotationsByType(annotationClass);
      if (result != null) {
        return result;
      }
    }
    return null;
  }
}
