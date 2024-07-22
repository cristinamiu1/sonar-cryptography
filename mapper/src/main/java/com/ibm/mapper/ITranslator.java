/*
 * SonarQube Cryptography Plugin
 * Copyright (C) 2024 IBM
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ibm.mapper;

import com.ibm.engine.detection.DetectionStore;
import com.ibm.engine.model.IValue;
import com.ibm.engine.model.context.IDetectionContext;
import com.ibm.mapper.model.INode;
import com.ibm.mapper.utils.DetectionLocation;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class ITranslator<R, T, S, P> {

    public static final String UNKNOWN = "unknown";

    @Nonnull
    protected abstract List<INode> translate(
            @Nonnull DetectionStore<R, T, S, P> rootDetectionStore);

    @Nonnull
    protected abstract Optional<INode> translate(
            @Nonnull IValue<T> value,
            @Nonnull IDetectionContext detectionValueContext,
            @Nonnull final String filePath);

    @Nullable protected abstract DetectionLocation getDetectionContextFrom(
            @Nonnull T location, @Nonnull String filePath);
}
