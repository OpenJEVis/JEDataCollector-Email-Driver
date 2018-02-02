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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.jevis.api.JEVisObject;
import org.jevis.commons.driver.DataSource;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jevis.api.JEVisClass;
import org.jevis.commons.driver.DataCollectorTypes;
import org.jevis.commons.driver.Importer;
import org.jevis.commons.driver.ImporterFactory;
import org.jevis.commons.driver.JEVisImporterAdapter;
import org.jevis.commons.driver.Parser;
import org.jevis.commons.driver.ParserFactory;
import org.jevis.commons.driver.Result;
import org.joda.time.DateTimeZone;

public class EMailDataSource implements DataSource {

    private List<JEVisObject> _channels;
    private JEVisObject _dataSource;
    private Importer _importer;
    private Parser _parser;
    private List<Result> _result;
    private EMailServerParameters _eMailServerParameters;
    private EMailConnection _eMailConnection;
    private EMailChannelParameters _channelParameters;

    @Override
    public void run() {

        for (JEVisObject channel : _channels) {
            //mess
            final long timeStart = System.currentTimeMillis();
            //
            try {
                _result = new ArrayList<>();

                JEVisClass parserJevisClass = channel.getDataSource().getJEVisClass(DataCollectorTypes.Parser.NAME);
                JEVisObject parser = channel.getChildren(parserJevisClass, true).get(0);

                _parser = ParserFactory.getParser(parser);
                Logger.getLogger(EMailDataSource.class.getName()).log(Level.INFO, "parser to string: {0}", _parser.toString());
                _parser.initialize(parser);
//                //measurment
//                final long timeAfterInit = System.currentTimeMillis();
//                Logger.getLogger(EMailDataSource.class.getName()).log(Level.INFO, "Execution time for channel " + channel.getName() + ". Init done in: " + (timeAfterInit - timeStart) + " Millisek.");
//                //

                Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
                List<InputStream> input = this.sendSampleRequest(channel);
//                //measurment
//                final long timeSendReques = System.currentTimeMillis();
//                Logger.getLogger(EMailDataSource.class.getName()).log(Level.INFO, "Execution time for channel " + channel.getName() + ". Response received in: " + (timeSendReques - timeStart) + " Millisek.");
//                //

                Logger.getLogger(EMailDataSource.class.getName()).log(Level.INFO, "Answerlist is empty: {0}", input.isEmpty());
                Logger.getLogger(EMailDataSource.class.getName()).log(Level.INFO, "Answerlist size: " + input.size());

                if (!input.isEmpty()) {
                    _parser.parse(input, DateTimeZone.forID(_eMailServerParameters.getTimezone()));
                    _result = _parser.getResult();
                }

                if (!_result.isEmpty()) {
                    try {
                        JEVisImporterAdapter.importResults(_result, _importer, channel);
                    } catch (NullPointerException np) {
                        Logger.getLogger(EMailDataSource.class.getName()).log(Level.SEVERE,"File is wrong or parse failed");
                    }
                    
                    DBHelper.setLastReadout(_result, channel);        
                }
                
                
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(EMailDataSource.class.getName()).log(java.util.logging.Level.SEVERE, "EMail Driver execution can not continue.", ex);
            }
            final long timeEnd = System.currentTimeMillis();
            Logger.getLogger(EMailDataSource.class.getName()).log(Level.INFO, "-----------"+channel.getName() + "execution time is: " + (timeEnd - timeStart) + " Millisek." +"-----------");
        }
    }

    @Override
    public void initialize(JEVisObject mailObject) {
        _dataSource = mailObject;
        initializeAttributes(mailObject);
        initializeChannelObjects(mailObject);

        _importer = ImporterFactory.getImporter(_dataSource);
        if (_importer != null) {
            _importer.initialize(_dataSource);
        }
    }

    @Override
    public List<InputStream> sendSampleRequest(JEVisObject channel) {
        List<InputStream> answerList = new ArrayList<>();
        final long start = System.currentTimeMillis();
        _eMailConnection = EMailManager.createConnection(_eMailServerParameters);
        //measurment
        final long connectDone = System.currentTimeMillis();
        Logger.getLogger(EMailDataSource.class.getName()).log(Level.INFO, "Send sample request. Connection parameters and connection: " + (connectDone - start) + " Millisek.");
        //
        _channelParameters = new EMailChannelParameters(channel, _eMailServerParameters.getProtocol());
        //measurment
        final long channelDone = System.currentTimeMillis();
        Logger.getLogger(EMailDataSource.class.getName()).log(Level.INFO, "Send sample request. Channel parameters: " + (channelDone - start) + " Millisek.");
        //
        answerList = EMailManager.getAnswerList(_channelParameters, _eMailConnection);
        
        EMailManager.terminate(_eMailConnection);
        //measurment
        final long timeTotalSendreq = System.currentTimeMillis();
        Logger.getLogger(EMailDataSource.class.getName()).log(Level.INFO, "Send sample request. Time Total: " + (timeTotalSendreq - start) + " Millisek.");
        //
        return answerList;
    }

    @Override
    public void parse(List<InputStream> input) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void importResult() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void initializeAttributes(JEVisObject mailObject) {
        try {
            _eMailServerParameters = new EMailServerParameters(mailObject);
        } catch (Exception ex) {
            Logger.getLogger(EMailDataSource.class.getName()).log(Level.SEVERE, "Server settings are incorrect or missing.", ex);
        }
    }

    private void initializeChannelObjects(JEVisObject mailObject) {
        try {
            JEVisClass channelDirClass = mailObject.getDataSource().getJEVisClass(EMailConstants.EMailChannelDirectory.NAME);
            JEVisObject channelDir = mailObject.getChildren(channelDirClass, false).get(0);
            JEVisClass channelClass = mailObject.getDataSource().getJEVisClass(EMailConstants.EMailChannel.NAME);
            _channels = channelDir.getChildren(channelClass, false);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(EMailDataSource.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
}
