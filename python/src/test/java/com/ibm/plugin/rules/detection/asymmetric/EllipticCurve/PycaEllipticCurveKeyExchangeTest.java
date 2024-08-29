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
package com.ibm.plugin.rules.detection.asymmetric.EllipticCurve;

import static org.assertj.core.api.Assertions.assertThat;

import com.ibm.engine.detection.DetectionStore;
import com.ibm.engine.model.Algorithm;
import com.ibm.engine.model.Curve;
import com.ibm.engine.model.IValue;
import com.ibm.engine.model.KeySize;
import com.ibm.engine.model.context.KeyAgreementContext;
import com.ibm.engine.model.context.KeyDerivationFunctionContext;
import com.ibm.engine.model.context.PrivateKeyContext;
import com.ibm.mapper.model.BlockSize;
import com.ibm.mapper.model.DigestSize;
import com.ibm.mapper.model.EllipticCurve;
import com.ibm.mapper.model.INode;
import com.ibm.mapper.model.KeyAgreement;
import com.ibm.mapper.model.KeyDerivationFunction;
import com.ibm.mapper.model.KeyLength;
import com.ibm.mapper.model.Oid;
import com.ibm.mapper.model.PrivateKey;
import com.ibm.mapper.model.functionality.KeyDerivation;
import com.ibm.mapper.model.functionality.KeyGeneration;
import com.ibm.plugin.TestBase;
import java.util.List;
import javax.annotation.Nonnull;
import org.junit.Test;
import org.sonar.plugins.python.api.PythonCheck;
import org.sonar.plugins.python.api.PythonVisitorContext;
import org.sonar.plugins.python.api.symbols.Symbol;
import org.sonar.plugins.python.api.tree.Tree;
import org.sonar.python.checks.utils.PythonCheckVerifier;

public class PycaEllipticCurveKeyExchangeTest extends TestBase {

    @Test
    public void test() {
        PythonCheckVerifier.verify(
                "src/test/files/rules/detection/asymmetric/EllipticCurve/PycaEllipticCurveKeyExchangeTestFile.py",
                this);
    }

