/**
 *
 * Copyright (c) 2006-2016, Speedment, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); You may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.speedment.internal.core.code.lifecycle;

import com.speedment.Speedment;
import com.speedment.codegen.Generator;
import com.speedment.codegen.model.Class;
import com.speedment.codegen.model.Field;
import com.speedment.codegen.model.File;
import com.speedment.codegen.model.Initializer;
import com.speedment.codegen.model.Javadoc;
import com.speedment.codegen.model.Method;
import com.speedment.codegen.model.Type;
import com.speedment.config.db.Project;
import com.speedment.internal.codegen.model.JavadocImpl;
import static com.speedment.internal.codegen.model.constant.DefaultAnnotationUsage.OVERRIDE;
import static com.speedment.internal.codegen.model.constant.DefaultJavadocTag.AUTHOR;
import static com.speedment.internal.codegen.model.constant.DefaultType.STRING;
import com.speedment.internal.codegen.model.value.ReferenceValue;
import com.speedment.internal.core.code.DefaultJavaClassTranslator;
import com.speedment.internal.core.runtime.ApplicationMetadata;
import com.speedment.internal.util.document.DocumentTranscoder;
import static java.util.Objects.requireNonNull;
import java.util.stream.Stream;

/**
 *
 * @author pemi
 */
public final class GeneratedSpeedmentApplicationMetadataTranslator extends DefaultJavaClassTranslator<Project, Class> {

    public static final String METADATA = "Metadata";

    private final String className = "Generated" + getSupport().typeName(getSupport().projectOrThrow()) + "Application" + METADATA;

    public GeneratedSpeedmentApplicationMetadataTranslator(Speedment speedment, Generator gen, Project doc) {
        super(speedment, gen, doc, Class::of);
    }

    @Override
    public boolean isInGeneratedPackage() {
        return true;
    }

    @Override
    protected Class makeCodeGenModel(File file) {
        requireNonNull(file);
        final Method getMetadata = Method.of("getMetadata", STRING)
            .public_()
            .add(OVERRIDE);
        
        final Field metadataField = Field.of("METADATA", Type.of(StringBuilder.class))
            .private_().final_().static_();
        
        final Initializer initializer = Initializer.of().static_();
        
        Stream.of(DocumentTranscoder.save(getSupport().projectOrThrow()).split("\\R"))
            .forEachOrdered(l -> {
                initializer.add("METADATA.append(\"" + 
                    l.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n") + 
                    "\\n\");"
                );
            });
        
        metadataField.set(new ReferenceValue("new StringBuilder()"));
        getMetadata.add("return METADATA.toString();");
        
        return newBuilder(file, className).build()
            .public_()
            .add(Type.of(ApplicationMetadata.class))
            .add(metadataField)
            .add(initializer)
            .add(getMetadata);
    }

    @Override
    protected Javadoc getJavaDoc() {
        final String owner = getSpeedment().getUserInterfaceComponent().getBrand().title();
        return new JavadocImpl(getJavadocRepresentText() + GENERATED_JAVADOC_MESSAGE)
            .add(AUTHOR.setValue(owner));
    }

    @Override
    protected String getJavadocRepresentText() {
        return "A {@link " + ApplicationMetadata.class.getName() + 
            "} class for the {@link " + Project.class.getName() + 
            "} named " + getSupport().projectOrThrow().getName() + "."
            + " This class contains the meta data present at code generation time.";
    }

    @Override
    protected String getClassOrInterfaceName() {
        return className;
    }
}