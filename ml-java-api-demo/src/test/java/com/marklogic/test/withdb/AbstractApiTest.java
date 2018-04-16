package com.marklogic.test.withdb;

import org.junit.Before;

import com.marklogic.mgmt.api.API;
import com.marklogic.mgmt.api.Resource;

public abstract class AbstractApiTest extends com.marklogic.test.withdb.AbstractMgmtTest {

    protected API api;

    @Before
    public void setup() {
        api = new API(manageClient);
    }

    protected void deleteIfExists(Resource... resources) {
        for (Resource r : resources) {
            if (r != null && r.exists()) {
                r.delete();
            }
        }
    }
}