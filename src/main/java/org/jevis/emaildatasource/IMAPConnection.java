/**
 * Copyright (C) 2013 - 2016 Envidatec GmbH <info@envidatec.com>
 *
 * This file is part of JEAPI.
 *
 * JEAPI is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation in version 3.
 *
 * JEAPI is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * JEAPI. If not, see <http://www.gnu.org/licenses/>.
 *
 * JEAPI is part of the OpenJEVis project, further project information are
 * published at <http://www.OpenJEVis.org/>.
 */
package org.jevis.emaildatasource;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPSSLStore;
import com.sun.mail.imap.IMAPStore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

/**
 * The IMAPConnection class
 *
 * @author Artur Iablokov
 */
public class IMAPConnection implements EMailConnection {

    private Store _store;
    private IMAPFolder _folder;
    private IMAPSSLStore _sslStore;
    private String _foldName;

    @Override
    public void setConnection(Session session, EMailServerParameters param) {

        if (param.isSsl()) {
            _store = new IMAPSSLStore(session, null);
        } else {
            _store = new IMAPStore(session, null);
        }   
        _foldName = param.getFolderName();
        try {
            _store.connect(param.getHost(), param.getUserEMail(), param.getPassword());
        } catch (MessagingException ex) {
            Logger.getLogger(IMAPConnection.class.getName()).log(Level.SEVERE, "EMail Connection setting failed. Wrong login data or properties.", ex);
        }

    }

    @Override
    public IMAPFolder getFolder() {
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
            Logger.getLogger(IMAPConnection.class.getName()).log(Level.SEVERE, "Email-Folder terminate failed", ex);
        }
        try {
            _store.close();
        } catch (MessagingException ex) {
            Logger.getLogger(IMAPConnection.class.getName()).log(Level.SEVERE, "Email-Store terminate failed", ex);
        }
    }
}
