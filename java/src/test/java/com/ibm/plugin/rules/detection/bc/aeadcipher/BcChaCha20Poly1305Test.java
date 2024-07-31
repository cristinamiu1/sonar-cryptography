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
package com.ibm.plugin.rules.detection.bc.aeadcipher;

import static org.assertj.core.api.Assertions.assertThat;

import com.ibm.engine.detection.DetectionStore;
import com.ibm.engine.model.IValue;
import com.ibm.engine.model.MacSize;
import com.ibm.engine.model.OperationMode;
import com.ibm.engine.model.ValueAction;
import com.ibm.engine.model.context.AlgorithmParameterContext;
import com.ibm.engine.model.context.CipherContext;
import com.ibm.engine.model.context.MacContext;
import com.ibm.mapper.model.AuthenticatedEncryption;
import com.ibm.mapper.model.HMAC;
import com.ibm.mapper.model.INode;
import com.ibm.mapper.model.StreamCipher;
import com.ibm.mapper.model.TagLength;
import com.ibm.mapper.model.functionality.Digest;
import com.ibm.mapper.model.functionality.Encrypt;
import com.ibm.mapper.model.functionality.Tag;
import com.ibm.plugin.TestBase;
import com.ibm.plugin.rules.detection.bc.BouncyCastleJars;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.sonar.java.checks.verifier.CheckVerifier;
import org.sonar.plugins.java.api.JavaCheck;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.semantic.Symbol;
import org.sonar.plugins.java.api.tree.Tree;

class BcChaCha20Poly1305Test extends TestBase {
    @Test
    void test() {
        CheckVerifier.newVerifier()
                .onFile(
                        "src/test/files/rules/detection/bc/aeadcipher/BcChaCha20Poly1305TestFile.java")
                .withChecks(this)
                .withClassPath(BouncyCastleJars.JARS)
                .verifyIssues();
    }

    @Override
    public void asserts(
            int findingId,
            @NotNull DetectionStore<JavaCheck, Tree, Symbol, JavaFileScannerContext> detectionStore,
            @NotNull List<INode> nodes) {
        /**
         * TODO: Optimally, we shouldn't have these direct detections of engines, as they appear in
         * the depending detection rules
         */
        if (findingId == 2) {
            return;
        }
        /*
         * Detection Store
         */

        assertThat(detectionStore.getDetectionValues()).hasSize(1);
        assertThat(detectionStore.getDetectionValueContext()).isInstanceOf(CipherContext.class);
        IValue<Tree> value0 = detectionStore.getDetectionValues().get(0);
        assertThat(value0).isInstanceOf(ValueAction.class);
        assertThat(value0.asString()).isEqualTo("ChaCha20Poly1305");

        DetectionStore<JavaCheck, Tree, Symbol, JavaFileScannerContext> store_1 =
                getStoreOfValueType(OperationMode.class, detectionStore.getChildren());
        assertThat(store_1.getDetectionValues()).hasSize(1);
        assertThat(store_1.getDetectionValueContext()).isInstanceOf(CipherContext.class);
        IValue<Tree> value0_1 = store_1.getDetectionValues().get(0);
        assertThat(value0_1).isInstanceOf(OperationMode.class);
        assertThat(value0_1.asString()).isEqualTo("1");

        DetectionStore<JavaCheck, Tree, Symbol, JavaFileScannerContext> store_1_1 =
                getStoreOfValueType(MacSize.class, store_1.getChildren());
        assertThat(store_1_1.getDetectionValues()).hasSize(1);
        assertThat(store_1_1.getDetectionValueContext())
                .isInstanceOf(AlgorithmParameterContext.class);
        IValue<Tree> value0_1_1 = store_1_1.getDetectionValues().get(0);
        assertThat(value0_1_1).isInstanceOf(MacSize.class);
        assertThat(value0_1_1.asString()).isEqualTo("128");

        if (findingId == 1) {
            DetectionStore<JavaCheck, Tree, Symbol, JavaFileScannerContext> store_2 =
                    getStoreOfValueType(ValueAction.class, detectionStore.getChildren());
            assertThat(store_2.getDetectionValues()).hasSize(1);
            assertThat(store_2.getDetectionValueContext()).isInstanceOf(MacContext.class);
            IValue<Tree> value0_2 = store_2.getDetectionValues().get(0);
            assertThat(value0_2).isInstanceOf(ValueAction.class);
            assertThat(value0_2.asString()).isEqualTo("Poly1305");
        }

        /*
         * Translation
         */

        assertThat(nodes).hasSize(1);

        // AuthenticatedEncryption
        INode authenticatedEncryptionNode1 = nodes.get(0);
        assertThat(authenticatedEncryptionNode1.getKind()).isEqualTo(AuthenticatedEncryption.class);
        assertThat(authenticatedEncryptionNode1.getChildren()).hasSize(3);
        assertThat(authenticatedEncryptionNode1.asString()).isEqualTo("ChaCha20Poly1305");

        // Encrypt under AuthenticatedEncryption
        INode encryptNode1 = authenticatedEncryptionNode1.getChildren().get(Encrypt.class);
        assertThat(encryptNode1).isNotNull();
        assertThat(encryptNode1.getChildren()).isEmpty();
        assertThat(encryptNode1.asString()).isEqualTo("ENCRYPT");

        // Mac under AuthenticatedEncryption
        INode macNode1 = authenticatedEncryptionNode1.getChildren().get(HMAC.class);
        assertThat(macNode1).isNotNull();
        assertThat(macNode1.getChildren()).hasSize(3);
        assertThat(macNode1.asString()).isEqualTo("Poly1305");

        // Digest under Mac under AuthenticatedEncryption
        INode digestNode1 = macNode1.getChildren().get(Digest.class);
        assertThat(digestNode1).isNotNull();
        assertThat(digestNode1.getChildren()).isEmpty();
        assertThat(digestNode1.asString()).isEqualTo("DIGEST");

        // TagLength under Mac under AuthenticatedEncryption
        INode tagLengthNode1 = macNode1.getChildren().get(TagLength.class);
        assertThat(tagLengthNode1).isNotNull();
        assertThat(tagLengthNode1.getChildren()).isEmpty();
        assertThat(tagLengthNode1.asString()).isEqualTo("128");

        // Tag under Mac under AuthenticatedEncryption
        INode tagNode1 = macNode1.getChildren().get(Tag.class);
        assertThat(tagNode1).isNotNull();
        assertThat(tagNode1.getChildren()).isEmpty();
        assertThat(tagNode1.asString()).isEqualTo("TAG");

        // StreamCipher under AuthenticatedEncryption
        INode streamCipherNode1 =
                authenticatedEncryptionNode1.getChildren().get(StreamCipher.class);
        assertThat(streamCipherNode1).isNotNull();
        assertThat(streamCipherNode1.getChildren()).isEmpty();
        assertThat(streamCipherNode1.asString()).isEqualTo("ChaCha20");
    }
}
