package com.microgis.util;

public class Constants {

    private Constants() {
    }

    public static final String START = "<KPT>";
    public static final String END = "</KPT>";

    //This command does not work
    public static final String CLEAN_ROUTES_COMMAND = "<KPT>3$1$$0$$</KPT>";

    public static final String RELOAD = "<KPT>5$1</KPT>";
    public static final String POST_EMPTY_STATIC_TEXT = "<KPT>16$1$</KPT>";
    public static final String RESET_STATIC_TEXT = "<KPT>17$1</KPT>";
    public static final String RESET_ALL_SETTINGS = "<KPT>8$1</KPT>";
    public static final String RESET_TICKER = "<KPT>11$1</KPT>";
    public static final String READ_TEXT_FOR_TIME_FIELD = "<KPT>15$1$";
    public static final String SYNC_PACKAGE = "<KPT>20$1</KPT>";
    public static final String ANSWER_SYNC_PACKAGE = "<KPT>21$1</KPT>";
    public static final String READ_GPRS = "<KPT>51$1</KPT>";
    public static final String SERVER_SETTINGS = "<KPT>53$1</KPT>";
    public static final String READ_STOP_NAME = "<KPT>61$1</KPT>";
    public static final String CLEAN_STOP_NAME = "<KPT>60$1$</KPT>";
    public static final String READ_PHYSICAL_INFO = "<KPT>303$1</KPT>";
    public static final String READ_DISPLAY_STYLE_SCHEDULE_PARAMETERS = "<KPT>305$1</KPT>";
    public static final String READ_DISPLAY_STYLE_PARAMETERS = "<KPT>307$1</KPT>";
    public static final String READ_OPERATION_PARAMETERS = "<KPT>311$1</KPT>";
    public static final String READ_TIME_ZONE = "<KPT>321$1</KPT>";
    public static final String READ_ADDITIONAL_SERVER = "<KPT>401$1</KPT>";
    public static final String RESET_ADDITIONAL_SERVER = "<KPT>400$1$$</KPT>";
    public static final String RESTART_MODEM = "<KPT>555$1</KPT>";
    public static final String RESETTING_COMMUNICATION_PARAMETERS = "<KPT>558$1</KPT>";
}
