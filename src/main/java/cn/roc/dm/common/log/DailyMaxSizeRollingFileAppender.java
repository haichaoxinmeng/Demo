/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package cn.roc.dm.common.log;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.helpers.CountingQuietWriter;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.LoggingEvent;

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 DailyMaxSizeRollingFileAppender extends {@link FileAppender} so that the
 underlying file is rolled over at a user chosen frequency.

 DailyMaxSizeRollingFileAppender has been observed to exhibit
 synchronization issues and data loss.  The log4j extras
 companion includes alternatives which should be considered
 for new deployments and which are discussed in the documentation
 for org.apache.log4j.rolling.RollingFileAppender.

 <p>The rolling schedule is specified by the <b>DatePattern</b>
 option. This pattern should follow the {@link SimpleDateFormat}
 conventions. In particular, you <em>must</em> escape literal text
 within a pair of single quotes. A formatted version of the date
 pattern is used as the suffix for the rolled file name.

 <p>For example, if the <b>File</b> option is set to
 <code>/foo/bar.log</code> and the <b>DatePattern</b> set to
 <code>'.'yyyy-MM-dd</code>, on 2001-02-16 at midnight, the logging
 file <code>/foo/bar.log</code> will be copied to
 <code>/foo/bar.log.2001-02-16</code> and logging for 2001-02-17
 will continue in <code>/foo/bar.log</code> until it rolls over
 the next day.

 <p>Is is possible to specify monthly, weekly, half-daily, daily,
 hourly, or minutely rollover schedules.

 <p><table border="1" cellpadding="2">
 <tr>
 <th>DatePattern</th>
 <th>Rollover schedule</th>
 <th>Example</th>

 <tr>
 <td><code>'.'yyyy-MM</code>
 <td>Rollover at the beginning of each month</td>

 <td>At midnight of May 31st, 2002 <code>/foo/bar.log</code> will be
 copied to <code>/foo/bar.log.2002-05</code>. Logging for the month
 of June will be output to <code>/foo/bar.log</code> until it is
 also rolled over the next month.

 <tr>
 <td><code>'.'yyyy-ww</code>

 <td>Rollover at the first day of each week. The first day of the
 week depends on the locale.</td>

 <td>Assuming the first day of the week is Sunday, on Saturday
 midnight, June 9th 2002, the file <i>/foo/bar.log</i> will be
 copied to <i>/foo/bar.log.2002-23</i>.  Logging for the 24th week
 of 2002 will be output to <code>/foo/bar.log</code> until it is
 rolled over the next week.

 <tr>
 <td><code>'.'yyyy-MM-dd</code>

 <td>Rollover at midnight each day.</td>

 <td>At midnight, on March 8th, 2002, <code>/foo/bar.log</code> will
 be copied to <code>/foo/bar.log.2002-03-08</code>. Logging for the
 9th day of March will be output to <code>/foo/bar.log</code> until
 it is rolled over the next day.

 <tr>
 <td><code>'.'yyyy-MM-dd-a</code>

 <td>Rollover at midnight and midday of each day.</td>

 <td>At noon, on March 9th, 2002, <code>/foo/bar.log</code> will be
 copied to <code>/foo/bar.log.2002-03-09-AM</code>. Logging for the
 afternoon of the 9th will be output to <code>/foo/bar.log</code>
 until it is rolled over at midnight.

 <tr>
 <td><code>'.'yyyy-MM-dd-HH</code>

 <td>Rollover at the top of every hour.</td>

 <td>At approximately 11:00.000 o'clock on March 9th, 2002,
 <code>/foo/bar.log</code> will be copied to
 <code>/foo/bar.log.2002-03-09-10</code>. Logging for the 11th hour
 of the 9th of March will be output to <code>/foo/bar.log</code>
 until it is rolled over at the beginning of the next hour.


 <tr>
 <td><code>'.'yyyy-MM-dd-HH-mm</code>

 <td>Rollover at the beginning of every minute.</td>

 <td>At approximately 11:23,000, on March 9th, 2001,
 <code>/foo/bar.log</code> will be copied to
 <code>/foo/bar.log.2001-03-09-10-22</code>. Logging for the minute
 of 11:23 (9th of March) will be output to
 <code>/foo/bar.log</code> until it is rolled over the next minute.

 </table>

 <p>Do not use the colon ":" character in anywhere in the
 <b>DatePattern</b> option. The text before the colon is interpeted
 as the protocol specificaion of a URL which is probably not what
 you want.


 @author Eirik Lygre
 @author Ceki G&uuml;lc&uuml;*/
