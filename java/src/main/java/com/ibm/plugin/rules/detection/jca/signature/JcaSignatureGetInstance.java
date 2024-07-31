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
package com.ibm.plugin.rules.detection.jca.signature;

import static com.ibm.plugin.rules.detection.TypeShortcuts.STRING_TYPE;

import com.ibm.engine.model.context.SignatureContext;
import com.ibm.engine.model.factory.AlgorithmFactory;
import com.ibm.engine.rule.IDetectionRule;
import com.ibm.engine.rule.builder.DetectionRuleBuilder;
import java.util.List;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import org.jetbrains.annotations.Unmodifiable;
import org.sonar.plugins.java.api.tree.Tree;

public final class JcaSignatureGetInstance {

    private static final IDetectionRule<Tree> SIGNATURE_1 =
            new DetectionRuleBuilder<Tree>()
                    .createDetectionRule()
                    .forObjectTypes("java.security.Signature")
                    .forMethods("getInstance")
                    .withMethodParameter(STRING_TYPE)
                    .shouldBeDetectedAs(new AlgorithmFactory<>())
                    .buildForContext(new SignatureContext())
                    .inBundle(() -> "Jca")
                    .withDependingDetectionRules(
                            Stream.concat(
                                            JcaSignatureAction.rules().stream(),
                                            JcaSignatureSetParameter.rules().stream())
                                    .toList());

    private static final IDetectionRule<Tree> SIGNATURE_2 =
            new DetectionRuleBuilder<Tree>()
                    .createDetectionRule()
                    .forObjectTypes("java.security.Signature")
                    .forMethods("getInstance")
                    .withMethodParameter(STRING_TYPE)
                    .shouldBeDetectedAs(new AlgorithmFactory<>())
                    .withMethodParameter(STRING_TYPE)
                    .buildForContext(new SignatureContext())
                    .inBundle(() -> "Jca")
                    .withDependingDetectionRules(
                            Stream.concat(
                                            JcaSignatureAction.rules().stream(),
                                            JcaSignatureSetParameter.rules().stream())
                                    .toList());

    private static final IDetectionRule<Tree> SIGNATURE_3 =
            new DetectionRuleBuilder<Tree>()
                    .createDetectionRule()
                    .forObjectTypes("java.security.Signature")
                    .forMethods("getInstance")
                    .withMethodParameter(STRING_TYPE)
                    .shouldBeDetectedAs(new AlgorithmFactory<>())
                    .withMethodParameter("java.security.Provider")
                    .buildForContext(new SignatureContext())
                    .inBundle(() -> "Jca")
                    .withDependingDetectionRules(
                            Stream.concat(
                                            JcaSignatureAction.rules().stream(),
                                            JcaSignatureSetParameter.rules().stream())
                                    .toList());

    private JcaSignatureGetInstance() {
        // nothing
    }

    @Unmodifiable
    @Nonnull
    public static List<IDetectionRule<Tree>> rules() {
        return List.of(SIGNATURE_1, SIGNATURE_2, SIGNATURE_3);
    }
}
