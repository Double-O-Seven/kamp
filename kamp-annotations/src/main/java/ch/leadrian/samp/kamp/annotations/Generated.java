package ch.leadrian.samp.kamp.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Copy of javax.annotation.Generated to keep class file retention.
 */
@Documented
@Retention(CLASS)
@Target({
        PACKAGE,
        TYPE,
        ANNOTATION_TYPE,
        METHOD,
        CONSTRUCTOR,
        FIELD,
        LOCAL_VARIABLE,
        PARAMETER
})
public @interface Generated {

    String[] value();

    String date() default "";

    String comments() default "";
}