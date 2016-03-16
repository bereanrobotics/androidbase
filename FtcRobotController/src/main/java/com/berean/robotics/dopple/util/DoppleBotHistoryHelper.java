package com.berean.robotics.dopple.util;

import com.berean.robotics.dopple.DoppleBotHistoryRecord;
import com.qualcomm.robotcore.util.RobotLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class provides static helper methods to allow the caller
 * to work with the robot history
 * Created by wdhoward on 3/15/16.
 */
public class DoppleBotHistoryHelper {

    /**
     * Use this method to get a completed DoppleBotHistoryRecord instance from the contents
     * of a file.  File should have the format of
     * one header row: [time(ms), Stringname1, Stringname2, ... Stringname n]
     * multiple value rows: [timeint, value1, value2, ... value n]
     * [timeint, value1, value2, ... value n]
     * [timeint, value1, value2, ... value n]
     *
     * @param historyFile
     * @return
     */
    public static DoppleBotHistoryRecord getHistoryFromFile(File historyFile) {

        DoppleBotHistoryRecord historyTable = new DoppleBotHistoryRecord();

        if (fileIsValid(historyFile)){
            try{
                BufferedReader reader = new BufferedReader(new FileReader(historyFile));

                //read and set the headerLine data
                String headerLine = reader.readLine();
                headerLine = headerLine.substring(1);
                headerLine = headerLine.replaceFirst("]", "");
                List headerList = Arrays.asList(headerLine.split("\\s*,\\s*"));
                RobotLog.i(String.format("HELPER: - %s", headerList.toString()));
                ArrayList<String> arrayListHeader = new ArrayList(headerList);
                historyTable = new DoppleBotHistoryRecord(arrayListHeader);

                //read and set the values data
                String valueLine;

                while ((valueLine = reader.readLine()) != null) {
                    valueLine = valueLine.substring(1);
                    valueLine = valueLine.replaceFirst("]","");
                    String valueLineString[] = valueLine.split("\\s*,\\s*");
                    //handle the first value - it's an int
                    ArrayList valueArrayList = new ArrayList(valueLineString.length);
                    valueArrayList.add(Integer.parseInt(valueLineString[0]));
                    for (int i = 1; i < valueLineString.length; i++) {
                        valueArrayList.add(Double.parseDouble(valueLineString[i]));
                    }
                    RobotLog.i(String.format("HELPER: - value row - %s", valueArrayList.toString()));
                    historyTable.addHistoryValueRow(valueArrayList);
                }

                reader.close();


            }catch (IOException e){
                RobotLog.e(e.getMessage());
            }
        }

        return historyTable;
    }

    /**
     * This private method validates that the historyFile passed into the method
     * is actually well formed and can be processed.
     * @// TODO: 3/16/16 build historyFile validation procedure
     * @param historyFile
     * @return
     */
    private static boolean fileIsValid(File historyFile){
        return true;
    }
}
