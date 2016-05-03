/*
 * Copyright (C) 2016 AI
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

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

/**
 *
 * @author Artur Iablokov
 */
public class EMailConnection {

    private final EMailServerParameters _parameters;
    private Store _store;
    private Folder _folder;
    private Session _session;
    
    public EMailConnection(EMailServerParameters parameters) {
        _parameters = parameters;
        setConnection();
    }
    
    private void setConnection() {
        try {
            Properties props = new Properties();
            props.put("mail.debug", "true");
            props.put("mail.store.protocol", "imaps");
            //props.;    
            _session = Session.getInstance(props);
            _store = _session.getStore();
            _store.connect(_parameters.getHost(), _parameters.getUserEMail(), _parameters.getPassword());
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(EMailConnection.class.getName()).log(Level.SEVERE, "EMail Connection failed", ex);
        } catch (MessagingException ex) {
            Logger.getLogger(EMailConnection.class.getName()).log(Level.SEVERE, "EMail Connection failed", ex);
        }
    }

    public Folder getFolder(String foldername) {
        try {
            if (!_store.isConnected()) {
                org.apache.log4j.Logger.getLogger(this.getClass().getName()).log(org.apache.log4j.Level.ERROR, "Connected not possible");
            }
            _folder = _store.getFolder(foldername);
        } catch (MessagingException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Unable to open the inbox folder", ex);
        }
        return _folder;
    }

    public void terminate() {
        try {
            _folder.close(false);
        } catch (MessagingException ex) {
            Logger.getLogger(EMailConnection.class.getName()).log(Level.SEVERE, "Email-Folder terminate failed", ex);
        }
        try {
            _store.close();
        } catch (MessagingException ex) {
            Logger.getLogger(EMailConnection.class.getName()).log(Level.SEVERE, "Email-Store terminate failed", ex);
        }
    }

}
