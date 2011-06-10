/*
 * Copyright (c) 2011 David Saff
 * Copyright (c) 2011 Christian Gruber
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.junit.contrib.truth;

import org.junit.contrib.truth.delegatetest.DelegationTest;
import org.junit.contrib.truth.extensiontest.ExtensionTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suite
 * 
 * TODO(cgruber): See if we even need this - Maven doesn't, and it
 * ends up being redundant in eclipse.
 * 
 * @author David Saff
 * @author Christian Gruber (cgruber@israfil.net)
 */
@RunWith(Suite.class) 
@SuiteClasses({ 
    BooleanTest.class, 
    CollectionTest.class, 
    ExpectFailureTest.class, 
    ExpectTest.class, 
    IntegerTest.class, 
    StringTest.class,
    SubjectTest.class,
    ExtensionTest.class,
    DelegationTest.class
    }) public class AllTests {

}
