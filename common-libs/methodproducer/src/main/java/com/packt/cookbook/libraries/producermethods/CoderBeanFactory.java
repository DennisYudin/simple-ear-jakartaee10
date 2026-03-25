/*
 * Copyright (c), Eclipse Foundation, Inc. and its licensors.
 *
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v1.0, which is available at
 * https://www.eclipse.org/org/documents/edl-v10.php
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package com.packt.cookbook.libraries.producermethods;

import com.packt.cookbook.libraries.coder.Coder;
import com.packt.cookbook.libraries.coder.CoderImpl;
import com.packt.cookbook.libraries.coder.CoderType;
import com.packt.cookbook.libraries.coder.TestCoderImpl;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Managed bean that calls a Coder implementation to perform a transformation on
 * an input string
 */
@ApplicationScoped
public class CoderBeanFactory {

    public Coder getCoder(CoderType type) {
		return switch (type) {
			case TEST -> new TestCoderImpl();
			case SHIFT -> new CoderImpl();
			default -> null;
		};
    }
}
