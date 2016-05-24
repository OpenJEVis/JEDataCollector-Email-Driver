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

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;

/**
 *
 * @author ai
 */
public class EMailManager {
    

    public static List<InputStream> getAnswerList(MessageFilter filter, IEMailConnection conn) {

        Folder folder = conn.getFolder();

        List<Message> messages = getMessageList(folder, filter);

        for (Message message : messages) {
            try {
                if (message.isMimeType("multipart/*")) {
                    Multipart multiPart = (Multipart) message.getContent();
                    // For all multipart contents
                    for (int i = 0; i < multiPart.getCount(); i++) {
                        MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(i);
                        // If multipart content is attachment
                        System.out.println("Name is: " + part.getFileName());
                        String disp = part.getDisposition();
                        if (Part.ATTACHMENT.equalsIgnoreCase(disp) || disp == null) {
                            System.out.println("EMail attach: " + " " + part.getFileName() + " !///! " + part.getContentType());
                        }
                    }
                }
            } catch (MessagingException | IOException ex) {
                Logger.getLogger(EMailDataSource.class.getName()).log(Level.SEVERE, "could not process the attachment!", ex);
            }
        }
        return null;
    }

    private static List<Message> getMessageList(Folder folder, MessageFilter filter) {

        List<Message> messageList = null;
        try {
            folder.open(Folder.READ_ONLY);
            messageList = Arrays.asList(folder.search(filter.getSearchTerms()));
        } catch (MessagingException ex) {
            Logger.getLogger(EMailManager.class.getName()).log(Level.SEVERE, "Unable to search for messages", ex);
        }
        return messageList;
    }

}
