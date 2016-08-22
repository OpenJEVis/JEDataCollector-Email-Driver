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

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.FromTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;
import org.jevis.api.JEVisClass;
import org.jevis.api.JEVisException;
import org.jevis.api.JEVisObject;
import org.joda.time.DateTime;

/**
 * The EMailChannelParameters class represents the settings required to search a
 * special message in the mailbox.
 *
 * @author Artur Iablokov
 */
public class EMailChannelParameters {

    private String _sender;
    private String _subject;
    private DateTime _lastReadout;
    private SearchTerm _searchTerm;
    private String _protokol;

    public EMailChannelParameters(JEVisObject channel, String protokol) {
        _protokol = protokol;
        setSearchTerms(channel);
    }

    /**
     * Set the channel attributes
     *
     * @param JEVisObject channel
     *
     */
    private void setChannelAttribute(JEVisObject channel) {

        JEVisClass channelClass;
        try {
            channelClass = channel.getJEVisClass();
        } catch (JEVisException ex) {
            Logger.getLogger(EMailChannelParameters.class.getName()).log(Level.SEVERE, "failed to get attributes for the channel.", ex);
        }

        _sender = DBHelper.getAttValue(DBHelper.RetType.STRING, channel, EMailConstants.EMailChannel.SENDER, EMailConstants.Errors.SEND_ERR, EMailConstants.DefParameters.SENDER);
        _subject = DBHelper.getAttValue(DBHelper.RetType.STRING, channel, EMailConstants.EMailChannel.SUBJECT, EMailConstants.Errors.SUBJ_ERR, EMailConstants.DefParameters.SUBJECT);
        _lastReadout = DBHelper.getAttValue(DBHelper.RetType.DATETIME, channel, EMailConstants.EMailChannel.LAST_READOUT, EMailConstants.Errors.LASTR_ERR, EMailConstants.DefParameters.LAST_READ);
    }

    /**
     * Set the terms for message search
     *
     * @param JEVisObject channel
     *
     */
    private void setSearchTerms(JEVisObject channel) {

        setChannelAttribute(channel);

        if (_lastReadout != null) {
            SearchTerm newerThan = null;
            SearchTerm subjectTerm = null;
            SearchTerm senderTerm = null;
            SearchTerm tempTerm = null;

            try {
                newerThan = new ReceivedDateTerm(ComparisonTerm.GT, _lastReadout.toDate());
            } catch (NullPointerException ex) {
                Logger.getLogger(EMailChannelParameters.class.getName()).log(Level.SEVERE, "Date term is wrong", ex);
            }

            if (!_subject.equals("")) {
                subjectTerm = new SubjectTerm(_subject);
            }

            if (!_sender.equals("")) {
                try {
                    senderTerm = new FromTerm(new InternetAddress(_sender, true));
                } catch (AddressException ex) {
                    Logger.getLogger(EMailChannelParameters.class.getName()).log(Level.SEVERE, "Sender email address is not valid.", ex);
                    senderTerm = null;
                }
            }
            if (_protokol.equalsIgnoreCase(EMailConstants.Protocol.IMAP)) {
                if (subjectTerm != null && senderTerm != null) {
                    tempTerm = new AndTerm(senderTerm, subjectTerm);
                    _searchTerm = new AndTerm(newerThan, tempTerm);
                } else if (subjectTerm != null) {
                    tempTerm = subjectTerm;
                    _searchTerm = new AndTerm(newerThan, tempTerm);
                } else if (senderTerm != null) {
                    tempTerm = senderTerm;
                    _searchTerm = new AndTerm(newerThan, tempTerm);
                } else {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Channel parameters are not valid.");
                    _searchTerm = newerThan;
                }
            } else if (_protokol.equalsIgnoreCase(EMailConstants.Protocol.POP3)){
                _searchTerm = subjectTerm;
            }
        }
    }

    /**
     * Get the search term
     *
     * @return SearchTerm
     *
     */
    public SearchTerm getSearchTerms() {
        return _searchTerm;
    }
}
