/*
 * Copyright (C) 2016 ai
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

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPMessage;
import com.sun.mail.imap.IMAPSSLStore;
import com.sun.mail.imap.IMAPStore;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;

/**
 *
 * @author ai
 */
public class IMAPConnection implements IEMailConnection {

    private IMAPStore _store;
    private IMAPFolder _folder;
    private IMAPSSLStore _sslStore;
    private EMailServerParameters _parameters;


    IMAPConnection(Session session) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

 

    
    /**
     *
     * @param parameters parameters for
     */
//    public EMailConnection(EMailServerParameters parameters) {
//        _parameters = parameters;
//        setConnection();
//    }

    private void setConnection(Session session) {
        try {
            _store = (IMAPStore) session.getStore();
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(IMAPConnection.class.getName()).log(Level.SEVERE, "EMail Connection session failed", ex);
        }
        try {
            _store.connect(_parameters.getHost(), _parameters.getUserEMail(), _parameters.getPassword());
        } catch (MessagingException ex) {
            Logger.getLogger(IMAPConnection.class.getName()).log(Level.SEVERE, "EMail Connection setting failed", ex);
        }
        
    }

    public Folder getFolder() {
        try {
            if (!_store.isConnected()) {
                org.apache.log4j.Logger.getLogger(this.getClass().getName()).log(org.apache.log4j.Level.ERROR, "Connected not possible");
            }
            _folder = (IMAPFolder) _store.getFolder(_parameters.getFolderName());
        } catch (MessagingException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Unable to open the inbox folder", ex);
        }
        return _folder;
    }

    @Override
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
