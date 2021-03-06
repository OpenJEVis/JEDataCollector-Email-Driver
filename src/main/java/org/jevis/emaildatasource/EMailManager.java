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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

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
        final long start = System.currentTimeMillis();
        List<Message> messages = getMessageList(folder, filter);
        final long answerDone = System.currentTimeMillis();
        Logger.getLogger(EMailDataSource.class.getName()).log(Level.INFO, ">>Get message list: " + (answerDone - start) + " Millisek.");

        if (filter.getDataInBody()) {
            input = getAnswerListFromBody(messages);
        } else {
            input = getAnswerListFromAttach(messages, filter.getFilename());
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

        SearchTerm term = chanParam.getSearchTerms();
        try {
            folder.open(Folder.READ_ONLY);
        } catch (MessagingException ex) {
            Logger.getLogger(EMailManager.class.getName()).log(Level.SEVERE, "EMail folder is not available to read.", ex);
        }
        Message[] msgs = null;

        Logger.getLogger(EMailManager.class
                .getName()
        ).log(Level.INFO, "Folder is open: {0}", folder.isOpen());
        if (chanParam.getProtocol()
                .equalsIgnoreCase(EMailConstants.Protocol.IMAP)) {
            try {
                msgs = folder.search(term);
            } catch (MessagingException ex) {
                Logger.getLogger(EMailManager.class.getName()).log(Level.SEVERE, "Unable to search messages", ex);
            }
        } else if (chanParam.getProtocol()
                .equalsIgnoreCase(EMailConstants.Protocol.POP3)) {
            try {
//                int[] msgnums = new int[1000];
//                Message[] messages = folder.getMessages(msgnums);
                Message[] messages = folder.getMessages();
                messages = filterPOP3ByDate(messages, chanParam.getLastReadout());
                msgs = folder.search(term, messages);
            } catch (MessagingException ex) {
                Logger.getLogger(EMailManager.class.getName()).log(Level.SEVERE, "POP3: failed to receive messages from a folder.", ex);
            }
        } else {
            Logger.getLogger(EMailManager.class.getName()).log(Level.SEVERE, "Unable to search messages");
        }

        messageList = Arrays.asList(msgs);

        Logger.getLogger(EMailManager.class
                .getName()
        ).log(Level.INFO, "Messages found: {0}", messageList.size());

        return messageList;
    }

    /**
     * Create special EMail Connection
     *
     * @param parameters
     *
     * @return EMailConnection
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
            Logger.getLogger(EMailManager.class
                    .getName()).log(Level.SEVERE, "EMail Connection failed");
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

        String str = parameters.getProtocol();
        Properties props = new Properties();
        String key;
        String ssl = parameters.getSsl();
        if (ssl.equals(EMailConstants.ValidValues.CryptProtocols.SSL_TLS)) {
            //props.put("mail.store.protocol", str + "s");
            key = "mail." + str + "s";
            props.put(key + ".ssl.enable", true);
        } else {
            props.put("mail.store.protocol", str);
            key = "mail." + parameters.getProtocol();
        }
        if (ssl.equals(EMailConstants.ValidValues.CryptProtocols.STARTTLS)) {
            props.put(key + ".starttls.enable", true);
        }
        props.put(key + ".host", parameters.getHost());
        props.put(key + ".port", parameters.getPort().toString());
        props.put(key + ".connectiontimeout", parameters.getConnectionTimeout().toString()); //*1000?ms
        props.put(key + ".timeout", parameters.getReadTimeout().toString());    //*1000?ms
        //_parameters.getAuthentication() usually not used in SSL connections
        props.put("mail.debug", "false");
        return props;
    }

    /**
     * Find attachment and save it in inputstream
     *
     * @param message EMail message
     *
     * @return List of InputStream
     */
    private static List<InputStream> prepareAnswer(Message message, String filename) throws IOException, MessagingException {
        Multipart multiPart = (Multipart) message.getContent();
        List<InputStream> input = new ArrayList<>();
        // For all multipart contents
        for (int i = 0; i < multiPart.getCount(); i++) {

            MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(i);
            String disp = part.getDisposition();
            String partName = part.getFileName();

            Logger
                    .getLogger(EMailDataSource.class
                            .getName()).log(Level.INFO, "is Multipart");
            // If multipart content is attachment
            if (!Part.ATTACHMENT.equalsIgnoreCase(disp)
                    && !StringUtils.isNotBlank(partName)) {
                continue; // dealing with attachments only

            }

            if (Part.ATTACHMENT.equalsIgnoreCase(disp) || disp == null) {
                if (StringUtils.containsIgnoreCase(part.getFileName(), filename)) {
                    Logger.getLogger(EMailManager.class
                            .getName()).log(Level.INFO, "Attach found: {0}", part.getFileName());
                    final long start = System.currentTimeMillis();
                    input.add(toInputStream(part));//add attach to answerlist
                    final long answerDone = System.currentTimeMillis();
                    Logger
                            .getLogger(EMailDataSource.class
                                    .getName()).log(Level.INFO, ">>Attach to inputstream: " + (answerDone - start) + " Millisek.");

                }
            }
        } //for multipart check
        return input;
    }

    private static Message[] filterPOP3ByDate(Message[] messages, DateTime datetime) {
        Date date = datetime.toDate();
        List<Message> msg = new ArrayList<>();
        for (Message message : messages) {
            try {
                if (message.getSentDate().after(date)) {
                    msg.add(message);

                }
            } catch (MessagingException ex) {
                Logger.getLogger(EMailManager.class
                        .getName()).log(Level.SEVERE, "POP3: failed to filter messages by date.", ex);
            }
        }
        int size = msg.size();
        Logger
                .getLogger(EMailManager.class
                        .getName()).log(Level.INFO, "POP3: messages after filtering by date: {0}", size);
        Message[] msgArray = new Message[size];
        return msg.toArray(msgArray);
    }

    private static InputStream toInputStream(MimeBodyPart part) {
        byte[] bytes = null;
        try {
            bytes = IOUtils.toByteArray(part.getInputStream());

        } catch (IOException | MessagingException ex) {
            Logger.getLogger(EMailManager.class
                    .getName()).log(Level.SEVERE, "Unable to pack a file in inputstream", ex);
        }
        InputStream answer = new ByteArrayInputStream(bytes);
//        InputStream inputStream = new ByteArrayInputStream(bytes);
        //InputStream answer = new BufferedInputStream(inputStream);
        return answer;
    }

    private static List<InputStream> getAnswerListFromAttach(List<Message> messages, String filename) {
        List<InputStream> input = new ArrayList<>();

        if (messages != null) {
            for (Message message : messages) {
                try {
                    Logger.getLogger(EMailDataSource.class
                            .getName()).log(Level.INFO, "Content type: " + message.getContentType());

                    if (message.isMimeType("multipart/*") && !message.isMimeType("multipart/encrypted")) {
                        Logger.getLogger(EMailDataSource.class
                                .getName()).log(Level.INFO, "Message content type" + message.getContentType());
                        //Message msg = (MimeMessage)message;
                        Object obj = message.getContent();
                        if (obj instanceof Multipart) {
                            input = prepareAnswer(message, filename);

                        } //instanceof

                    } else {
                        Logger.getLogger(EMailDataSource.class
                                .getName()).log(Level.INFO, "Mimetype of message is not a multipart/*");

                    }
                } catch (MessagingException | IOException ex) {
                    Logger.getLogger(EMailDataSource.class
                            .getName()).log(Level.SEVERE, "Could not process the attachment!", ex);
                }
            }
        }
        return input;
    }

    private static List<InputStream> getAnswerListFromBody(List<Message> messages) {
        List<InputStream> input = new ArrayList<>();

        if (messages != null) {
            for (Message message : messages) {

                try {
                    Logger.getLogger(EMailDataSource.class
                            .getName()).log(Level.INFO, "Content type: " + message.getContentType());

                    if (message.isMimeType("text/plain")) {
                        Logger.getLogger(EMailDataSource.class
                                .getName()).log(Level.INFO, "Message content type: " + message.getContentType());
                        //Message msg = (MimeMessage)message;
                        String content = (String) message.getContent();

                        if (content.equals("") && content != null) {
                            //TODO;//input = prepareAnswer(message);
                        } //instanceof

                    } else if (message.isMimeType("message/rfc822")) {
                        Logger.getLogger(EMailDataSource.class
                                .getName()).log(Level.INFO, "Message content type: " + message.getContentType());

                    } else if (message.isMimeType("multipart/*")) {
                        Logger.getLogger(EMailDataSource.class
                                .getName()).log(Level.INFO, "Message content type" + message.getContentType());
                        //Message msg = (MimeMessage)message;
                        Object obj = message.getContent();

                        if (obj instanceof Multipart) {
                        }
                    } else {
                        Logger.getLogger(EMailDataSource.class
                                .getName()).log(Level.INFO, "");

                    }
                } //try
                catch (MessagingException | IOException ex) {
                    Logger.getLogger(EMailDataSource.class
                            .getName()).log(Level.SEVERE, "Could not process the attachment!", ex);
                }
            }
        }
        return input;
    }
}
