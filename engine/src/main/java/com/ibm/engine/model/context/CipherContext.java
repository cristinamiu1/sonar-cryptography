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
package com.ibm.engine.model.context;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;

public class CipherContext extends DetectionContext
        implements IDetectionContext, ISupportKind<CipherContext.Kind> {

    public enum Kind {
        PKE,
        RSA,
        Fernet,
        OAEP,
        CHACHA20POLY1305,
        AES_WRAP,
        AES_WRAP_WITH_PADDING,
        ENCRYPTION_STATUS,
        WRAPPING_STATUS,
        ENCODING,
        ENCODING_SIGNATURE,
        WRAP,
        BLOCK_CIPHER,
        BLOCK_CIPHER_ENGINE,
        BLOCK_CIPHER_ENGINE_FOR_AEAD,
        STREAM_CIPHER_ENGINE,
        ASYMMETRIC_CIPHER_ENGINE,
        ASYMMETRIC_CIPHER_ENGINE_SIGNATURE,
        ASYMMETRIC_BUFFERED_BLOCK_CIPHER,
        BUFFERED_BLOCK_CIPHER,
        AEAD_BLOCK_CIPHER,
        AEAD_ENGINE,
        PADDING,
        PBE,
        HASH,
        NONE
    }

    @Nonnull private final Kind kind;

    /**
     * use a property map instead
     *
     * @deprecated
     */
    @Deprecated(since = "1.3.0")
    public CipherContext(@Nonnull Kind kind) {
        super(new HashMap<>());
        this.kind = kind;
    }

    public CipherContext() {
        super(new HashMap<>());
        this.kind = Kind.NONE;
    }

    public CipherContext(@Nonnull Map<String, String> properties) {
        super(properties);
        this.kind = Kind.NONE;
    }

    /**
     * use a property map instead
     *
     * @deprecated
     */
    @Deprecated(since = "1.3.0")
    @Nonnull
    public Kind kind() {
        return kind;
    }

    @Nonnull
    @Override
    public Class<? extends IDetectionContext> type() {
        return CipherContext.class;
    }
}
