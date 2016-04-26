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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jevis.api.JEVisAttribute;
import org.jevis.api.JEVisException;
import org.jevis.api.JEVisObject;

/**
 *
 * @author AI
 */
public class EMailServerParameters {

    private JEVisObject _mailObj;
    private Map _attMap;
    private EMailConstants.Protocol _protocol;
    private String _userEMail; //email adress
    private String _password;
    private String _host;
    private String _folderName;
    private Integer _connectionTimeout;
    private Integer _readTimeout;
    private Boolean _enabled;
    private String _timezone;
    private String _ssl;
    private String _authentication;

    public EMailServerParameters(JEVisObject mailObj) throws JEVisException {
        _mailObj = mailObj;
        setAllEMailParameteres();
    }

    /**
     * Sets the EMail clients protocol.
     */
    private void setProtocol() throws JEVisException {
        if (_mailObj.getJEVisClass().getName().equalsIgnoreCase(EMailConstants.EMail.IMAPEMail.NAME)) {
            _protocol = EMailConstants.Protocol.imap;
        } else if (_mailObj.getJEVisClass().getName().equalsIgnoreCase(EMailConstants.EMail.POP3EMail.NAME)) {
            _protocol = EMailConstants.Protocol.pop3;
        }
    }

    /**
     * Sets the Host.
     */
    private void setHost() throws JEVisException {
        JEVisAttribute att = _mailObj.getAttribute(EMailConstants.EMail.HOST);
        if (att == null) {
            Logger.getLogger(EMailDataSource.class.getName()).log(Level.SEVERE, "Host Attribute is missing");
        }
        if (!att.hasSample()) {
            Logger.getLogger(EMailDataSource.class.getName()).log(Level.SEVERE, "Warning Host has no samples");
            _host = EMailConstants.DefaultEMailParameters.HOST;
        }
        _host = att.getLatestSample().getValueAsString();
    }

    /**
     * Sets the password.
     */
    private void setPass() throws JEVisException {
        JEVisAttribute att = _mailObj.getAttribute(EMailConstants.EMail.PASSWORD);
        if (att == null) {
            Logger.getLogger(EMailDataSource.class.getName()).log(Level.SEVERE, "Password Attribute is missing");
        }
        if (!att.hasSample()) {
            Logger.getLogger(EMailDataSource.class.getName()).log(Level.SEVERE, "Warning Host has no samples");
            _password = EMailConstants.DefaultEMailParameters.PASSWORD;
        }
        _password = att.getLatestSample().getValueAsString();
    }

    /**
     * Sets the user email.
     */
    private void setUserEMail() throws JEVisException {
        JEVisAttribute att = _mailObj.getAttribute(EMailConstants.EMail.USER);
        if (att == null) {
            Logger.getLogger(EMailDataSource.class.getName()).log(Level.SEVERE, "User EMail Attribute is missing");
        }
        if (!att.hasSample()) {
            Logger.getLogger(EMailDataSource.class.getName()).log(Level.SEVERE, "Warning User EMail has no samples");
            _userEMail = EMailConstants.DefaultEMailParameters.USER_EMAIL;
        }
        _userEMail = att.getLatestSample().getValueAsString();
    }

    /**
     * Sets the connection timeout.
     */
    private void setConnTimeout() throws JEVisException {
        JEVisAttribute att = _mailObj.getAttribute(EMailConstants.EMail.USER);
        if (att == null) {
            Logger.getLogger(EMailDataSource.class.getName()).log(Level.SEVERE, "Connection Timeout Attribute is missing");
        }
        if (!att.hasSample()) {
            Logger.getLogger(EMailDataSource.class.getName()).log(Level.SEVERE, "Warning Connection Timeout has no samples");
            _connectionTimeout = EMailConstants.DefaultEMailParameters.CONNECTION_TIMEOUT;
        }
        _connectionTimeout = att.getLatestSample().getValueAsLong().intValue();
    }