//roy_modify 文件按时间备份，同时文件太大的话可以换自动按文件大小拆分文件。
public class DailyMaxSizeRollingFileAppender extends FileAppender {

    /**
     The default maximum file size is 10MB.
     */
    //roy_modify 添加属性
    protected long maxFileSize = 10*1024*1024;

    //add by wlf
    private long maxBackupIndex = 0;

    //roy_modify 添加属性
    private long nextRollover = 0;

	//roy_modify 添加方法
    public void setMaxFileSize(String value) {
        maxFileSize = OptionConverter.toFileSize(value, maxFileSize + 1);
    }


    //roy_modify 添加方法
    protected
    void setQWForFiles(Writer writer) {
        this.qw = new CountingQuietWriter(writer, errorHandler);
    }

    //roy_modify 添加方法
    public
    synchronized
    void setFile(String fileName, boolean append, boolean bufferedIO, int bufferSize)
            throws IOException {
        super.setFile(fileName, append, this.bufferedIO, this.bufferSize);
        if(append) {
            File f = new File(fileName);
            ((CountingQuietWriter) qw).setCount(f.length());
        }
    }

    // The code assumes that the following constants are in a increasing
    // sequence.
    static final int TOP_OF_TROUBLE=-1;
    static final int TOP_OF_MINUTE = 0;
    static final int TOP_OF_HOUR   = 1;
    static final int HALF_DAY      = 2;
    static final int TOP_OF_DAY    = 3;
    static final int TOP_OF_WEEK   = 4;
    static final int TOP_OF_MONTH  = 5;


    /**
     The date pattern. By default, the pattern is set to
     "'.'yyyy-MM-dd" meaning daily rollover.
     */
    private String datePattern = "'.'yyyy-MM-dd";

    /**
     The log file will be renamed to the value of the
     scheduledFilename variable when the next interval is entered. For
     example, if the rollover period is one hour, the log file will be
     renamed to the value of "scheduledFilename" at the beginning of
     the next hour.

     The precise time when a rollover occurs depends on logging
     activity.
     */
    private String scheduledFilename;

    /**
     The next time we estimate a rollover should occur. */
    private long nextCheck = System.currentTimeMillis () - 1;

    Date now = new Date();

    SimpleDateFormat sdf;

    RollingCalendar rpc = new RollingCalendar();

    int checkPeriod = TOP_OF_TROUBLE;

    // The gmtTimeZone is used only in computeCheckPeriod() method.
    static final TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");


    /**
     The default constructor does nothing. */
    public DailyMaxSizeRollingFileAppender() {
    }

    /**
     Instantiate a <code>DailyMaxSizeRollingFileAppender</code> and open the
     file designated by <code>filename</code>. The opened filename will
     become the ouput destination for this appender.

     */
    public DailyMaxSizeRollingFileAppender(Layout layout, String filename,
                                           String datePattern) throws IOException {
        super(layout, filename, true);
        this.datePattern = datePattern;
        activateOptions();
    }

    /**
     The <b>DatePattern</b> takes a string in the same format as
     expected by {@link SimpleDateFormat}. This options determines the
     rollover schedule.
     */
    public void setDatePattern(String pattern) {
        datePattern = pattern;
    }


    public void setMaxBackupIndex(long maxBackupIndex) {
        this.maxBackupIndex = maxBackupIndex;
    }

    /** Returns the value of the <b>DatePattern</b> option. */
    public String getDatePattern() {
        return datePattern;
    }

