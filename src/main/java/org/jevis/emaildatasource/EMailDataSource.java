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
import org.jevis.commons.driver.Result;

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

            try {
                _result = new ArrayList<>();

                JEVisClass parserJevisClass = channel.getDataSource().getJEVisClass(DataCollectorTypes.Parser.NAME);
                JEVisObject parser = channel.getChildren(parserJevisClass, true).get(0);

//                _parser = ParserFactory.getParser(parser);
//                _parser.initialize(parser);
                List<InputStream> input = this.sendSampleRequest(channel);

                if (!input.isEmpty()) {
                    _parser.parse(input);
                    _result = _parser.getResult();
                }

                if (!_result.isEmpty()) {
                    JEVisImporterAdapter.importResults(_result, _importer, channel);
                }
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(EMailDataSource.class.getName()).log(java.util.logging.Level.SEVERE, "EMail Driver execution can not continue.", ex);
            }
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

        _eMailConnection = EMailManager.createConnection(_eMailServerParameters);
        _channelParameters = new EMailChannelParameters(channel);
        answerList = EMailManager.getAnswerList(_channelParameters, _eMailConnection);
        EMailManager.terminate(_eMailConnection);
        return answerList;
    }

    @Override
    public void parse(List<InputStream> input
    ) {
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