    /**
     * Sets the read timeout.
     */
    private void setReadTimeout() throws JEVisException {
        JEVisAttribute att = _mailObj.getAttribute(EMailConstants.EMail.USER);
        if (att == null) {
            Logger.getLogger(EMailDataSource.class.getName()).log(Level.SEVERE, "Read Timeout Attribute is missing");
        }
        if (!att.hasSample()) {
            Logger.getLogger(EMailDataSource.class.getName()).log(Level.SEVERE, "Warning Read Timeout has no samples");
            _readTimeout = EMailConstants.DefaultEMailParameters.READ_TIMEOUT;
        }
        _readTimeout = att.getLatestSample().getValueAsLong().intValue();
    }

    /**
     * Sets the folder name.
     */
    private void setFolderName() throws JEVisException {
        JEVisAttribute att = _mailObj.getAttribute(EMailConstants.EMail.USER);
        if (att == null) {
            Logger.getLogger(EMailDataSource.class.getName()).log(Level.SEVERE, "Folder name Attribute is missing");
        }
        if (!att.hasSample()) {
            Logger.getLogger(EMailDataSource.class.getName()).log(Level.SEVERE, "Warning Folder name has no samples");
            _folderName = EMailConstants.DefaultEMailParameters.FOLDER_NAME;
        }
        _folderName = att.getLatestSample().getValueAsString();
    }

    /**
     * Sets the SSL.
     */
    private void setSSL() throws JEVisException {
        JEVisAttribute att = _mailObj.getAttribute(EMailConstants.EMail.USER);
        if (att == null) {
            Logger.getLogger(EMailDataSource.class.getName()).log(Level.SEVERE, "SSL Attribute is missing");
        }
        if (!att.hasSample()) {
            Logger.getLogger(EMailDataSource.class.getName()).log(Level.SEVERE, "Warning SSL has no samples");
            _ssl = EMailConstants.DefaultEMailParameters.SSL;
        }
        _ssl = att.getLatestSample().getValueAsString();
    }

    /**
     * Sets the timezone.
     */
    private void setTimezone() throws JEVisException {
        JEVisAttribute att = _mailObj.getAttribute(EMailConstants.EMail.USER);
        if (att == null) {
            Logger.getLogger(EMailDataSource.class.getName()).log(Level.SEVERE, "Timezone Attribute is missing");
        }
        if (!att.hasSample()) {
            Logger.getLogger(EMailDataSource.class.getName()).log(Level.SEVERE, "Warning Timezone has no samples");
            _timezone = EMailConstants.DefaultEMailParameters.TIMEZONE;
        }
        _timezone = att.getLatestSample().getValueAsString();
    }

    /**
     * Sets the activation state.
     */
    private void setEnabled() throws JEVisException {
        JEVisAttribute att = _mailObj.getAttribute(EMailConstants.EMail.USER);
        if (att == null) {
            Logger.getLogger(EMailDataSource.class.getName()).log(Level.SEVERE, "Enabled Attribute is missing");
        }
        if (!att.hasSample()) {
            Logger.getLogger(EMailDataSource.class.getName()).log(Level.SEVERE, "Warning Enabled has no samples");
            // defualt value ?
        }
        _enabled = att.getLatestSample().getValueAsBoolean();
    }

    /**
     * Sets the authentication.
     */
    private void setAuthentication() throws JEVisException {
        JEVisAttribute att = _mailObj.getAttribute(EMailConstants.EMail.USER);
        if (att == null) {
            Logger.getLogger(EMailDataSource.class.getName()).log(Level.SEVERE, "Read Timeout Attribute is missing");
        }
        if (!att.hasSample()) {
            Logger.getLogger(EMailDataSource.class.getName()).log(Level.SEVERE, "Warning Read Timeout has no samples");
            _authentication = EMailConstants.DefaultEMailParameters.AUTHENTICATION;
        }
        _authentication = att.getLatestSample().getValueAsString();
    }

    /**
     * Sets all email parameters.
     */
    private void setAllEMailParameteres() throws JEVisException {
        //all setter methods
    }
}
