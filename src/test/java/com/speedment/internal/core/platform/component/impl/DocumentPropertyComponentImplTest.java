/*
 * Copyright 2016 Speedment, Inc..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.speedment.internal.core.platform.component.impl;

import com.speedment.Speedment;
import com.speedment.component.Component;
import com.speedment.component.DocumentPropertyComponent;
import com.speedment.config.db.Dbms;
import com.speedment.config.db.Project;
import com.speedment.config.db.Schema;
import com.speedment.internal.core.config.db.mutator.DbmsMutator;
import com.speedment.internal.core.platform.SpeedmentFactory;
import com.speedment.internal.core.runtime.DefaultSpeedmentApplicationLifecycle;
import com.speedment.internal.ui.config.AbstractChildDocumentProperty;
import com.speedment.internal.ui.config.ColumnProperty;
import com.speedment.internal.ui.config.DbmsProperty;
import com.speedment.internal.ui.config.DocumentProperty;
import com.speedment.internal.ui.config.ForeignKeyColumnProperty;
import com.speedment.internal.ui.config.ForeignKeyProperty;
import com.speedment.internal.ui.config.IndexColumnProperty;
import com.speedment.internal.ui.config.IndexProperty;
import com.speedment.internal.ui.config.PrimaryKeyColumnProperty;
import com.speedment.internal.ui.config.ProjectProperty;
import com.speedment.internal.ui.config.SchemaProperty;
import com.speedment.internal.ui.config.TableProperty;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import org.controlsfx.control.PropertySheet;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Emil Forslund
 */
public class DocumentPropertyComponentImplTest {
    
    private Speedment speedment;
    private DocumentPropertyComponent componenet;
    
    @Before
    public void setUp() {
        speedment = new DefaultSpeedmentApplicationLifecycle().build();
        componenet = speedment.getDocumentPropertyComponent();
    }

    @Test
    public void testDefaultInstallments() {

        final DocumentProperty project = componenet.getConstructor(DocumentPropertyComponent.PROJECTS).create(null);
        final DocumentProperty dbms = componenet.getConstructor(DocumentPropertyComponent.DBMSES).create(project);
        final DocumentProperty schema = componenet.getConstructor(DocumentPropertyComponent.SCHEMAS).create(dbms);
        final DocumentProperty table = componenet.getConstructor(DocumentPropertyComponent.TABLES).create(schema);
        final DocumentProperty column = componenet.getConstructor(DocumentPropertyComponent.COLUMNS).create(table);
        final DocumentProperty index = componenet.getConstructor(DocumentPropertyComponent.INDEXES).create(table);
        final DocumentProperty indexColumn = componenet.getConstructor(DocumentPropertyComponent.INDEX_COLUMNS).create(index);
        final DocumentProperty foreignKey = componenet.getConstructor(DocumentPropertyComponent.FOREIGN_KEYS).create(table);
        final DocumentProperty foreignKeyColumn = componenet.getConstructor(DocumentPropertyComponent.FOREIGN_KEY_COLUMNS).create(foreignKey);
        final DocumentProperty primaryKey = componenet.getConstructor(DocumentPropertyComponent.PRIMARY_KEY_COLUMNS).create(table);
        
        assertEquals("Make sure ProjectProperty is used by default: ", ProjectProperty.class, project.getClass());
        assertEquals("Make sure DbmsProperty is used by default: ", DbmsProperty.class, dbms.getClass());
        assertEquals("Make sure SchemaProperty is used by default: ", SchemaProperty.class, schema.getClass());
        assertEquals("Make sure TableProperty is used by default: ", TableProperty.class, table.getClass());
        assertEquals("Make sure ColumnProperty is used by default: ", ColumnProperty.class, column.getClass());
        assertEquals("Make sure IndexProperty is used by default: ", IndexProperty.class, index.getClass());
        assertEquals("Make sure IndexColumnProperty is used by default: ", IndexColumnProperty.class, indexColumn.getClass());
        assertEquals("Make sure ForeignKeyProperty is used by default: ", ForeignKeyProperty.class, foreignKey.getClass());
        assertEquals("Make sure ForeignKeyColumnProperty is used by default: ", ForeignKeyColumnProperty.class, foreignKeyColumn.getClass());
        assertEquals("Make sure PrimaryKeyColumnProperty is used by default: ", PrimaryKeyColumnProperty.class, primaryKey.getClass());
    }
    
    @Test
    public void testAlternateInstallments() {
        componenet.setConstructor(parent -> new AlternativeDbms((Project) parent), DocumentPropertyComponent.DBMSES);
        
        final DocumentProperty project = componenet.getConstructor(DocumentPropertyComponent.PROJECTS).create(null);
        final DocumentProperty dbms = componenet.getConstructor(DocumentPropertyComponent.DBMSES).create(project);
        
        assertEquals(ProjectProperty.class, project.getClass());
        assertEquals(AlternativeDbms.class, dbms.getClass());
    }
    
    private final static class AlternativeDbms extends AbstractChildDocumentProperty<Project, AlternativeDbms> implements Dbms {

        public AlternativeDbms(Project parent) {
            super(parent);
        }
        
        @Override
        protected String[] keyPathEndingWith(String key) {
            return DocumentPropertyComponent.concat(DocumentPropertyComponent.DBMSES, key);
        }

        @Override
        public Stream<PropertySheet.Item> getUiVisibleProperties(Speedment speedment) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public BiFunction<Dbms, Map<String, Object>, ? extends Schema> schemaConstructor() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public DbmsMutator mutator() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
}