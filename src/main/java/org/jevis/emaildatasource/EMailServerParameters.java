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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jevis.api.JEVisException;
import org.jevis.api.JEVisObject;
import static org.jevis.emaildatasource.DBHelper.RetType.*;

/**
 *
 * @author AI
 */
public class EMailServerParameters {

    private final JEVisObject _mailObj;
    private String _protocol;
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

    public EMailServerParameters(JEVisObject mailObj) throws Exception {
        _mailObj = mailObj;
        try {
            setAllEMailParameteres();
        } catch (JEVisException ex) {
            Logger.getLogger(EMailServerParameters.class.getName()).log(Level.SEVERE, "Failed to get server settings", ex);
        }
    }

    /**
     * Sets all email parameters.
     */
    private void setAllEMailParameteres() throws JEVisException {
        _protocol = setProtocol();
        _host = DBHelper.getAttValue(STRING, _mailObj, EMailConstants.EMail.HOST, EMailConstants.Errors.HOST_ERR, null);
        _userEMail = DBHelper.getAttValue(STRING, _mailObj, EMailConstants.EMail.USER, EMailConstants.Errors.USER_ERR, null);
        _password = DBHelper.getAttValue(STRING, _mailObj, EMailConstants.EMail.PASSWORD, EMailConstants.Errors.PASS_ERR, null);
        _folderName = DBHelper.getAttValue(STRING, _mailObj, EMailConstants.EMail.FOLDER, EMailConstants.Errors.FOLD_ERR, EMailConstants.DefParameters.FOLDER_NAME);
        _authentication = DBHelper.getAttValue(STRING, _mailObj, EMailConstants.EMail.PASSWORD, EMailConstants.Errors.PASS_ERR, null);
        _ssl = DBHelper.getAttValue(STRING, _mailObj, EMailConstants.EMail.SSL, EMailConstants.Errors.SSL_ERR, null);
        _timezone = DBHelper.getAttValue(STRING, _mailObj, EMailConstants.EMail.TIMEZONE, EMailConstants.Errors.TIMEZ_ERR, EMailConstants.DefParameters.TIMEZONE);
        _readTimeout = DBHelper.getAttValue(INTEGER, _mailObj, EMailConstants.EMail.READ_TIMEOUT, EMailConstants.Errors.READ_ERR, EMailConstants.DefParameters.READ_TIMEOUT);
        _connectionTimeout = DBHelper.getAttValue(INTEGER, _mailObj, EMailConstants.EMail.CONNECTION_TIMEOUT, EMailConstants.Errors.CONN_ERR, EMailConstants.DefParameters.CONNECTION_TIMEOUT);
        _enabled = DBHelper.getAttValue(BOOLEAN, _mailObj, EMailConstants.EMail.ENABLE, EMailConstants.Errors.ENAB_ERR, EMailConstants.DefParameters.ENABLE);
    }

    /**
     * Sets the EMail clients protocol.
     */
    private String setProtocol() throws JEVisException {
        if (_mailObj.getJEVisClass().getName().equalsIgnoreCase(EMailConstants.EMail.IMAPEMail.NAME)) {
            return EMailConstants.Protocol.IMAP;
        } else if (_mailObj.getJEVisClass().getName().equalsIgnoreCase(EMailConstants.EMail.POP3EMail.NAME)) {
            return EMailConstants.Protocol.POP3;
        } else {
            Logger.getLogger(EMailDataSource.class.getName()).log(Level.SEVERE, "EMail protocol is not received");
            throw new NullPointerException();
        }
    } 

    public String getProtocol() {
        return _protocol;
    }

    public String getUserEMail() {
        return _userEMail;
    }

    public String getPassword() {
        return _password;
    }

    public String getHost() {
        return _host;
    }

    public String getFolderName() {
        return _folderName;
    }

    public Integer getConnectionTimeout() {
        return _connectionTimeout;
    }

    public Integer getReadTimeout() {
        return _readTimeout;
    }

    public Boolean getEnabled() {
        return _enabled;
    }

    public String getTimezone() {
        return _timezone;
    }

    public String getSsl() {
        return _ssl;
    }

    public String getAuthentication() {
        return _authentication;
    }
}
