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

import org.jevis.commons.driver.DataCollectorTypes;

/**
 *
 * @author bi
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
    }

    public enum Protocol {
        imap, pop3
    }

    interface DefaultEMailParameters {

        public final static String FOLDER_NAME = "INBOX";
        public final static String USER_EMAIL = "";
        public final static String HOST = ""; 
        public final static String PASSWORD = "";
        public final static String AUTHENTICATION = "";
        public final static String SSL = "";
        public final static String TIMEZONE = "UTC";
        public final static int PORT = 223;
        public final static int READ_TIMEOUT = 300;
        public final static int CONNECTION_TIMEOUT = 300;
    }
}
