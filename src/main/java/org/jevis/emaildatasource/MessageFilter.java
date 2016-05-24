/*
 * Copyright (C) 2016
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

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
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
import org.jevis.api.JEVisType;
import org.jevis.commons.DatabaseHelper;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 *
 * @author ai
 */
public class MessageFilter {

    private String _sender;
    private String _subject;
    private DateTime _lastReadout;
    private SearchTerm _searchTerm;

    public MessageFilter(JEVisObject channel) {
        setSearchTerms(channel);
    }

    private void getChannelAttribute(JEVisObject channel) {

        JEVisClass channelClass;
        try {
            channelClass = channel.getJEVisClass();
        } catch (JEVisException ex) {
            Logger.getLogger(MessageFilter.class.getName()).log(Level.SEVERE, "failed to get attributes for the channel.", ex);
        }

        _sender = DBHelper.getAttValue(DBHelper.RetType.STRING, channel, EMailConstants.EMailChannel.SENDER, EMailConstants.Errors.SEND_ERR, null);
        _subject = DBHelper.getAttValue(DBHelper.RetType.STRING, channel, EMailConstants.EMailChannel.SUBJECT, EMailConstants.Errors.SUBJ_ERR, null);
        _lastReadout = DBHelper.getAttValue(DBHelper.RetType.STRING, channel, EMailConstants.EMailChannel.SENDER, EMailConstants.Errors.LASTR_ERR, null);

//            JEVisType readoutType = channelClass.getType(EMailConstants.EMailChannel.LAST_READOUT);
//            _lastReadout = DatabaseHelper.getObjectAsDate(channel, readoutType, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private void setSearchTerms(JEVisObject channel) {

        getChannelAttribute(channel);
        SearchTerm newerThan = new ReceivedDateTerm(ComparisonTerm.GT, _lastReadout.toDate());
        SearchTerm senderTerm = null;
        try {
            senderTerm = new FromTerm(new InternetAddress(_sender));
        } catch (AddressException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Email address of the sender is not valid.", e);
        }
        SearchTerm subjectTerm = new SubjectTerm(_subject);
        _searchTerm = new AndTerm(newerThan, new AndTerm(senderTerm, subjectTerm));

    }

    public SearchTerm getSearchTerms() {
        return _searchTerm;
    }
}
