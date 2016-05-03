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
import org.jevis.api.JEVisAttribute;
import org.jevis.api.JEVisException;
import org.jevis.api.JEVisObject;
import org.jevis.api.JEVisSample;

/**
 *
 * @author AI
 */
public class EMailServerParameters {

    private final JEVisObject _mailObj;
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
     * Sets all email parameters.
     */
    private void setAllEMailParameteres() throws JEVisException {
        setProtocol();
        _host = helper(_host, _mailObj, EMailConstants.EMail.HOST, EMailConstants.Errors.HOST_ERR, null);
        _userEMail = helper(_userEMail, _mailObj, EMailConstants.EMail.USER, EMailConstants.Errors.USER_ERR, null);
        _password = helper(_password, _mailObj, EMailConstants.EMail.PASSWORD, EMailConstants.Errors.PASS_ERR, null);
        _folderName= helper(_folderName, _mailObj, EMailConstants.EMail.FOLDER, EMailConstants.Errors.FOLD_ERR, EMailConstants.DefParameters.FOLDER_NAME);
        _authentication= helper(_password, _mailObj, EMailConstants.EMail.PASSWORD, EMailConstants.Errors.PASS_ERR, null);
        _ssl= helper(_ssl, _mailObj, EMailConstants.EMail.SSL, EMailConstants.Errors.SSL_ERR, null);
        _timezone= helper(_timezone, _mailObj, EMailConstants.EMail.TIMEZONE, EMailConstants.Errors.TIMEZ_ERR, EMailConstants.DefParameters.TIMEZONE);
        _readTimeout = helper(_readTimeout, _mailObj, EMailConstants.EMail.READ_TIMEOUT, EMailConstants.Errors.READ_ERR, EMailConstants.DefParameters.READ_TIMEOUT);
        _connectionTimeout = helper(_connectionTimeout, _mailObj, EMailConstants.EMail.CONNECTION_TIMEOUT, EMailConstants.Errors.CONN_ERR, EMailConstants.DefParameters.CONNECTION_TIMEOUT);
        _enabled= helper(_enabled, _mailObj, EMailConstants.EMail.ENABLE, EMailConstants.Errors.ENAB_ERR, EMailConstants.DefParameters.ENABLE);
    }

    /**
     * Sets the EMail clients protocol.
     */
    private void setProtocol() throws JEVisException {
        if (_mailObj.getJEVisClass().getName().equalsIgnoreCase(EMailConstants.EMail.IMAPEMail.NAME)) {
            _protocol = EMailConstants.Protocol.imap;
        } else if (_mailObj.getJEVisClass().getName().equalsIgnoreCase(EMailConstants.EMail.POP3EMail.NAME)) {
            _protocol = EMailConstants.Protocol.pop3;
        } else {
            Logger.getLogger(EMailDataSource.class.getName()).log(Level.SEVERE, "EMail protocol is not received");
            throw new NullPointerException();
        }
    }

    /**
     * Sets the attributes values.
     */
    private <T> T helper(T t, JEVisObject obj, String attType, MailError error, T defaultValue) throws NullPointerException {
        try {
            JEVisAttribute att = obj.getAttribute(attType);
            if (att == null) {
                Logger.getLogger(EMailDataSource.class.getName()).log(error.getLevel(),
                        error.getMessage() + " Attribute is missing");
            }
            if (!att.hasSample()) {
                Logger.getLogger(EMailDataSource.class.getName()).log(error.getLevel(), "Warning " + error.getMessage() + " has no samples");
                if (defaultValue != null) {
                    return (T) defaultValue;
                } else {
                    throw new NullPointerException(error.getMessage() + " is empty");
                }
            }

            JEVisSample lastS = att.getLatestSample();

            if (t instanceof String) {
                try {
                    return (T) lastS.getValueAsString();
                } catch (Exception ex) {
                    //fehlermeldung
                    throw new NullPointerException();
                }
            } else if (t instanceof Integer) {
                try {
                    Long longValue = lastS.getValueAsLong();
                    return (T) new Integer(longValue.intValue());
                } catch (Exception ex) {
                    //fehlermeldung
                    throw new NullPointerException();
                }
            } else if (t instanceof Boolean) {
                try {
                    return (T) lastS.getValueAsBoolean();
                } catch (Exception ex) {
                    //fehlermeldung
                    throw new NullPointerException();
                }
            } else {
                throw new NullPointerException();
                //fehlermeldung
            }
        } catch (JEVisException jex) {
            //doSomething
            throw new NullPointerException();
        }
    }

    public EMailConstants.Protocol getProtocol() {
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
