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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.jevis.commons.driver.DataCollectorTypes;
import org.joda.time.DateTime;

/**
 * The EMailConstants class is a container for the constants.
 *
 * @author Artur Iablokov
 */
public interface EMailConstants {

    interface EMailChannelDirectory extends DataCollectorTypes.ChannelDirectory {

        public final static String NAME = "EMail Channel Directory";
    }

    interface EMail {

        public final static String NAME = "EMail Server";
        public final static String TIMEZONE = "Timezone";
        public final static String ENABLE = "Enabled";

        public final static String CONNECTION_TIMEOUT = "Connection Timeout";
        public final static String READ_TIMEOUT = "Read Timeout";
        public final static String HOST = "Host";
        public final static String PORT = "Port";

        public final static String PASSWORD = "Password";
        public final static String USER = "User";
        public final static String AUTHENTICATION = "Authentication";
        public final static String SSL = "SSL";
        public final static String FOLDER = "Folder";

        interface POP3EMail extends EMail {

            public final static String NAME = "POP3 EMail Server";
        }

        interface IMAPEMail extends EMail {

            public final static String NAME = "IMAP EMail Server";
        }
    }

    interface EMailChannel extends DataCollectorTypes.Channel {

        public final static String NAME = "EMail Channel";
        public final static String LAST_READOUT = "Last Readout";
        public final static String SUBJECT = "Subject";
        public final static String SENDER = "Sender";
        public final static String INBODY = "Data in body";
        public final static String FILENAME = "Filename";
    }

    interface Protocol {

        public final static String POP3 = "pop3";
        public final static String IMAP = "imap";
    }

    interface Errors {

        public final static MailError HOST_ERR = new MailError("Host", "536350", Level.SEVERE);
        public final static MailError USER_ERR = new MailError("User EMail", "536351", Level.SEVERE);
        public final static MailError PASS_ERR = new MailError("Paasword", "536352", Level.SEVERE);
        public final static MailError FOLD_ERR = new MailError("Folder name", "536353", Level.WARNING);
        public final static MailError AUTH_ERR = new MailError("Authentication", "536354", Level.WARNING);
        public final static MailError READ_ERR = new MailError("Read timeout", "536355", Level.WARNING);
        public final static MailError CONN_ERR = new MailError("Connection timeout", "536356", Level.WARNING);
        public final static MailError SSL_ERR = new MailError("SSL", "536357", Level.SEVERE);
        public final static MailError TIMEZ_ERR = new MailError("Timezone", "536358", Level.WARNING);
        public final static MailError PORT_ERR = new MailError("Port", "536359", Level.WARNING);
        public final static MailError ENAB_ERR = new MailError("Enable", "536360", Level.SEVERE);
        public final static MailError SEND_ERR = new MailError("Sender", "536361", Level.WARNING);
        public final static MailError SUBJ_ERR = new MailError("Subject", "536362", Level.WARNING);
        public final static MailError LASTR_ERR = new MailError("Last readout", "536363", Level.SEVERE);
        public final static MailError FILENAME_ERR = new MailError("File name", "536364", Level.WARNING);
        public final static MailError BODY_ERR = new MailError("Parse body", "536365", Level.SEVERE);
    }

    interface DefParameters {

        // EMail server parameters
        public final static String FOLDER_NAME = "INBOX";
        public final static String USER_EMAIL = "";
        public final static String HOST = "";
        public final static String PASSWORD = "";
        public final static String AUTHENTICATION = "";
        public final static String SSL = "";
        public final static String TIMEZONE = "UTC";
        public final static int READ_TIMEOUT = 300;
        public final static int CONNECTION_TIMEOUT = 300;
        public final static Boolean ENABLE = false;
        public final static int PORT = -1;
        // EMail Channel parameters
        public final static String SUBJECT = "";
        public final static String SENDER = "";
        public final static DateTime LAST_READ = new DateTime(2010, 1, 1, 0, 0, 0);  //2010-01-01 00:00:00        
        public final static Boolean INBODY = false;
        public final static String FILENAME = "";
    }

    interface ValidValues {

        public final static String TIMEFORMAT = "yyyy-MM-dd HH:mm:ss";

        interface CryptProtocols {

            public final static String SSL_TLS = "SSL/TLS";
            public final static String STARTTLS = "STARTTLS";
        }
    }
}
