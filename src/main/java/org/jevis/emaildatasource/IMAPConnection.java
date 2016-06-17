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
import com.sun.mail.imap.IMAPSSLStore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

/**
 *
 * @author ai
 */
public class IMAPConnection implements IEMailConnection {

    private Store _store;
    private IMAPFolder _folder;
    private IMAPSSLStore _sslStore;
    private String _foldName;

    IMAPConnection() {
    }

    public void setConnection(Session session, EMailServerParameters param) {
        try {
            _store = session.getStore();
            _foldName = param.getFolderName();
            _store.connect(param.getHost(), param.getUserEMail(), param.getPassword());
        } catch (MessagingException ex) {
            Logger.getLogger(IMAPConnection.class.getName()).log(Level.SEVERE, "EMail Connection setting failed", ex);
        }

    }

    @Override
    public Folder getFolder() {
        try {
            if (!_store.isConnected()) {
                org.apache.log4j.Logger.getLogger(this.getClass().getName()).log(org.apache.log4j.Level.ERROR, "Connected not possible");
            }
            _folder = (IMAPFolder) _store.getFolder(_foldName);
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
