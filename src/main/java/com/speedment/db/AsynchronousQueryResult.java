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
package com.speedment.db;

import com.speedment.annotation.Api;
import com.speedment.stream.HasParallelStrategy;
import java.sql.ResultSet;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 *
 * @author pemi
 * @param <T> The type that the ResultSet shall be mapped to
 */
@Api(version = "2.3")
public interface AsynchronousQueryResult<T> extends HasParallelStrategy, AutoCloseable {

    Stream<T> stream();

    @Override
    void close();

    String getSql();

    void setSql(String sql);

    List<?> getValues();

    void setValues(List<?> values);

    Function<ResultSet, T> getRsMapper();

    void setRsMapper(Function<ResultSet, T> rsMapper);

}
