/*
 * Licensed to ObjectStyle LLC under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ObjectStyle LLC licenses
 * this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.bootique.linkmove.v4;

import io.bootique.BQRuntime;
import io.bootique.Bootique;
import io.bootique.jdbc.junit.derby.DerbyTester;
import io.bootique.junit.BQApp;
import io.bootique.junit.BQTest;
import io.bootique.junit.BQTestTool;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.access.jdbc.SQLTemplateProcessor;
import org.apache.cayenne.runtime.CayenneRuntime;
import org.apache.cayenne.query.SQLSelect;
import org.apache.cayenne.velocity.VelocitySQLTemplateProcessor;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@BQTest
public class VelocityIT {

    // Bypassing LinkMove here. Testing whether Cayenne stack works with Velocity. Velocity version mismatch used to
    // be a frequent source of instability

    @BQTestTool
    static final DerbyTester db = DerbyTester.db();

    @BQApp(skipRun = true)
    static final BQRuntime app = Bootique.app()
            .autoLoadModules()
            .module(db.moduleWithTestDataSource("ds"))
            .createRuntime();

    @Test
    public void checkVelocity() {
        SQLTemplateProcessor p = app.getInstance(CayenneRuntime.class)
                .getInjector()
                .getInstance(SQLTemplateProcessor.class);

        assertInstanceOf(VelocitySQLTemplateProcessor.class, p);
    }

    @Test
    public void checkSQLSelect() {
        ObjectContext c = app.getInstance(CayenneRuntime.class).newContext();

        Object[] vals = SQLSelect.columnQuery("VALUES $p").param("p", "CURRENT_TIMESTAMP").selectOne(c);
        assertInstanceOf(Date.class, vals[0]);
    }

}
