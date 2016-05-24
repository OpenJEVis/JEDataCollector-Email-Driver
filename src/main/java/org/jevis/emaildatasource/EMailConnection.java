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

import com.sun.xml.internal.fastinfoset.algorithm.IEEE754FloatingPointEncodingAlgorithm;
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
import org.jevis.api.JEVisException;

/**
 *
 * @author Artur Iablokov
 */
public class EMailConnection {

    private EMailServerParameters _parameters;
    private Store _store;
    private Folder _folder;
    private Session _session;

//    /**
//     *
//     * @param parameters parameters for
//     */
//    public EMailConnection(EMailServerParameters parameters) {
//        _parameters = parameters;
//        setConnection();
//    }
    public static IEMailConnection setConnection(EMailServerParameters parameters) {
        
        Properties props = createProperties(parameters);
        Session session = Session.getInstance(props);
        if (parameters.getProtocol().equalsIgnoreCase(EMailConstants.Protocol.IMAP)) {         
            return new IMAPConnection(session);
        }
        else if(parameters.getProtocol().equalsIgnoreCase(EMailConstants.Protocol.IMAP)){
            return new POP3Connection(parameters);
        }
        else{
            Logger.getLogger(EMailConnection.class.getName()).log(Level.SEVERE, "EMail Connection failed");
            return null; //!!!!!!!!!!
        }
    }
 
    
    public static void terminate(IEMailConnection conn) {
        conn.terminate();
    }

    private Properties createProperties(EMailServerParameters parameters) {

        Properties props = new Properties();
        String key = "mail." + parameters.getProtocol();
        props.put(key + ".host", parameters.getHost());
        props.put(key + ".port", parameters.getPort());
        props.put(key + ".connectiontimeout", parameters.getConnectionTimeout() * 1000);
        props.put(key + ".timeout", parameters.getReadTimeout() * 1000);

        String ssl = parameters.getSsl();
        if (ssl.equals(EMailConstants.ValidValues.CryptProtocols.SSL_TLS)) {
            props.put(key + ".ssl.enable", true);
        } else if (ssl.equals(EMailConstants.ValidValues.CryptProtocols.STARTTLS)) {
            props.put(key + "starttls.enable", true);
        }

        //_parameters.getAuthentication() usually not used in SSL connections
//        props.put("mail.debug", "true");
//        props.put("mail.store.protocol", "imaps");
        return props;
    }
}