    @Override
    public void asserts(
            int findingId,
            @Nonnull DetectionStore<PythonCheck, Tree, Symbol, PythonVisitorContext> detectionStore,
            @Nonnull List<INode> nodes) {
        if (findingId == 0) {
            /*
             * Detection Store
             */

            assertThat(detectionStore.getDetectionValues()).hasSize(1);
            assertThat(detectionStore.getDetectionValueContext())
                    .isInstanceOf(PrivateKeyContext.class);
            IValue<Tree> value0 = detectionStore.getDetectionValues().get(0);
            assertThat(value0).isInstanceOf(Curve.class);
            assertThat(value0.asString()).isEqualTo("SECP384R1");

            DetectionStore<PythonCheck, Tree, Symbol, PythonVisitorContext> store_1 =
                    getStoreOfValueType(Algorithm.class, detectionStore.getChildren());
            assertThat(store_1.getDetectionValues()).hasSize(1);
            assertThat(store_1.getDetectionValueContext()).isInstanceOf(KeyAgreementContext.class);
            IValue<Tree> value0_1 = store_1.getDetectionValues().get(0);
            assertThat(value0_1).isInstanceOf(Algorithm.class);
            assertThat(value0_1.asString()).isEqualTo("ECDH");

            /*
             * Translation
             */

            assertThat(nodes).hasSize(1);

            // PrivateKey
            INode privateKeyNode = nodes.get(0);
            assertThat(privateKeyNode.getKind()).isEqualTo(PrivateKey.class);
            assertThat(privateKeyNode.getChildren()).hasSize(2);
            assertThat(privateKeyNode.asString()).isEqualTo("EC-secp384r1");

            // KeyGeneration under PrivateKey
            INode keyGenerationNode = privateKeyNode.getChildren().get(KeyGeneration.class);
            assertThat(keyGenerationNode).isNotNull();
            assertThat(keyGenerationNode.getChildren()).isEmpty();
            assertThat(keyGenerationNode.asString()).isEqualTo("KEYGENERATION");

            // KeyAgreement under PrivateKey
            INode keyAgreementNode = privateKeyNode.getChildren().get(KeyAgreement.class);
            assertThat(keyAgreementNode).isNotNull();
            assertThat(keyAgreementNode.getChildren()).hasSize(2);
            assertThat(keyAgreementNode.asString()).isEqualTo("ECDH");

            // EllipticCurve under KeyAgreement under PrivateKey
            INode ellipticCurveNode = keyAgreementNode.getChildren().get(EllipticCurve.class);
            assertThat(ellipticCurveNode).isNotNull();
            assertThat(ellipticCurveNode.getChildren()).isEmpty();
            assertThat(ellipticCurveNode.asString()).isEqualTo("secp384r1");

            // Oid under KeyAgreement under PrivateKey
            INode oidNode = keyAgreementNode.getChildren().get(Oid.class);
            assertThat(oidNode).isNotNull();
            assertThat(oidNode.getChildren()).isEmpty();
            assertThat(oidNode.asString()).isEqualTo("1.3.132.1.12");
        } else if (findingId == 1) {
            /*
             * Detection Store
             */
            assertThat(detectionStore.getDetectionValues()).hasSize(1);
            assertThat(detectionStore.getDetectionValueContext())
                    .isInstanceOf(KeyDerivationFunctionContext.class);
            IValue<Tree> value0 = detectionStore.getDetectionValues().get(0);
            assertThat(value0).isInstanceOf(Algorithm.class);
            assertThat(value0.asString()).isEqualTo("SHA256");

            DetectionStore<PythonCheck, Tree, Symbol, PythonVisitorContext> store_1 =
                    getStoreOfValueType(KeySize.class, detectionStore.getChildren());
            assertThat(store_1.getDetectionValues()).hasSize(1);
            assertThat(store_1.getDetectionValueContext())
                    .isInstanceOf(KeyDerivationFunctionContext.class);
            IValue<Tree> value0_1 = store_1.getDetectionValues().get(0);
            assertThat(value0_1).isInstanceOf(KeySize.class);
            assertThat(value0_1.asString()).isEqualTo("256");

            /*
             * Translation
             */
            assertThat(nodes).hasSize(1);

            // KeyDerivationFunction
            INode keyDerivationFunctionNode = nodes.get(0);
            assertThat(keyDerivationFunctionNode.getKind()).isEqualTo(KeyDerivationFunction.class);
            assertThat(keyDerivationFunctionNode.getChildren()).hasSize(5);
            assertThat(keyDerivationFunctionNode.asString()).isEqualTo("SHA256");

            // BlockSize under KeyDerivationFunction
            INode blockSizeNode = keyDerivationFunctionNode.getChildren().get(BlockSize.class);
            assertThat(blockSizeNode).isNotNull();
            assertThat(blockSizeNode.getChildren()).isEmpty();
            assertThat(blockSizeNode.asString()).isEqualTo("512");

            // DigestSize under KeyDerivationFunction
            INode digestSizeNode = keyDerivationFunctionNode.getChildren().get(DigestSize.class);
            assertThat(digestSizeNode).isNotNull();
            assertThat(digestSizeNode.getChildren()).isEmpty();
            assertThat(digestSizeNode.asString()).isEqualTo("256");

            // KeyDerivation under KeyDerivationFunction
            INode keyDerivationNode =
                    keyDerivationFunctionNode.getChildren().get(KeyDerivation.class);
            assertThat(keyDerivationNode).isNotNull();
            assertThat(keyDerivationNode.getChildren()).isEmpty();
            assertThat(keyDerivationNode.asString()).isEqualTo("KEYDERIVATION");

            // KeyLength under KeyDerivationFunction
            INode keyLengthNode = keyDerivationFunctionNode.getChildren().get(KeyLength.class);
            assertThat(keyLengthNode).isNotNull();
            assertThat(keyLengthNode.getChildren()).isEmpty();
            assertThat(keyLengthNode.asString()).isEqualTo("256");

            // Oid under KeyDerivationFunction
            INode oidNode = keyDerivationFunctionNode.getChildren().get(Oid.class);
            assertThat(oidNode).isNotNull();
            assertThat(oidNode.getChildren()).isEmpty();
            assertThat(oidNode.asString()).isEqualTo("2.16.840.1.101.3.4.2.1");
        }
    }
}
