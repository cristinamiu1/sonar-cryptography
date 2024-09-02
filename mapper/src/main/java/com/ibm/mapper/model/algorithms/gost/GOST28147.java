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
package com.ibm.mapper.model.algorithms.gost;

import com.ibm.mapper.model.Algorithm;
import com.ibm.mapper.model.AuthenticatedEncryption;
import com.ibm.mapper.model.BlockCipher;
import com.ibm.mapper.model.BlockSize;
import com.ibm.mapper.model.IPrimitive;
import com.ibm.mapper.model.KeyLength;
import com.ibm.mapper.model.Mac;
import com.ibm.mapper.model.Mode;
import com.ibm.mapper.utils.DetectionLocation;
import javax.annotation.Nonnull;
import org.jetbrains.annotations.NotNull;

public final class GOST28147 extends Algorithm
        implements BlockCipher, AuthenticatedEncryption, Mac {
    // https://www.rfc-editor.org/rfc/rfc5830

    private static final String NAME = "GOST28147"; // Magma, GOST 28147-89 (RFC 5830)

    public GOST28147(@NotNull DetectionLocation detectionLocation) {
        super(NAME, BlockCipher.class, detectionLocation);
        this.put(new BlockSize(64, detectionLocation));
        this.put(new KeyLength(256, detectionLocation));
    }

    public GOST28147(@Nonnull Mode mode, @NotNull DetectionLocation detectionLocation) {
        this(detectionLocation);
        this.put(mode);
    }

    public GOST28147(
            @Nonnull final Class<? extends IPrimitive> asKind, @NotNull GOST28147 gost28147) {
        super(gost28147, asKind);
    }
}