    public void activateOptions() {
        super.activateOptions();
        if(datePattern != null && fileName != null) {
            now.setTime(System.currentTimeMillis());
            sdf = new SimpleDateFormat(datePattern);
            int type = computeCheckPeriod();
            printPeriodicity(type);
            rpc.setType(type);
            File file = new File(fileName);
            scheduledFilename = fileName+sdf.format(new Date(file.lastModified()));

        } else {
            LogLog.error("Either File or DatePattern options are not set for appender ["
                    +name+"].");
        }
    }

    void printPeriodicity(int type) {
        switch(type) {
            case TOP_OF_MINUTE:
                LogLog.debug("Appender ["+name+"] to be rolled every minute.");
                break;
            case TOP_OF_HOUR:
                LogLog.debug("Appender ["+name
                        +"] to be rolled on top of every hour.");
                break;
            case HALF_DAY:
                LogLog.debug("Appender ["+name
                        +"] to be rolled at midday and midnight.");
                break;
            case TOP_OF_DAY:
                LogLog.debug("Appender ["+name
                        +"] to be rolled at midnight.");
                break;
            case TOP_OF_WEEK:
                LogLog.debug("Appender ["+name
                        +"] to be rolled at start of week.");
                break;
            case TOP_OF_MONTH:
                LogLog.debug("Appender ["+name
                        +"] to be rolled at start of every month.");
                break;
            default:
                LogLog.warn("Unknown periodicity for appender ["+name+"].");
        }
    }


    // This method computes the roll over period by looping over the
    // periods, starting with the shortest, and stopping when the r0 is
    // different from from r1, where r0 is the epoch formatted according
    // the datePattern (supplied by the user) and r1 is the
    // epoch+nextMillis(i) formatted according to datePattern. All date
    // formatting is done in GMT and not local format because the test
    // logic is based on comparisons relative to 1970-01-01 00:00:00
    // GMT (the epoch).

