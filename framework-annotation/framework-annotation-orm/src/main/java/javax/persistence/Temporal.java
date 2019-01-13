package javax.persistence;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static javax.persistence.TemporalType.TIMESTAMP;

@Target({METHOD, FIELD}) 
@Retention(RUNTIME)
public @interface Temporal {

    TemporalType value();
}
