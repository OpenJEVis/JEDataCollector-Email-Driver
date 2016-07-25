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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.search.SearchTerm;
import org.apache.commons.lang.StringUtils;

/**
 * EMailManager Class is a service that is initiated by the creation and
 * termination of the connection, as well as the search and returns the required
 * messages.
 *
 * @author Artur Iablokov
 */
public class EMailManager {

    /**
     * Get list of attachments
     *
     * @param filter
     * @param conn
     *
     * @return List of InputStream
     */
    public static List<InputStream> getAnswerList(EMailChannelParameters filter, EMailConnection conn) {
        List<InputStream> input = new ArrayList<>();

        Folder folder = conn.getFolder();
        List<Message> messages = getMessageList(folder, filter);

        if (messages != null) {

            for (Message message : messages) {
                try {
                    //test
                    System.out.println("MESSAGE SUBJECT!!!!!!: " + message.getSubject());
                    //test end

                    if (message.isMimeType("multipart/*")) {
                        Multipart multiPart = (Multipart) message.getContent();
                        // For all multipart contents
                        for (int i = 0; i < multiPart.getCount(); i++) {
                            MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(i);

                            // If multipart content is attachment
                            String disp = part.getDisposition();
                            String partName = part.getFileName();

                            if (!Part.ATTACHMENT.equalsIgnoreCase(disp)
                                    && !StringUtils.isNotBlank(partName)) {
                                continue; // dealing with attachments only
                            }

                            if (Part.ATTACHMENT.equalsIgnoreCase(disp) || disp == null) {
                                System.out.println("EMail attach: " + " " + partName + " !///! " + part.getContentType());
                                input.add(part.getInputStream());
                            }
                        }
                    }
                } catch (MessagingException | IOException ex) {
                    Logger.getLogger(EMailDataSource.class.getName()).log(Level.SEVERE, "could not process the attachment!", ex);
                }
            }
        }

        return input;
    }

    /**
     * Get list of messages
     *
     * @param folder
     * @param conn
     *
     * @return List of Message
     */
    private static List<Message> getMessageList(Folder folder, EMailChannelParameters chanParam) {

        List<Message> messageList = null;

        try {
            folder.open(Folder.READ_ONLY);
        } catch (MessagingException ex) {
            Logger.getLogger(EMailManager.class.getName()).log(Level.SEVERE, "EMail folder is not available to read.", ex);
        }

        SearchTerm term = chanParam.getSearchTerms();
        
        Message[] msgs = null;
        try {
            msgs = folder.search(term);
        } catch (MessagingException ex) {
            Logger.getLogger(EMailManager.class.getName()).log(Level.SEVERE, "Unable to search messages", ex);
        }
        messageList = Arrays.asList(msgs);

//        try {
//            messageList = Arrays.asList(folder.search(chanParam.getSearchTerms()));
//        } catch (MessagingException ex) {
//            Logger.getLogger(EMailManager.class.getName()).log(Level.SEVERE, "Unable to search messages", ex);
//        }
        return messageList;
    }

    /**
     * Create special EMail Connection
     *
     * @param filter
     * @param conn
     *
     * @return List of InputStream
     */
    public static EMailConnection createConnection(EMailServerParameters parameters) {
        
        EMailConnection conn = null;
        Properties props = createProperties(parameters);
        Session session = Session.getInstance(props);
        if (parameters.getProtocol().equalsIgnoreCase(EMailConstants.Protocol.IMAP)) {
            conn = new IMAPConnection();
            conn.setConnection(session, parameters);
        } else if (parameters.getProtocol().equalsIgnoreCase(EMailConstants.Protocol.POP3)) {
            conn = new POP3Connection();
            conn.setConnection(session, parameters);
        } else {
            Logger.getLogger(EMailManager.class.getName()).log(Level.SEVERE, "EMail Connection failed");
        }
        
        return conn;
    }

    /**
     * Terminate EMail Connection
     *
     * @param conn EMail Connection
     */
    public static void terminate(EMailConnection conn) {
        conn.terminate();
    }

    /**
     * Create Properties for EMail Connection
     *
     * @param parameters EMail parameters from Frontend
     *
     * @return props
     */
    private static Properties createProperties(EMailServerParameters parameters) {

        Properties props = new Properties();
        String key = "mail." + parameters.getProtocol();
        System.out.println("Key is: " + key);
        props.put(key + ".host", parameters.getHost());
        props.put(key + ".port", parameters.getPort());
        props.put(key + ".connectiontimeout", parameters.getConnectionTimeout()); //*1000?ms
        props.put(key + ".timeout", parameters.getReadTimeout());    //*1000?ms

//        String ssl = parameters.getSsl();
//        if (ssl.equals(EMailConstants.ValidValues.CryptProtocols.SSL_TLS)) {
//            props.put(key + ".ssl.enable", true);
//        } else if (ssl.equals(EMailConstants.ValidValues.CryptProtocols.STARTTLS)) {
//            props.put(key + "starttls.enable", true);
//        }
        //_parameters.getAuthentication() usually not used in SSL connections
        props.put("mail.debug", "true");
        props.put("mail.store.protocol", "imaps");
        return props;
    }
}