    int computeCheckPeriod() {
        RollingCalendar rollingCalendar = new RollingCalendar(gmtTimeZone, Locale.getDefault());
        // set sate to 1970-01-01 00:00:00 GMT
        Date epoch = new Date(0);
        if(datePattern != null) {
            for(int i = TOP_OF_MINUTE; i <= TOP_OF_MONTH; i++) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
                simpleDateFormat.setTimeZone(gmtTimeZone); // do all date formatting in GMT
                String r0 = simpleDateFormat.format(epoch);
                rollingCalendar.setType(i);
                Date next = new Date(rollingCalendar.getNextCheckMillis(epoch));
                String r1 =  simpleDateFormat.format(next);
                //System.out.println("Type = "+i+", r0 = "+r0+", r1 = "+r1);
                if(r0 != null && r1 != null && !r0.equals(r1)) {
                    return i;
                }
            }
        }
        return TOP_OF_TROUBLE; // Deliberately head for trouble...
    }

    /**
     Rollover the current file to a new file.
     */
    void rollOver() throws IOException {

    /* Compute filename, but only if datePattern is specified */
        if (datePattern == null) {
            errorHandler.error("Missing DatePattern option in rollOver().");
            return;
        }

        String datedFilename = fileName+sdf.format(now);
        // It is too early to roll over because we are still within the
        // bounds of the current interval. Rollover will occur once the
        // next interval is reached.
        if (scheduledFilename.equals(datedFilename)) {
            return;
        }

        // close current file, and rename it to datedFilename
        this.closeFile();

        File target  = new File(scheduledFilename);
        if (target.exists()) {
            target.delete();
        }

        File file = new File(fileName);
        boolean result = file.renameTo(target);
        if(result) {
            LogLog.debug(fileName +" -> "+ scheduledFilename);
        } else {
            LogLog.error("Failed to rename ["+fileName+"] to ["+scheduledFilename+"].");
        }
        //roy_modify 移动fileName.i的文件到scheduledFilename.i的文件
        int i = 1;
        while(i>0) {
            file = new File(fileName + "." + i);
            if (!file.exists()){
                break;
            }
            target  = new File(scheduledFilename + "." + i);
            if (target.exists()) {
                target.delete();
            }
            result = file.renameTo(target);
            if(result) {
                LogLog.debug(fileName + "." + i +" -> "+ scheduledFilename + "." + i);
            } else {
                LogLog.error("Failed to rename ["+fileName + "." + i+"] to ["+scheduledFilename + "." + i+"].");
            }
            i++ ;
        }
        try {
            // This will also close the file. This is OK since multiple
            // close operations are safe.
            this.setFile(fileName, true, this.bufferedIO, this.bufferSize);
        }
        catch(IOException e) {
            errorHandler.error("setFile("+fileName+", true) call failed.");
        }
        scheduledFilename = datedFilename;
    }

    /**
     * This method differentiates DailyMaxSizeRollingFileAppender from its
     * super class.
     *
     * <p>Before actually logging, this method will check whether it is
     * time to do a rollover. If it is, it will schedule the next
     * rollover time and then rollover.
     * */
    protected void subAppend(LoggingEvent event) {
        long n = System.currentTimeMillis();
        if (n >= nextCheck) {
            now.setTime(n);
            nextCheck = rpc.getNextCheckMillis(now);
            try {
                rollOver();
            }
            catch(IOException ioe) {
                if (ioe instanceof InterruptedIOException) {
                    Thread.currentThread().interrupt();
                }
                LogLog.error("rollOver() failed.", ioe);
            }
        }
        super.subAppend(event);
        //roy_modify 写之后判断是否需要切分文件
        if(fileName != null && qw != null) {
            long size = ((CountingQuietWriter) qw).getCount();
            if (size >= maxFileSize && size >= nextRollover) {
                rollOverBySize();
            }
        }

    }

    public // synchronization not necessary since doAppend is alreasy synched
    void rollOverBySize() {
        File target;
        File file;

        if (qw != null) {
            long size = ((CountingQuietWriter) qw).getCount();
            LogLog.debug("rolling over count=" + size);
            //   if operation fails, do not roll again until
            //      maxFileSize more bytes are written
            nextRollover = size + maxFileSize;
        }
        //roy_modify 先查找最大的备份数
        int maxBackupIndex = 0;
        while (maxBackupIndex >= 0) {
            file = new File(fileName + "." + (maxBackupIndex + 1));
            if (file.exists()) {
                maxBackupIndex += 1;
            } else {
                break;
            }
        }
        //roy_modify 在最大的文件备份数上加1，保证每次都多生成一个文件
        maxBackupIndex += 1;

        LogLog.debug("maxBackupIndex="+maxBackupIndex);

        boolean renameSucceeded = true;
        // If maxBackups <= 0, then there is no file renaming to be done.
        if(maxBackupIndex > 0) {
            // Delete the oldest file, to keep Windows happy.
            file = new File(fileName + '.' + maxBackupIndex);
            if (file.exists())
                renameSucceeded = file.delete();

            // Map {(maxBackupIndex - 1), ..., 2, 1} to {maxBackupIndex, ..., 3, 2}
            for (int i = maxBackupIndex - 1; i >= 1 && renameSucceeded; i--) {
                file = new File(fileName + "." + i);
                if (file.exists()) {
                    target = new File(fileName + '.' + (i + 1));
                    LogLog.debug("Renaming file " + file + " to " + target);
                    renameSucceeded = file.renameTo(target);
                }
            }

            if(renameSucceeded) {
                // Rename fileName to fileName.1
                target = new File(fileName + "." + 1);

                this.closeFile(); // keep windows happy.

                file = new File(fileName);
                LogLog.debug("Renaming file " + file + " to " + target);
                renameSucceeded = file.renameTo(target);
                //
                //   if file rename failed, reopen file with append = true
                //
                if (!renameSucceeded) {
                    try {
                        this.setFile(fileName, true, bufferedIO, bufferSize);
                    }
                    catch(IOException e) {
                        if (e instanceof InterruptedIOException) {
                            Thread.currentThread().interrupt();
                        }
                        LogLog.error("setFile("+fileName+", true) call failed.", e);
                    }
                }
            }
        }

        //
        //   if all renames were successful, then
        //
        if (renameSucceeded) {
            try {
                // This will also close the file. This is OK since multiple
                // close operations are safe.
                this.setFile(fileName, false, bufferedIO, bufferSize);
                nextRollover = 0;
            }
            catch(IOException e) {
                if (e instanceof InterruptedIOException) {
                    Thread.currentThread().interrupt();
                }
                LogLog.error("setFile("+fileName+", false) call failed.", e);
            }
        }
    }

}

