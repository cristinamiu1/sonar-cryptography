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
package com.ibm.mapper.model.functionality;

import com.ibm.mapper.model.INode;
import com.ibm.mapper.model.Property;
import com.ibm.mapper.utils.DetectionLocation;
import java.util.List;
import javax.annotation.Nonnull;
import org.jetbrains.annotations.Unmodifiable;

public abstract class Functionality extends Property {

    protected Functionality(
            @Nonnull Class<? extends Functionality> type,
            @Nonnull DetectionLocation detectionLocation) {
        super(type, detectionLocation);
    }

    protected Functionality(@Nonnull Functionality functionality) {
        super(functionality.type, functionality.detectionLocation, functionality.children);
    }

    @Nonnull
    @Override
    public String asString() {
        return type.getSimpleName().toUpperCase();
    }

    @Unmodifiable
    @Nonnull
    public static List<Class<? extends INode>> getKinds() {
        return List.of(
                Decrypt.class,
                Decapsulate.class,
                Digest.class,
                Encrypt.class,
                Encapsulate.class,
                Generate.class,
                KeyDerivation.class,
                KeyGeneration.class,
                Sign.class,
                Tag.class,
                Verify.class);
    }
}
