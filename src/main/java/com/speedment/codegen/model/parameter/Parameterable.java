package com.speedment.codegen.model.parameter;

import com.speedment.codegen.model.field.parameter.Parameter_;
import java.util.List;

/**
 *
 * @author pemi
 */
public interface Parameterable {

    Parameterable add(Parameter_ field_);

    List<Parameter_> getParameters();

    boolean hasParameter(Parameter_ parameter);

}