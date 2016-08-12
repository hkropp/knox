/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.hadoop.gateway.shirorealm;

import java.util.Scanner;

import org.junit.Test;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

public class KnoxPamRealmTest {
  @Test
  public void setService() {
    KnoxPamRealm realm = new KnoxPamRealm();
    realm.setService("knox-pam-os-service");
    //assertEquals(realm.getService(), "knox-pam-os-service");
  }

  public void testDoGetAuthenticationInfo() {
    KnoxPamRealm realm = new KnoxPamRealm();
    realm.setService("sshd");                       // pam settings being used: /etc/pam.d/sshd

    /*
     * Does obfuscate the password but not work in IDE: http://stackoverflow.com/questions/10819469/hide-input-on-command-line
     */
    //Console console = System.console();
    //String username = console.readLine("username: ");
    //String password = new String(console.readPassword("password: "));


    // read username/password from console
    Scanner scanner = new Scanner(System.in);
    System.out.print("username: ");
    String username = scanner.next();

    System.out.print("password: ");
    String password = scanner.next();

    // mock shiro auth token
    UsernamePasswordToken authToken = createMock(UsernamePasswordToken.class);
    expect(authToken.getUsername()).andReturn(username);
    expect(authToken.getPassword()).andReturn(password.toCharArray());
    expect(authToken.getCredentials()).andReturn(password);
    replay(authToken);

    // login
    AuthenticationInfo authInfo = realm.doGetAuthenticationInfo(authToken);

    // verify success
    assertTrue(authInfo.getCredentials() != null);
  }

  public static void main(String[] args) throws Exception {
    KnoxPamRealmTest pamTest = new KnoxPamRealmTest();
    pamTest.testDoGetAuthenticationInfo();
  }
}