/**
 *  RollingCalendar is a helper class to DailyMaxSizeRollingFileAppender.
 *  Given a periodicity type and the current time, it computes the
 *  start of the next interval.  
 * */
class RollingCalendar extends GregorianCalendar {
    private static final long serialVersionUID = -3560331770601814177L;

    int type = DailyMaxSizeRollingFileAppender.TOP_OF_TROUBLE;

    RollingCalendar() {
        super();
    }

    RollingCalendar(TimeZone tz, Locale locale) {
        super(tz, locale);
    }

    void setType(int type) {
        this.type = type;
    }

    public long getNextCheckMillis(Date now) {
        return getNextCheckDate(now).getTime();
    }

    public Date getNextCheckDate(Date now) {
        this.setTime(now);

        switch(type) {
            case DailyMaxSizeRollingFileAppender.TOP_OF_MINUTE:
                this.set(Calendar.SECOND, 0);
                this.set(Calendar.MILLISECOND, 0);
                this.add(Calendar.MINUTE, 1);
                break;
            case DailyMaxSizeRollingFileAppender.TOP_OF_HOUR:
                this.set(Calendar.MINUTE, 0);
                this.set(Calendar.SECOND, 0);
                this.set(Calendar.MILLISECOND, 0);
                this.add(Calendar.HOUR_OF_DAY, 1);
                break;
            case DailyMaxSizeRollingFileAppender.HALF_DAY:
                this.set(Calendar.MINUTE, 0);
                this.set(Calendar.SECOND, 0);
                this.set(Calendar.MILLISECOND, 0);
                int hour = get(Calendar.HOUR_OF_DAY);
                if(hour < 12) {
                    this.set(Calendar.HOUR_OF_DAY, 12);
                } else {
                    this.set(Calendar.HOUR_OF_DAY, 0);
                    this.add(Calendar.DAY_OF_MONTH, 1);
                }
                break;
            case DailyMaxSizeRollingFileAppender.TOP_OF_DAY:
                this.set(Calendar.HOUR_OF_DAY, 0);
                this.set(Calendar.MINUTE, 0);
                this.set(Calendar.SECOND, 0);
                this.set(Calendar.MILLISECOND, 0);
                this.add(Calendar.DATE, 1);
                break;
            case DailyMaxSizeRollingFileAppender.TOP_OF_WEEK:
                this.set(Calendar.DAY_OF_WEEK, getFirstDayOfWeek());
                this.set(Calendar.HOUR_OF_DAY, 0);
                this.set(Calendar.MINUTE, 0);
                this.set(Calendar.SECOND, 0);
                this.set(Calendar.MILLISECOND, 0);
                this.add(Calendar.WEEK_OF_YEAR, 1);
                break;
            case DailyMaxSizeRollingFileAppender.TOP_OF_MONTH:
                this.set(Calendar.DATE, 1);
                this.set(Calendar.HOUR_OF_DAY, 0);
                this.set(Calendar.MINUTE, 0);
                this.set(Calendar.SECOND, 0);
                this.set(Calendar.MILLISECOND, 0);
                this.add(Calendar.MONTH, 1);
                break;
            default:
                throw new IllegalStateException("Unknown periodicity type.");
        }
        return getTime();
    }
}

