package org.jevis.emaildatasource;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jevis.api.JEVisAttribute;
import org.jevis.api.JEVisException;
import org.jevis.api.JEVisObject;
import org.jevis.api.JEVisSample;
import org.joda.time.format.DateTimeFormat;


/*
 * Copyright (C) 2016 ai
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
/**
 *
 * @author Artur Iablokov
 */
public class DBHelper {

    /**
     * return type
     */
    public static enum RetType {
        STRING, BOOLEAN, INTEGER, DATETIME
    }

    /**
     * Get the attributes values.
     *
     * @param <T>
     * @param rtype expected return type
     * @param obj email object
     * @param attType specific attribute of an email object (see EMailConstants)
     * @param error specific error object for attribute
     * @param defValue default value of attribute
     *
     * @return T value of attribute
     */
    public static <T> T getAttValue(RetType rtype, JEVisObject obj, String attType, MailError error, T defValue) throws NullPointerException {
        try {
            JEVisAttribute att = obj.getAttribute(attType);
            if (att == null) {
                Logger.getLogger(EMailDataSource.class.getName()).log(error.getLevel(), "{0} Attribute is missing", error.getMessage());
            }
            if (!att.hasSample()) {
                Logger.getLogger(EMailDataSource.class.getName()).log(error.getLevel(), "Warning! {0} has no samples", error.getMessage());
                return getDefValue(defValue, error);
//                if (defValue != null) {
//                    return (T) defValue;
//                } else {
//                    throw new NullPointerException(error.getMessage() + " is empty");
//                }
            } else {
                System.out.println("Sample count: " + att.getSampleCount());
                for (JEVisSample s : att.getAllSamples()) {
                    System.out.println("s: " + s.getTimestamp() + "=" + s.getValue());
                }
            }

            JEVisSample lastS = att.getLatestSample();

            switch (rtype) {
                case STRING:
                    try {
                        if (lastS.getValueAsString().equals("")) {
                            Logger.getLogger(EMailDataSource.class.getName()).log(error.getLevel(), "Warning! {0} has empty string", error.getMessage());
                            return getDefValue(defValue, error);
                        } else {
                            return (T) lastS.getValueAsString();
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(EMailDataSource.class.getName()).log(error.getLevel(), "Attribute {0}: failed to get the value", error.getMessage());
                        throw new NullPointerException();
                    }
                case INTEGER:
                    try {
                        Long longValue = lastS.getValueAsLong();
                        return (T) new Integer(longValue.intValue());
                    } catch (Exception ex) {
                        Logger.getLogger(EMailDataSource.class.getName()).log(error.getLevel(), "Attribute {0}: failed to get the value", error.getMessage());
                        return getDefValue(defValue, error);
                    }
                case BOOLEAN:
                    try {
                        return (T) lastS.getValueAsBoolean();
                    } catch (Exception ex) {
                        Logger.getLogger(EMailDataSource.class.getName()).log(error.getLevel(), "Attribute {0}: failed to get the value. ", error.getMessage());
                        throw new NullPointerException();
                    }
                case DATETIME:
                    try {
                        String value = lastS.getValueAsString();
                        return (T) DateTimeFormat.forPattern(EMailConstants.ValidValues.TIMEFORMAT).parseDateTime(value);
                    } catch (JEVisException ex) {
                        Logger.getLogger(DBHelper.class.getName()).log(error.getLevel(), "Attribute {0}: failed to get the value. ", error.getMessage());
                    }

                default:
                    Logger.getLogger(EMailDataSource.class.getName()).log(error.getLevel(), "Attribute {0}: return type is wrong or unknown.", error.getMessage());
                    throw new NullPointerException();
            }

        } catch (JEVisException jex) {
            throw new NullPointerException();
        }
    }

    private static <T> T getDefValue(T defValue, MailError error) {
        if (defValue != null) {
            return (T) defValue;
        } else {
            throw new NullPointerException(error.getMessage() + " is empty");
        }
    }
}
