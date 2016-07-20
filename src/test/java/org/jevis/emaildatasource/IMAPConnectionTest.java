/*
 * Copyright (C) 2016 bi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jevis.emaildatasource;

import javax.mail.Folder;
import javax.mail.Session;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author bi
 */
public class IMAPConnectionTest {
    
    public IMAPConnectionTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of setConnection method, of class IMAPConnection.
     */
    @Test
    public void testSetConnection() {
        System.out.println("setConnection");
        Session session = null;
        EMailServerParameters param = null;
        IMAPConnection instance = new IMAPConnection();
        instance.setConnection(session, param);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFolder method, of class IMAPConnection.
     */
    @Test
    public void testGetFolder() {
        System.out.println("getFolder");
        IMAPConnection instance = new IMAPConnection();
        Folder expResult = null;
        Folder result = instance.getFolder();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of terminate method, of class IMAPConnection.
     */
    @Test
    public void testTerminate() {
        System.out.println("terminate");
        IMAPConnection instance = new IMAPConnection();
        instance.terminate();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
