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

import javax.mail.Folder;
import javax.mail.Session;

/**
 *
 * @author bi
 */
public interface IEMailConnection {

    /**
     * @return inbox folder
     */
    //public Folder getFolder();
    
    /**
     * terminate email connection
     */
    public void terminate();
    
    /**
     * set email connection
     */
    public void setConnection(Session session, EMailServerParameters param);
    
    /**
     * get email folder
     */
    public Folder getFolder();
}
