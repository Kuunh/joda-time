/*
 * Joda Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2004 Stephen Colebourne.  
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:  
 *       "This product includes software developed by the
 *        Joda project (http://www.joda.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The name "Joda" must not be used to endorse or promote products
 *    derived from this software without prior written permission. For
 *    written permission, please contact licence@joda.org.
 *
 * 5. Products derived from this software may not be called "Joda",
 *    nor may "Joda" appear in their name, without prior written
 *    permission of the Joda project.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE JODA AUTHORS OR THE PROJECT
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Joda project and was originally 
 * created by Stephen Colebourne <scolebourne@joda.org>. For more
 * information on the Joda project, please see <http://www.joda.org/>.
 */
package org.joda.time.format;

import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import org.joda.time.PeriodType;

/**
 * This class is a Junit unit test for ISOPeriodFormat.
 *
 * @author Stephen Colebourne
 */
public class TestISOPeriodFormatParsing extends TestCase {

    private static final Period PERIOD = new Period(1, 2, 3, 4, 5, 6, 7, 8);
    private static final Period EMPTY_PERIOD = new Period(0, 0, 0, 0, 0, 0, 0, 0);
    private static final Period YEAR_DAY_PERIOD = new Period(1, 0, 0, 4, 5, 6, 7, 8, PeriodType.yearDayTime());
    private static final Period EMPTY_YEAR_DAY_PERIOD = new Period(0, 0, 0, 0, 0, 0, 0, 0, PeriodType.yearDayTime());
    private static final Period TIME_PERIOD = new Period(0, 0, 0, 0, 5, 6, 7, 8);
    private static final Period DATE_PERIOD = new Period(1, 2, 3, 4, 0, 0, 0, 0);

    private static final DateTimeZone PARIS = DateTimeZone.getInstance("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.getInstance("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.getInstance("Asia/Tokyo");

    long y2002days = 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 
                     366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 
                     365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
                     366 + 365;
    // 2002-06-09
    private long TEST_TIME_NOW = (y2002days + 31L + 28L + 31L + 30L + 31L + 9L - 1L) * DateTimeConstants.MILLIS_PER_DAY;

    private DateTimeZone originalDateTimeZone = null;
    private TimeZone originalTimeZone = null;
    private Locale originalLocale = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestISOPeriodFormatParsing.class);
    }

    public TestISOPeriodFormatParsing(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    protected void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
        originalDateTimeZone = null;
        originalTimeZone = null;
        originalLocale = null;
    }

    //-----------------------------------------------------------------------
    public void testParseStandard1() {
        PeriodParser parser = ISOPeriodFormat.getInstance().standard();
        Period p = parser.parsePeriod(PeriodType.standard(), "P1Y2M3W4DT5H6M7.008S");
        assertEquals(new Period(1, 2, 3, 4, 5, 6, 7, 8), p);
    }

    //-----------------------------------------------------------------------
    public void testParseStandard2() {
        PeriodParser parser = ISOPeriodFormat.getInstance().standard();
        Period p = parser.parsePeriod(PeriodType.standard(), "P0Y0M0W0DT5H6M7.008S");
        assertEquals(new Period(0, 0, 0, 0, 5, 6, 7, 8), p);
    }

    //-----------------------------------------------------------------------
    public void testParseStandard3() {
        PeriodParser parser = ISOPeriodFormat.getInstance().standard();
        Period p = parser.parsePeriod(PeriodType.standard(), "P0DT5H6M7.008S");
        assertEquals(new Period(0, 0, 0, 0, 5, 6, 7, 8), p);
    }

    //-----------------------------------------------------------------------
    public void testParseStandard4() {
        PeriodParser parser = ISOPeriodFormat.getInstance().standard();
        Period p = parser.parsePeriod(PeriodType.standard(), "P2Y3DT5H6M7.008S");
        assertEquals(new Period(2, 0, 0, 3, 5, 6, 7, 8), p);
    }

    //-----------------------------------------------------------------------
    public void testParseStandard5() {
        PeriodParser parser = ISOPeriodFormat.getInstance().standard();
        Period p = parser.parsePeriod(PeriodType.standard(), "P2YT5H6M7.008S");
        assertEquals(new Period(2, 0, 0, 0, 5, 6, 7, 8), p);
    }

    //-----------------------------------------------------------------------
    public void testParseStandard6() {
        PeriodParser parser = ISOPeriodFormat.getInstance().standard();
        Period p = parser.parsePeriod(PeriodType.standard(), "PT5H6M7.008S");
        assertEquals(new Period(0, 0, 0, 0, 5, 6, 7, 8), p);
    }

    //-----------------------------------------------------------------------
    public void testParseStandard7() {
        PeriodParser parser = ISOPeriodFormat.getInstance().standard();
        Period p = parser.parsePeriod(PeriodType.standard(), "P1Y2M3W4D");
        assertEquals(new Period(1, 2, 3, 4, 0, 0, 0, 0), p);
    }

    //-----------------------------------------------------------------------
    public void testParseStandard8() {
        PeriodParser parser = ISOPeriodFormat.getInstance().standard();
        Period p = parser.parsePeriod(PeriodType.standard(), "PT5H6M7S");
        assertEquals(new Period(0, 0, 0, 0, 5, 6, 7, 0), p);
    }

    //-----------------------------------------------------------------------
    public void testParseStandard9() {
        PeriodParser parser = ISOPeriodFormat.getInstance().standard();
        Period p = parser.parsePeriod(PeriodType.standard(), "PT0S");
        assertEquals(new Period(0, 0, 0, 0, 0, 0, 0, 0), p);
    }

    //-----------------------------------------------------------------------
    public void testParseStandard10() {
        PeriodParser parser = ISOPeriodFormat.getInstance().standard();
        Period p = parser.parsePeriod(PeriodType.standard(), "P0D");
        assertEquals(new Period(0, 0, 0, 0, 0, 0, 0, 0), p);
    }

    //-----------------------------------------------------------------------
    public void testParseStandard11() {
        PeriodParser parser = ISOPeriodFormat.getInstance().standard();
        Period p = parser.parsePeriod(PeriodType.standard(), "P0Y");
        assertEquals(new Period(0, 0, 0, 0, 0, 0, 0, 0), p);
    }

    //-----------------------------------------------------------------------
    public void testParseStandardFail1() {
        PeriodParser parser = ISOPeriodFormat.getInstance().standard();
        try {
            parser.parsePeriod(PeriodType.standard(), "P1Y2S");
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testParseStandardFail2() {
        PeriodParser parser = ISOPeriodFormat.getInstance().standard();
        try {
            parser.parsePeriod(PeriodType.standard(), "PS");
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testParseStandardFail3() {
        PeriodParser parser = ISOPeriodFormat.getInstance().standard();
        try {
            parser.parsePeriod(PeriodType.standard(), "PTS");
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testParseStandardFail4() {
        PeriodParser parser = ISOPeriodFormat.getInstance().standard();
        try {
            parser.parsePeriod(PeriodType.standard(), "PXS");
            fail();
        } catch (IllegalArgumentException ex) {}
    }

}