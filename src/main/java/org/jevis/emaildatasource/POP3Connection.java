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

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPMessage;
import com.sun.mail.imap.IMAPSSLStore;
import com.sun.mail.imap.IMAPStore;
import com.sun.mail.pop3.POP3Folder;
import com.sun.mail.pop3.POP3Store;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;

/**
 *
 * @author bi
 */
public class POP3Connection {//implements IEMailConnection{
////    private POP3Store _store;
////    private POP3Folder _folder;
////    private Session _session;
//    
////    POP3Connection(EMailServerParameters parameters) {
////        setConnection(_session);
////    }
////
////
////    /**
////     *
////     * @param parameters parameters for
////     */
//////    public EMailConnection(EMailServerParameters parameters) {
//////        _parameters = parameters;
//////        setConnection();
//////    }
////
////    private void setConnection(Session session) {
////        try {
////            _store = (IMAPStore) session.getStore();
////        } catch (NoSuchProviderException ex) {
////            Logger.getLogger(IMAPConnection.class.getName()).log(Level.SEVERE, "EMail Connection session failed", ex);
////        }
////        try {
////            _store.connect(_parameters.getHost(), _parameters.getUserEMail(), _parameters.getPassword());
////        } catch (MessagingException ex) {
////            Logger.getLogger(IMAPConnection.class.getName()).log(Level.SEVERE, "EMail Connection setting failed", ex);
////        }
////    }
////
////    public Folder getFolder() {
////        try {
////            if (!_store.isConnected()) {
////                org.apache.log4j.Logger.getLogger(this.getClass().getName()).log(org.apache.log4j.Level.ERROR, "Connected not possible");
////            }
////            _folder = _store.getFolder(_parameters.getFolderName());
////        } catch (MessagingException ex) {
////            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Unable to open the inbox folder", ex);
////        }
////        return _folder;
////    }
////
////    @Override
////    public void terminate() {
////        try {
////            _folder.close(false);
////        } catch (MessagingException ex) {
////            Logger.getLogger(EMailConnection.class.getName()).log(Level.SEVERE, "Email-Folder terminate failed", ex);
////        }
////        try {
////            _store.close();
////        } catch (MessagingException ex) {
////            Logger.getLogger(EMailConnection.class.getName()).log(Level.SEVERE, "Email-Store terminate failed", ex);
////        }
////    }
////
////    private Properties createProperties() {
////
////        Properties props = new Properties();
////        String key = "mail." + _parameters.getProtocol();
////        props.put(key + ".host", _parameters.getHost());
////        props.put(key + ".port", _parameters.getPort());
////        props.put(key + ".connectiontimeout", _parameters.getConnectionTimeout() * 1000);
////        props.put(key + ".timeout", _parameters.getReadTimeout() * 1000);
////
////        String ssl = _parameters.getSsl();
////        if (ssl.equals(EMailConstants.ValidValues.CryptProtocols.SSL_TLS)) {
////            props.put(key + ".ssl.enable", true);
////        } else if (ssl.equals(EMailConstants.ValidValues.CryptProtocols.STARTTLS)) {
////            props.put(key + "starttls.enable", true);
////        }
////
////        //_parameters.getAuthentication() usually not used in SSL connections
//////        props.put("mail.debug", "true");
//////        props.put("mail.store.protocol", "imaps");
////        return props;
////    }
////
////    public List<InputStream> getAnswerList(MessageFilter filter) {
////
////        List<IMAPMessage> messages = filter.getMessageList(_folder);
////
////        for (IMAPMessage message : messages) {
////            try {
////                if (message.isMimeType("multipart/*")) {
////                    Multipart multiPart = (Multipart) message.getContent();
////                    // For all multipart contents
////                    for (int i = 0; i < multiPart.getCount(); i++) {
////                        MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(i);
////                        // If multipart content is attachment
////                        System.out.println("Name is: " + part.getFileName());
////                        String disp = part.getDisposition();
////                        if (Part.ATTACHMENT.equalsIgnoreCase(disp) || disp == null) {
////                            System.out.println("EMail attach: " + " " + part.getFileName() + " !///! " + part.getContentType());
////                        }
////                    }
////                }
////            } catch (MessagingException | IOException ex) {
////                Logger.getLogger(EMailDataSource.class.getName()).log(Level.SEVERE, "could not process the attachment!", ex);
////            }
////        }
////
////        return null;
////    }
    
}
