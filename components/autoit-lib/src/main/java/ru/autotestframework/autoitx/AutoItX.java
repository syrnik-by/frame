package ru.autotestframework.autoitx;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Variant;

/**
 * Auto it x.
 */
public class AutoItX {
    /**
     * The constant DRIVE_MAP_ADD.
     */
    public static final String DRIVE_MAP_ADD = "DriveMapAdd";
    /**
     * The constant PROCESS_WAIT.
     */
    public static final String PROCESS_WAIT = "ProcessWait";
    /**
     * The constant PROCESS_WAIT_CLOSE.
     */
    public static final String PROCESS_WAIT_CLOSE = "ProcessWaitClose";
    /**
     * The constant RUN_WAIT.
     */
    public static final String RUN_WAIT = "RunWait";
    /**
     * The constant CONTROL_CLICK.
     */
    public static final String CONTROL_CLICK = "ControlClick";
    /**
     * The constant GET_SELECTED.
     */
    public static final String GET_SELECTED = "GetSelected";
    /**
     * The constant CONTROL_LIST_VIEW.
     */
    public static final String CONTROL_LIST_VIEW = "ControlListView";
    /**
     * The constant CONTROL_MOVE.
     */
    public static final String CONTROL_MOVE = "ControlMove";
    /**
     * The constant CONTROL_TREE_VIEW.
     */
    public static final String CONTROL_TREE_VIEW = "ControlTreeView";
    /**
     * The constant STATUSBAR_GET_TEXT.
     */
    public static final String STATUSBAR_GET_TEXT = "StatusbarGetText";
    /**
     * The constant WIN_EXISTS.
     */
    public static final String WIN_EXISTS = "WinExists";
    /**
     * The constant WIN_MENU_SELECT_ITEM.
     */
    public static final String WIN_MENU_SELECT_ITEM = "WinMenuSelectItem";
    /**
     * The constant WIN_WAIT.
     */
    public static final String WIN_WAIT = "WinWait";
    /**
     * The constant WIN_WAIT_ACTIVE.
     */
    public static final String WIN_WAIT_ACTIVE = "WinWaitActive";
    /**
     * The constant WIN_WAIT_CLOSE.
     */
    public static final String WIN_WAIT_CLOSE = "WinWaitClose";
    /**
     * The constant WIN_WAIT_NOT_ACTIVE.
     */
    public static final String WIN_WAIT_NOT_ACTIVE = "WinWaitNotActive";
    /**
     * The constant IS_NOT_COMPLETE.
     */
    public static final String IS_NOT_COMPLETE = "is not complete";
    /**
     * The Active x component.
     */
    protected ActiveXComponent activeXComponent = new ActiveXComponent("AutoItX3.Control");

    /**
     * Gets version.
     *
     * @return the version
     */
    public String getVersion() {
        return this.activeXComponent.getProperty("version").getString();
    }

    /**
     * Gets error.
     *
     * @return the error
     */
    public int getError() {
        Variant error = this.activeXComponent.invoke("error");
        return error.getInt();
    }

    /**
     * Clip get string.
     *
     * @return the string
     */
    public String clipGet() {
        return this.activeXComponent.invoke("ClipGet").getString();
    }

    /**
     * Clip put.
     *
     * @param value the value
     */
    public void clipPut(String value) {
        this.activeXComponent.invoke("ClipPut", new Variant[] {new Variant(value)});
    }

    /**
     * Drive map add boolean.
     *
     * @param device the device
     * @param remote the remote
     * @return the boolean
     */
    public boolean driveMapAdd(String device, String remote) {
        return this.driveMapAdd(device, remote, 0, "", "");
    }

    /**
     * Drive map add boolean.
     *
     * @param device   the device
     * @param remote   the remote
     * @param flags    the flags
     * @param username the username
     * @param password the password
     * @return the boolean
     */
    public boolean driveMapAdd(String device, String remote, int flags, String username, String password) {
        var vDevice = new Variant(device);
        var vRemote = new Variant(remote);
        var vFlags = new Variant(flags);
        var vUsername = new Variant(username);
        var vPassword = new Variant(password);
        var params = new Variant[] {vDevice, vRemote, vFlags, vUsername, vPassword};
        Variant result = this.activeXComponent.invoke(DRIVE_MAP_ADD, params);
        return result.getvt() == 8
                ? this.oneToTrue(Integer.parseInt(result.getString()), DRIVE_MAP_ADD)
                : this.oneToTrue(result.getInt(), DRIVE_MAP_ADD);
    }

    /**
     * Drive map add boolean.
     *
     * @param device the device
     * @param remote the remote
     * @param flags  the flags
     * @return the boolean
     */
    public boolean driveMapAdd(String device, String remote, int flags) {
        var vDevice = new Variant(device);
        var vRemote = new Variant(remote);
        var vFlags = new Variant(flags);
        var params = new Variant[] {vDevice, vRemote, vFlags};
        Variant result = this.activeXComponent.invoke(DRIVE_MAP_ADD, params);
        return result.getvt() == 8
                ? this.oneToTrue(Integer.parseInt(result.getString()), DRIVE_MAP_ADD)
                : this.oneToTrue(result.getInt(), DRIVE_MAP_ADD);
    }

    /**
     * Drive map delete boolean.
     *
     * @param device the device
     * @return the boolean
     */
    public boolean driveMapDelete(String device) {
        Variant result = this.activeXComponent.invoke("DriveMapDel", device);
        return this.oneToTrue(result, "DriveMapDel");
    }

    /**
     * Drive map get string.
     *
     * @param device the device
     * @return the string
     */
    public String driveMapGet(String device) {
        Variant result = this.activeXComponent.invoke("DriveMapGet", device);
        return result.getString();
    }

    /**
     * Ini delete boolean.
     *
     * @param filename the filename
     * @param section  the section
     * @param key      the key
     * @return the boolean
     */
    public boolean iniDelete(String filename, String section, String key) {
        var vFilename = new Variant(filename);
        var vSection = new Variant(section);
        var vKey = new Variant(key);
        var params = new Variant[] {vFilename, vSection, vKey};
        Variant result = this.activeXComponent.invoke("IniDelete", params);
        return this.oneToTrue(result, "IniDelete");
    }

    /**
     * Ini delete boolean.
     *
     * @param filename the filename
     * @param section  the section
     * @return the boolean
     */
    public boolean iniDelete(String filename, String section) {
        return this.iniDelete(filename, section, "");
    }

    /**
     * Ini read string.
     *
     * @param filename   the filename
     * @param section    the section
     * @param key        the key
     * @param defaultVal the default val
     * @return the string
     */
    public String iniRead(String filename, String section, String key, String defaultVal) {
        var vFilename = new Variant(filename);
        var vSection = new Variant(section);
        var vKey = new Variant(key);
        var vDefault = new Variant(defaultVal);
        var params = new Variant[] {vFilename, vSection, vKey, vDefault};
        Variant result = this.activeXComponent.invoke("IniRead", params);
        return result.getString();
    }

    /**
     * Ini write boolean.
     *
     * @param filename the filename
     * @param section  the section
     * @param key      the key
     * @param value    the value
     * @return the boolean
     */
    public Boolean iniWrite(String filename, String section, String key, String value) {
        var vFilename = new Variant(filename);
        var vSection = new Variant(section);
        var vKey = new Variant(key);
        var vValue = new Variant(value);
        var params = new Variant[] {vFilename, vSection, vKey, vValue};
        Variant result = this.activeXComponent.invoke("IniWrite", params);
        return this.oneToTrue(result, "IniWrite");
    }

    /**
     * Pixel checksum double.
     *
     * @param left   the left
     * @param top    the top
     * @param right  the right
     * @param bottom the bottom
     * @param step   the step
     * @return the double
     */
    public double pixelChecksum(int left, int top, int right, int bottom, int step) {
        var vLeft = new Variant(left);
        var vTop = new Variant(top);
        var vRight = new Variant(right);
        var vBottom = new Variant(bottom);
        var vStep = new Variant(step);
        var params = new Variant[] {vLeft, vTop, vRight, vBottom, vStep};
        Variant result = this.activeXComponent.invoke("PixelChecksum", params);
        return result.getDouble();
    }

    /**
     * Pixel checksum double.
     *
     * @param left   the left
     * @param top    the top
     * @param right  the right
     * @param bottom the bottom
     * @return the double
     */
    public double pixelChecksum(int left, int top, int right, int bottom) {
        return this.pixelChecksum(left, top, right, bottom, 0);
    }

    /**
     * Pixel get color float.
     *
     * @param x the x
     * @param y the y
     * @return the float
     */
    public float pixelGetColor(int x, int y) {
        var vX = new Variant(x);
        var vY = new Variant(y);
        var params = new Variant[] {vX, vY};
        Variant result = this.activeXComponent.invoke("PixelGetColor", params);
        return (float) result.getInt();
    }

    /**
     * Pixel search long [ ].
     *
     * @param left           the left
     * @param top            the top
     * @param right          the right
     * @param bottom         the bottom
     * @param color          the color
     * @param shadeVariation the shade variation
     * @param step           the step
     * @return the long [ ]
     */
    public long[] pixelSearch(int left, int top, int right, int bottom, int color, int shadeVariation, int step) {
        var vLeft = new Variant(left);
        var vTop = new Variant(top);
        var vRight = new Variant(right);
        var vBottom = new Variant(bottom);
        var vColor = new Variant(color);
        var vShadeVariation = new Variant(shadeVariation);
        var vStep = new Variant(step);
        var params = new Variant[] {vLeft, vTop, vRight, vBottom, vColor, vShadeVariation, vStep};
        Variant result = this.activeXComponent.invoke("PixelSearch", params);
        var l = new long[2];
        if (result.getvt() == 8204) {
            l[0] = (long) result.toSafeArray().getInt(0);
            l[1] = (long) result.toSafeArray().getInt(1);
        }

        return l;
    }

    /**
     * Pixel search long [ ].
     *
     * @param left   the left
     * @param top    the top
     * @param right  the right
     * @param bottom the bottom
     * @param color  the color
     * @return the long [ ]
     */
    public long[] pixelSearch(int left, int top, int right, int bottom, int color) {
        return this.pixelSearch(left, top, right, bottom, color, 0, 1);
    }

    /**
     * Send.
     *
     * @param keys  the keys
     * @param isRaw the is raw
     */
    public void send(String keys, boolean isRaw) {
        var vKeys = new Variant(keys);
        var vFlag = new Variant(isRaw ? 1 : 0);
        var params = new Variant[] {vKeys, vFlag};
        this.activeXComponent.invoke("Send", params);
    }

    /**
     * Send.
     *
     * @param keys the keys
     */
    public void send(String keys) {
        this.send(keys, true);
    }

    /**
     * Tool tip.
     *
     * @param text the text
     * @param x    the x
     * @param y    the y
     */
    public void toolTip(String text, int x, int y) {
        var vText = new Variant(text);
        var vX = new Variant(x);
        var vY = new Variant(y);
        var params = new Variant[] {vText, vX, vY};
        this.activeXComponent.invoke("ToolTip", params);
    }

    /**
     * Tool tip.
     *
     * @param text the text
     */
    public void toolTip(String text) {
        this.activeXComponent.invoke("ToolTip", text);
    }

    /**
     * Block input.
     *
     * @param disableInput the disable input
     */
    public void blockInput(boolean disableInput) {
        this.activeXComponent.invoke("BlockInput", disableInput ? 1 : 0);
    }

    /**
     * Cd tray boolean.
     *
     * @param drive  the drive
     * @param status the status
     * @return the boolean
     */
    public boolean cdTray(String drive, String status) {
        var vDrive = new Variant(drive);
        var vStatus = new Variant(status);
        var params = new Variant[] {vDrive, vStatus};
        Variant result = this.activeXComponent.invoke("CDTray", params);
        return this.oneToTrue(result, "CDTray");
    }

    /**
     * Is admin boolean.
     *
     * @return the boolean
     */
    public boolean isAdmin() {
        return this.oneToTrue(this.activeXComponent.invoke("IsAdmin"), "IsAdmin");
    }

    /**
     * Auto it set option string.
     *
     * @param option the option
     * @param param  the param
     * @return the string
     */
    public String autoItSetOption(String option, String param) {
        var vOption = new Variant(option);
        var vParam = new Variant(param);
        var params = new Variant[] {vOption, vParam};
        Variant result = this.activeXComponent.invoke("AutoItSetOption", params);
        return result.getvt() == 3 ? String.valueOf(result.getInt()) : result.getString();
    }

    /**
     * Sets option.
     *
     * @param option the option
     * @param param  the param
     * @return the option
     */
    public String setOption(String option, String param) {
        return this.autoItSetOption(option, param);
    }

    /**
     * Mouse click.
     *
     * @param button the button
     * @param x      the x
     * @param y      the y
     * @param clicks the clicks
     * @param speed  the speed
     */
    public void mouseClick(String button, int x, int y, int clicks, int speed) {
        var vButton = new Variant(button);
        var vX = new Variant(x);
        var vY = new Variant(y);
        var vClicks = new Variant(clicks);
        var vSpeed = new Variant(speed);
        var params = new Variant[] {vButton, vX, vY, vClicks, vSpeed};
        this.activeXComponent.invoke("MouseClick", params);
    }

    /**
     * Mouse click.
     *
     * @param button the button
     * @param clicks the clicks
     * @param speed  the speed
     */
    public void mouseClick(String button, int clicks, int speed) {
        var vButton = new Variant(button);
        var vClicks = new Variant(clicks);
        var vSpeed = new Variant(speed);
        var params = new Variant[] {vButton, vClicks, vSpeed};
        this.activeXComponent.invoke("MouseClick", params);
    }

    /**
     * Mouse click drag.
     *
     * @param button the button
     * @param x      the x
     * @param y      the y
     * @param x2     the x 2
     * @param y2     the y 2
     * @param speed  the speed
     */
    public void mouseClickDrag(String button, int x, int y, int x2, int y2, int speed) {
        var vButton = new Variant(button);
        var vX = new Variant(x);
        var vY = new Variant(y);
        var vX2 = new Variant(x2);
        var vY2 = new Variant(y2);
        var vSpeed = new Variant(speed);
        var params = new Variant[] {vButton, vX, vY, vX2, vY2, vSpeed};
        this.activeXComponent.invoke("MouseClickDrag", params);
    }

    /**
     * Mouse click drag.
     *
     * @param button the button
     * @param x      the x
     * @param y      the y
     * @param x2     the x 2
     * @param y2     the y 2
     */
    public void mouseClickDrag(String button, int x, int y, int x2, int y2) {
        this.mouseClickDrag(button, x, y, x2, y2, 10);
    }

    /**
     * Mouse down.
     *
     * @param button the button
     */
    public void mouseDown(String button) {
        this.activeXComponent.invoke("MouseDown", button);
    }

    /**
     * Mouse get cursor int.
     *
     * @return the int
     */
    public int mouseGetCursor() {
        return this.activeXComponent.invoke("MouseGetCursor").getInt();
    }

    /**
     * Mouse get pos x int.
     *
     * @return the int
     */
    public int mouseGetPosX() {
        return this.activeXComponent.invoke("MouseGetPosX").getInt();
    }

    /**
     * Mouse get pos y int.
     *
     * @return the int
     */
    public int mouseGetPosY() {
        return this.activeXComponent.invoke("MouseGetPosY").getInt();
    }

    /**
     * Mouse move boolean.
     *
     * @param x     the x
     * @param y     the y
     * @param speed the speed
     * @return the boolean
     */
    public boolean mouseMove(int x, int y, int speed) {
        var vX = new Variant(x);
        var vY = new Variant(y);
        var vSpeed = new Variant(speed);
        var params = new Variant[] {vX, vY, vSpeed};
        return this.oneToTrue(this.activeXComponent.invoke("MouseMove", params).getInt(), "MouseMove");
    }

    /**
     * Mouse move boolean.
     *
     * @param x the x
     * @param y the y
     * @return the boolean
     */
    public boolean mouseMove(int x, int y) {
        return this.mouseMove(x, y, 10);
    }

    /**
     * Mouse up.
     *
     * @param button the button
     */
    public void mouseUp(String button) {
        this.activeXComponent.invoke("MouseUp", button);
    }

    /**
     * Mouse wheel.
     *
     * @param direction the direction
     * @param clicks    the clicks
     */
    public void mouseWheel(String direction, int clicks) {
        var vDirection = new Variant(direction);
        var vClicks = new Variant(clicks);
        var params = new Variant[] {vDirection, vClicks};
        this.activeXComponent.invoke("MouseWheel", params);
    }

    /**
     * Mouse wheel.
     *
     * @param direction the direction
     */
    public void mouseWheel(String direction) {
        this.mouseWheel(direction, 1);
    }

    /**
     * Process close.
     *
     * @param process the process
     */
    public void processClose(String process) {
        this.activeXComponent.invoke("ProcessClose", process);
    }

    /**
     * Process exists int.
     *
     * @param process the process
     * @return the int
     */
    public int processExists(String process) {
        return this.activeXComponent.invoke("ProcessExists", process).getInt();
    }

    /**
     * Process set priority boolean.
     *
     * @param process  the process
     * @param priority the priority
     * @return the boolean
     */
    public boolean processSetPriority(String process, int priority) {
        var vProcess = new Variant(process);
        var vPriority = new Variant(priority);
        var params = new Variant[] {vProcess, vPriority};
        Variant result = this.activeXComponent.invoke("ProcessSetPriority", params);
        return this.oneToTrue(result.getInt(), "ProcessSetPriority");
    }

    /**
     * Process wait boolean.
     *
     * @param process the process
     * @param timeout the timeout
     * @return the boolean
     */
    public boolean processWait(String process, int timeout) {
        var vProcess = new Variant(process);
        var vTimeout = new Variant(timeout);
        var params = new Variant[] {vProcess, vTimeout};
        Variant result = this.activeXComponent.invoke(PROCESS_WAIT, params);
        return this.oneToTrue(result.getInt(), PROCESS_WAIT);
    }

    /**
     * Process wait boolean.
     *
     * @param process the process
     * @return the boolean
     */
    public boolean processWait(String process) {
        Variant result = this.activeXComponent.invoke(PROCESS_WAIT, process);
        return this.oneToTrue(result.getInt(), PROCESS_WAIT);
    }

    /**
     * Process wait close boolean.
     *
     * @param process the process
     * @param timeout the timeout
     * @return the boolean
     */
    public boolean processWaitClose(String process, int timeout) {
        var vProcess = new Variant(process);
        var vTimeout = new Variant(timeout);
        var params = new Variant[] {vProcess, vTimeout};
        Variant result = this.activeXComponent.invoke(PROCESS_WAIT_CLOSE, params);
        return this.oneToTrue(result.getInt(), PROCESS_WAIT_CLOSE);
    }

    /**
     * Process wait close boolean.
     *
     * @param process the process
     * @return the boolean
     */
    public boolean processWaitClose(String process) {
        Variant result = this.activeXComponent.invoke(PROCESS_WAIT_CLOSE, process);
        return this.oneToTrue(result.getInt(), PROCESS_WAIT_CLOSE);
    }

    /**
     * Run int.
     *
     * @param filename         the filename
     * @param workingDirectory the working directory
     * @param flag             the flag
     * @return the int
     */
    public int run(String filename, String workingDirectory, int flag) {
        var vFilename = new Variant(filename);
        var vWorkingDirectory = new Variant(workingDirectory);
        var vFlag = new Variant(flag);
        var params = new Variant[] {vFilename, vWorkingDirectory, vFlag};
        return this.activeXComponent.invoke("Run", params).getInt();
    }

    /**
     * Run int.
     *
     * @param filename         the filename
     * @param workingDirectory the working directory
     * @return the int
     */
    public int run(String filename, String workingDirectory) {
        var vFilename = new Variant(filename);
        var vWorkingDirectory = new Variant(workingDirectory);
        var params = new Variant[] {vFilename, vWorkingDirectory};
        return this.activeXComponent.invoke("Run", params).getInt();
    }

    /**
     * Run int.
     *
     * @param filename the filename
     * @return the int
     */
    public int run(String filename) {
        return this.activeXComponent.invoke("Run", filename).getInt();
    }

    /**
     * Run as set int.
     *
     * @param username the username
     * @param domain   the domain
     * @param password the password
     * @param options  the options
     * @return the int
     */
    public int runAsSet(String username, String domain, String password, int options) {
        var vUsername = new Variant(username);
        var vDomain = new Variant(domain);
        var vPassword = new Variant(password);
        var vOptions = new Variant(options);
        var params = new Variant[] {vUsername, vDomain, vPassword, vOptions};
        return this.activeXComponent.invoke("RunAsSet", params).getInt();
    }

    /**
     * Run as set int.
     *
     * @param username the username
     * @param domain   the domain
     * @param password the password
     * @return the int
     */
    public int runAsSet(String username, String domain, String password) {
        return this.runAsSet(username, domain, password, 1);
    }

    /**
     * Run wait int.
     *
     * @param filename         the filename
     * @param workingDirectory the working directory
     * @param flag             the flag
     * @return the int
     */
    public int runWait(String filename, String workingDirectory, int flag) {
        var vFilename = new Variant(filename);
        var vWorkingDirectory = new Variant(workingDirectory);
        var vFlag = new Variant(flag);
        var params = new Variant[] {vFilename, vWorkingDirectory, vFlag};
        return this.activeXComponent.invoke(RUN_WAIT, params).getInt();
    }

    /**
     * Run wait int.
     *
     * @param filename         the filename
     * @param workingDirectory the working directory
     * @return the int
     */
    public int runWait(String filename, String workingDirectory) {
        var vFilename = new Variant(filename);
        var vWorkingDirectory = new Variant(workingDirectory);
        var params = new Variant[] {vFilename, vWorkingDirectory};
        return this.activeXComponent.invoke(RUN_WAIT, params).getInt();
    }

    /**
     * Run wait int.
     *
     * @param filename the filename
     * @return the int
     */
    public int runWait(String filename) {
        return this.activeXComponent.invoke(RUN_WAIT, filename).getInt();
    }

    /**
     * Shutdown boolean.
     *
     * @param code the code
     * @return the boolean
     */
    public boolean shutdown(int code) {
        return this.oneToTrue(
                this.activeXComponent
                        .invoke("Shutdown", new Variant[] {new Variant(code)})
                        .getInt(),
                "Shutdown");
    }

    /**
     * Reg delete key int.
     *
     * @param keyname the keyname
     * @return the int
     */
    public int regDeleteKey(String keyname) {
        return this.activeXComponent.invoke("RegDeleteKey", keyname).getInt();
    }

    /**
     * Reg delete val int.
     *
     * @param keyname the keyname
     * @return the int
     */
    public int regDeleteVal(String keyname) {
        return this.activeXComponent.invoke("RegDeleteVal", keyname).getInt();
    }

    /**
     * Reg enum key string.
     *
     * @param keyname  the keyname
     * @param instance the instance
     * @return the string
     */
    public String regEnumKey(String keyname, int instance) {
        var vKeyname = new Variant(keyname);
        var vInstance = new Variant(instance);
        var params = new Variant[] {vKeyname, vInstance};
        return this.activeXComponent.invoke("RegEnumKey", params).getString();
    }

    /**
     * Reg enum val string.
     *
     * @param keyname  the keyname
     * @param instance the instance
     * @return the string
     */
    public String regEnumVal(String keyname, int instance) {
        var vKeyname = new Variant(keyname);
        var vInstance = new Variant(instance);
        var params = new Variant[] {vKeyname, vInstance};
        return this.activeXComponent.invoke("RegEnumVal", params).getString();
    }

    /**
     * Reg read string.
     *
     * @param keyname   the keyname
     * @param valueName the value name
     * @return the string
     */
    public String regRead(String keyname, String valueName) {
        var vKeyname = new Variant(keyname);
        var vValueName = new Variant(valueName);
        var params = new Variant[] {vKeyname, vValueName};
        Variant result = this.activeXComponent.invoke("RegRead", params);
        if (result.getvt() == 3) {
            return String.valueOf(result.getInt());
        } else {
            return result.getvt() == 8 ? result.getString() : "";
        }
    }

    /**
     * Reg write boolean.
     *
     * @param keyname   the keyname
     * @param valueName the value name
     * @param type      the type
     * @param value     the value
     * @return the boolean
     */
    public boolean regWrite(String keyname, String valueName, String type, String value) {
        var vKeyname = new Variant(keyname);
        var vValueName = new Variant(valueName);
        var vType = new Variant(type);
        var vValue = new Variant(value);
        var params = new Variant[] {vKeyname, vValueName, vType, vValue};
        Variant result = this.activeXComponent.invoke("RegWrite", params);
        return this.oneToTrue(result.getInt(), "RegWrite");
    }

    /**
     * Sleep.
     *
     * @param delay the delay
     */
    public void sleep(int delay) {
        this.activeXComponent.invoke("sleep", delay);
    }

    /**
     * Control click boolean.
     *
     * @param title     the title
     * @param text      the text
     * @param controlID the control id
     * @param button    the button
     * @param clicks    the clicks
     * @param x         the x
     * @param y         the y
     * @return the boolean
     */
    public boolean controlClick(String title, String text, String controlID, String button, int clicks, int x, int y) {
        var vTitle = new Variant(title);
        var vText = new Variant(text);
        var vControlID = new Variant(controlID);
        var vButton = new Variant(button);
        var vClicks = new Variant(clicks);
        var vX = new Variant(x);
        var vY = new Variant(y);
        var params = new Variant[] {vTitle, vText, vControlID, vButton, vClicks, vX, vY};
        Variant result = this.activeXComponent.invoke(CONTROL_CLICK, params);
        return this.oneToTrue(result.getInt(), CONTROL_CLICK);
    }

    /**
     * Control click boolean.
     *
     * @param title     the title
     * @param text      the text
     * @param controlID the control id
     * @param button    the button
     * @param clicks    the clicks
     * @return the boolean
     */
    public boolean controlClick(String title, String text, String controlID, String button, int clicks) {
        var vTitle = new Variant(title);
        var vText = new Variant(text);
        var vControlID = new Variant(controlID);
        var vButton = new Variant(button);
        var vClicks = new Variant(clicks);
        var params = new Variant[] {vTitle, vText, vControlID, vButton, vClicks};
        Variant result = this.activeXComponent.invoke(CONTROL_CLICK, params);
        return this.oneToTrue(result.getInt(), CONTROL_CLICK);
    }

    /**
     * Control click boolean.
     *
     * @param title     the title
     * @param text      the text
     * @param controlID the control id
     * @param button    the button
     * @return the boolean
     */
    public boolean controlClick(String title, String text, String controlID, String button) {
        return this.controlClick(title, text, controlID, button, 1);
    }

    /**
     * Control click boolean.
     *
     * @param title     the title
     * @param text      the text
     * @param controlID the control id
     * @return the boolean
     */
    public boolean controlClick(String title, String text, String controlID) {
        return this.controlClick(title, text, controlID, "left", 1);
    }

    private String controlCommandString(String title, String text, String control, String command, String option) {
        var result = this.controlCommandVariant(title, text, control, command, option);
        return result.getString();
    }

    private void controlCommandVoid(String title, String text, String control, String command, String option) {
        this.controlCommandVariant(title, text, control, command, option);
    }

    private Variant controlCommandVariant(String title, String text, String control, String command, String option) {
        var vTitle = new Variant(title);
        var vText = new Variant(text);
        var vControl = new Variant(control);
        var vCommand = new Variant(command);
        var vOption = new Variant(option);
        var params = new Variant[] {vTitle, vText, vControl, vCommand, vOption};
        return this.activeXComponent.invoke("ControlCommand", params);
    }

    /**
     * Control command show dropdown.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     */
    public void controlCommandShowDropdown(String title, String text, String control) {
        this.controlCommandVoid(title, text, control, "ShowDropDown", "");
    }

    /**
     * Control command hide drop down.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     */
    public void controlCommandHideDropDown(String title, String text, String control) {
        this.controlCommandVoid(title, text, control, "HideDropDown", "");
    }

    /**
     * Control command check.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     */
    public void controlCommandCheck(String title, String text, String control) {
        this.controlCommandVoid(title, text, control, "Check", "");
    }

    /**
     * Control command uncheck.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     */
    public void controlCommandUncheck(String title, String text, String control) {
        this.controlCommandVoid(title, text, control, "UnCheck", "");
    }

    /**
     * Control command add string.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @param string  the string
     */
    public void controlCommandAddString(String title, String text, String control, String string) {
        this.controlCommandVoid(title, text, control, "AddString", string);
    }

    /**
     * Control command delete string.
     *
     * @param title      the title
     * @param text       the text
     * @param control    the control
     * @param occurrance the occurrance
     */
    public void controlCommandDeleteString(String title, String text, String control, String occurrance) {
        this.controlCommandVoid(title, text, control, "DelString", occurrance);
    }

    /**
     * Control command edit paste.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @param string  the string
     */
    public void controlCommandEditPaste(String title, String text, String control, String string) {
        this.controlCommandVoid(title, text, control, "EditPaste", string);
    }

    /**
     * Control command set current selection.
     *
     * @param title      the title
     * @param text       the text
     * @param control    the control
     * @param occurrance the occurrance
     */
    public void controlCommandSetCurrentSelection(String title, String text, String control, String occurrance) {
        this.controlCommandVoid(title, text, control, "SetCurrentSelection", occurrance);
    }

    /**
     * Control command select string.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @param string  the string
     */
    public void controlCommandSelectString(String title, String text, String control, String string) {
        this.controlCommandVoid(title, text, control, "SelectString", string);
    }

    private boolean controlCommandBoolean(String title, String text, String control, String command, String option) {
        return this.oneToBool(this.controlCommandInts(title, text, control, command, option));
    }

    /**
     * Control command is visible boolean.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @return the boolean
     */
    public boolean controlCommandIsVisible(String title, String text, String control) {
        return this.controlCommandBoolean(title, text, control, "IsVisible", "");
    }

    /**
     * Control command is checked boolean.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @return the boolean
     */
    public boolean controlCommandIsChecked(String title, String text, String control) {
        return this.controlCommandBoolean(title, text, control, "IsChecked", "");
    }

    /**
     * Control command is enabled boolean.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @return the boolean
     */
    public boolean controlCommandIsEnabled(String title, String text, String control) {
        return this.controlCommandBoolean(title, text, control, "IsEnabled", "");
    }

    /**
     * Control command find string int.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @param string  the string
     * @return the int
     */
    public int controlCommandFindString(String title, String text, String control, String string) {
        return this.controlCommandInts(title, text, control, "FindString", string);
    }

    private int controlCommandInts(String title, String text, String control, String command, String option) {
        var result = this.controlCommandVariant(title, text, control, command, option);
        return result.getvt() == 8 ? Integer.parseInt(result.getString()) : 0;
    }

    /**
     * Control command get current line int.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @return the int
     */
    public int controlCommandGetCurrentLine(String title, String text, String control) {
        return this.controlCommandInts(title, text, control, "GetCurrentLine", "");
    }

    /**
     * Control command get current col int.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @return the int
     */
    public int controlCommandGetCurrentCol(String title, String text, String control) {
        return this.controlCommandInts(title, text, control, "GetCurrentCol", "");
    }

    /**
     * Control command get line count int.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @return the int
     */
    public int controlCommandGetLineCount(String title, String text, String control) {
        return this.controlCommandInts(title, text, control, "GetLineCount", "");
    }

    /**
     * Control command get current selection string.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @return the string
     */
    public String controlCommandGetCurrentSelection(String title, String text, String control) {
        return this.controlCommandString(title, text, control, "GetCurrentSelection", "");
    }

    /**
     * Control command get selected string.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @return the string
     */
    public String controlCommandGetSelected(String title, String text, String control) {
        return this.controlCommandString(title, text, control, GET_SELECTED, "");
    }

    /**
     * Control command tab left.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     */
    public void controlCommandTabLeft(String title, String text, String control) {
        this.controlCommandVoid(title, text, control, "TabLeft", "");
    }

    /**
     * Control command tab right.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     */
    public void controlCommandTabRight(String title, String text, String control) {
        this.controlCommandVoid(title, text, control, "TabRight", "");
    }

    /**
     * Control command current tab string.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @return the string
     */
    public String controlCommandCurrentTab(String title, String text, String control) {
        return this.controlCommandString(title, text, control, "CurrentTab", "");
    }

    /**
     * Control disable boolean.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @return the boolean
     */
    public boolean controlDisable(String title, String text, String control) {
        var vTitle = new Variant(title);
        var vText = new Variant(text);
        var vControl = new Variant(control);
        var params = new Variant[] {vTitle, vText, vControl};
        Variant result = this.activeXComponent.invoke("ControlDisable", params);
        return this.oneToTrue(result.getInt(), "ControlDisable");
    }

    /**
     * Control enable boolean.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @return the boolean
     */
    public boolean controlEnable(String title, String text, String control) {
        var vTitle = new Variant(title);
        var vText = new Variant(text);
        var vControl = new Variant(control);
        var params = new Variant[] {vTitle, vText, vControl};
        Variant result = this.activeXComponent.invoke("ControlEnable", params);
        return this.oneToTrue(result.getInt(), "ControlEnable");
    }

    /**
     * Control focus boolean.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @return the boolean
     */
    public boolean controlFocus(String title, String text, String control) {
        return this.controlBool(title, text, control, "ControlFocus");
    }

    /**
     * Control get focus string.
     *
     * @param title the title
     * @param text  the text
     * @return the string
     */
    public String controlGetFocus(String title, String text) {
        var vTitle = new Variant(title);
        var vText = new Variant(text);
        var params = new Variant[] {vTitle, vText};
        return this.activeXComponent.invoke("ControlGetFocus", params).getString();
    }

    /**
     * Control get focus string.
     *
     * @param title the title
     * @return the string
     */
    public String controlGetFocus(String title) {
        return this.activeXComponent.invoke("ControlGetFocus", title).getString();
    }

    /**
     * Control get handle string.
     *
     * @param title     the title
     * @param text      the text
     * @param controlID the control id
     * @return the string
     */
    public String controlGetHandle(String title, String text, String controlID) {
        return this.controlString(title, text, controlID, "ControlGetHandle");
    }

    /**
     * Control get pos x int.
     *
     * @param title     the title
     * @param text      the text
     * @param controlID the control id
     * @return the int
     */
    public int controlGetPosX(String title, String text, String controlID) {
        return this.controlInt(title, text, controlID, "ControlGetPosX");
    }

    /**
     * Control get pos y int.
     *
     * @param title     the title
     * @param text      the text
     * @param controlID the control id
     * @return the int
     */
    public int controlGetPosY(String title, String text, String controlID) {
        return this.controlInt(title, text, controlID, "ControlGetPosY");
    }

    /**
     * Control get pos width int.
     *
     * @param title     the title
     * @param text      the text
     * @param controlID the control id
     * @return the int
     */
    public int controlGetPosWidth(String title, String text, String controlID) {
        return this.controlInt(title, text, controlID, "ControlGetPosWidth");
    }

    /**
     * Control get pos height int.
     *
     * @param title     the title
     * @param text      the text
     * @param controlID the control id
     * @return the int
     */
    public int controlGetPosHeight(String title, String text, String controlID) {
        return this.controlInt(title, text, controlID, "ControlGetPosHeight");
    }

    /**
     * Control get text string.
     *
     * @param title     the title
     * @param text      the text
     * @param controlID the control id
     * @return the string
     */
    public String controlGetText(String title, String text, String controlID) {
        return this.controlString(title, text, controlID, "ControlGetText");
    }

    /**
     * Control hide boolean.
     *
     * @param title     the title
     * @param text      the text
     * @param controlID the control id
     * @return the boolean
     */
    public boolean controlHide(String title, String text, String controlID) {
        return this.controlBool(title, text, controlID, "ControlHide");
    }

    /**
     * Control list view find item int.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @param string  the string
     * @param subitem the subitem
     * @return the int
     */
    public int controlListViewFindItem(String title, String text, String control, String string, String subitem) {
        return this.controlListViewInt(title, text, control, "FindItem", string, subitem);
    }

    private int controlListViewInt(
            String title, String text, String control, String command, String option, String option2) {
        return this.controlView(title, text, control, command, option, option2, CONTROL_LIST_VIEW)
                .getInt();
    }

    private String controlListViewString(
            String title, String text, String control, String command, String option, String option2) {
        return this.controlView(title, text, control, command, option, option2, CONTROL_LIST_VIEW)
                .getString();
    }

    private Variant controlView(
            String title, String text, String control, String command, String option, String option2, String function) {
        var vTitle = new Variant(title);
        var vText = new Variant(text);
        var vControl = new Variant(control);
        var vCommand = new Variant(command);
        var vOption = new Variant(option);
        var vOption2 = new Variant(option2);
        var params = new Variant[] {vTitle, vText, vControl, vCommand, vOption, vOption2};
        return this.activeXComponent.invoke(function, params);
    }

    /**
     * Control list view get item count int.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @return the int
     */
    public int controlListViewGetItemCount(String title, String text, String control) {
        return this.controlListViewInt(title, text, control, "GetItemCount", "", "");
    }

    /**
     * Control list view get selected count int.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @return the int
     */
    public int controlListViewGetSelectedCount(String title, String text, String control) {
        return this.controlListViewInt(title, text, control, "GetSelectedCount", "", "");
    }

    /**
     * Control list view get sub item count int.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @return the int
     */
    public int controlListViewGetSubItemCount(String title, String text, String control) {
        return this.controlListViewInt(title, text, control, "GetSubItemCount", "", "");
    }

    /**
     * Control list view get text string.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @param item    the item
     * @param subitem the subitem
     * @return the string
     */
    public String controlListViewGetText(String title, String text, String control, String item, String subitem) {
        return this.controlListViewString(title, text, control, "GetText", item, subitem);
    }

    /**
     * Control list view is selected boolean.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @param item    the item
     * @return the boolean
     */
    public boolean controlListViewIsSelected(String title, String text, String control, String item) {
        return this.oneToTrue(this.controlListViewInt(title, text, control, "IsSelected", item, ""), CONTROL_LIST_VIEW);
    }

    /**
     * Control list view get selected string.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @return the string
     */
    public String controlListViewGetSelected(String title, String text, String control) {
        return this.controlListViewString(title, text, control, GET_SELECTED, "", "");
    }

    /**
     * Control list view get selected array string [ ].
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @return the string [ ]
     */
    public String[] controlListViewGetSelectedArray(String title, String text, String control) {
        var safeArr = this.controlView(title, text, control, GET_SELECTED, "", "", CONTROL_LIST_VIEW)
                .toSafeArray();
        return safeArr.toStringArray();
    }

    /**
     * Control list view select.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @param from    the from
     * @param to      the to
     */
    public void controlListViewSelect(String title, String text, String control, String from, String to) {
        this.controlView(title, text, control, "Select", from, to, CONTROL_LIST_VIEW);
    }

    /**
     * Control list view select all.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @param from    the from
     * @param to      the to
     */
    public void controlListViewSelectAll(String title, String text, String control, String from, String to) {
        this.controlView(title, text, control, "SelectAll", from, to, CONTROL_LIST_VIEW);
    }

    /**
     * Control list view select all.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @param from    the from
     */
    public void controlListViewSelectAll(String title, String text, String control, String from) {
        var vTitle = new Variant(title);
        var vText = new Variant(text);
        var vControl = new Variant(control);
        var vCommand = new Variant("SelectAll");
        var vFrom = new Variant(from);
        var params = new Variant[] {vTitle, vText, vControl, vCommand, vFrom};
        this.activeXComponent.invoke(CONTROL_LIST_VIEW, params);
    }

    /**
     * Control list view select clear.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     */
    public void controlListViewSelectClear(String title, String text, String control) {
        this.controlView(title, text, control, "SelectClear", "", "", CONTROL_LIST_VIEW);
    }

    /**
     * Control list view select invert.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     */
    public void controlListViewSelectInvert(String title, String text, String control) {
        this.controlView(title, text, control, "SelectInvert", "", "", CONTROL_LIST_VIEW);
    }

    /**
     * Control list view select view change.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @param view    the view
     */
    public void controlListViewSelectViewChange(String title, String text, String control, String view) {
        this.controlView(title, text, control, "ViewChnage", view, "", CONTROL_LIST_VIEW);
    }

    /**
     * Control variant variant.
     *
     * @param title    the title
     * @param text     the text
     * @param control  the control
     * @param function the function
     * @return the variant
     */
    protected Variant controlVariant(String title, String text, String control, String function) {
        var vTitle = new Variant(title);
        var vText = new Variant(text);
        var vControl = new Variant(control);
        var params = new Variant[] {vTitle, vText, vControl};
        return this.activeXComponent.invoke(function, params);
    }

    /**
     * Control bool boolean.
     *
     * @param title    the title
     * @param text     the text
     * @param control  the control
     * @param function the function
     * @return the boolean
     */
    protected boolean controlBool(String title, String text, String control, String function) {
        var result = this.controlVariant(title, text, control, function);
        return this.oneToTrue(result.getInt(), function);
    }

    /**
     * Control int int.
     *
     * @param title    the title
     * @param text     the text
     * @param control  the control
     * @param function the function
     * @return the int
     */
    protected int controlInt(String title, String text, String control, String function) {
        var result = this.controlVariant(title, text, control, function);
        return result.getInt();
    }

    /**
     * Control string string.
     *
     * @param title    the title
     * @param text     the text
     * @param control  the control
     * @param function the function
     * @return the string
     */
    protected String controlString(String title, String text, String control, String function) {
        var result = this.controlVariant(title, text, control, function);
        if (result.getvt() == 8) {
            return result.getString();
        } else {
            return result.getvt() == 3 ? String.valueOf(result.getInt()) : "";
        }
    }

    /**
     * Control move boolean.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @param x       the x
     * @param y       the y
     * @param width   the width
     * @param height  the height
     * @return the boolean
     */
    public boolean controlMove(String title, String text, String control, int x, int y, int width, int height) {
        var vTitle = new Variant(title);
        var vText = new Variant(text);
        var vControl = new Variant(control);
        var vX = new Variant(x);
        var vY = new Variant(y);
        var vWidth = new Variant(width);
        var vHeight = new Variant(height);
        var params = new Variant[] {vTitle, vText, vControl, vX, vY, vWidth, vHeight};
        Variant result = this.activeXComponent.invoke(CONTROL_MOVE, params);
        return this.oneToTrue(result.getInt(), CONTROL_MOVE);
    }

    /**
     * Control move boolean.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @param x       the x
     * @param y       the y
     * @return the boolean
     */
    public boolean controlMove(String title, String text, String control, int x, int y) {
        var vTitle = new Variant(title);
        var vText = new Variant(text);
        var vControl = new Variant(control);
        var vX = new Variant(x);
        var vY = new Variant(y);
        var params = new Variant[] {vTitle, vText, vControl, vX, vY};
        Variant result = this.activeXComponent.invoke(CONTROL_MOVE, params);
        return this.oneToTrue(result.getInt(), CONTROL_MOVE);
    }

    /**
     * Control send boolean.
     *
     * @param title       the title
     * @param text        the text
     * @param control     the control
     * @param string      the string
     * @param sendRawKeys the send raw keys
     * @return the boolean
     */
    public boolean controlSend(String title, String text, String control, String string, boolean sendRawKeys) {
        var vTitle = new Variant(title);
        var vText = new Variant(text);
        var vControl = new Variant(control);
        var vString = new Variant(string);
        int flag = sendRawKeys ? 1 : 0;
        var vFlag = new Variant(flag);
        var params = new Variant[] {vTitle, vText, vControl, vString, vFlag};
        Variant result = this.activeXComponent.invoke("ControlSend", params);
        return this.oneToTrue(result.getInt(), "ControlSend");
    }

    /**
     * Control send boolean.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @param string  the string
     * @return the boolean
     */
    public boolean controlSend(String title, String text, String control, String string) {
        return this.controlSend(title, text, control, string, false);
    }

    /**
     * Control set text boolean.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @param string  the string
     * @return the boolean
     */
    public boolean ControlSetText(String title, String text, String control, String string) {
        var vTitle = new Variant(title);
        var vText = new Variant(text);
        var vControl = new Variant(control);
        var vString = new Variant(string);
        var params = new Variant[] {vTitle, vText, vControl, vString};
        Variant result = this.activeXComponent.invoke("ControlSetText", params);
        return this.oneToTrue(result.getInt(), "ControlSetText");
    }

    /**
     * Control show boolean.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @return the boolean
     */
    public boolean controlShow(String title, String text, String control) {
        return this.controlBool(title, text, control, "ControlShow");
    }

    private String controlTreeViewString(
            String title, String text, String control, String command, String option, String option2) {
        return this.controlView(title, text, control, command, option, option2, CONTROL_TREE_VIEW)
                .getString();
    }

    private int controlTreeViewInt(
            String title, String text, String control, String command, String option, String option2) {
        return this.controlView(title, text, control, command, option, option2, CONTROL_TREE_VIEW)
                .getInt();
    }

    /**
     * Control tree view boolean boolean.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @param command the command
     * @param option  the option
     * @param option2 the option 2
     * @return the boolean
     */
    public boolean controlTreeViewBoolean(
            String title, String text, String control, String command, String option, String option2) {
        Variant result = this.controlView(title, text, control, command, option, option2, CONTROL_TREE_VIEW);
        return this.oneToTrue(result.getInt(), CONTROL_TREE_VIEW);
    }

    /**
     * Control tree view check.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @param item    the item
     */
    public void controlTreeViewCheck(String title, String text, String control, String item) {
        this.controlView(title, text, control, "Check", item, "", CONTROL_TREE_VIEW);
    }

    /**
     * Control tree view collapse.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @param item    the item
     */
    public void controlTreeViewCollapse(String title, String text, String control, String item) {
        this.controlView(title, text, control, "Collapse", item, "", CONTROL_TREE_VIEW);
    }

    /**
     * Control tree view exists boolean.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @param item    the item
     * @return the boolean
     */
    public Boolean controlTreeViewExists(String title, String text, String control, String item) {
        return this.controlTreeViewBoolean(title, text, control, "Exists", item, "");
    }

    /**
     * Control tree view expand.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @param item    the item
     */
    public void controlTreeViewExpand(String title, String text, String control, String item) {
        this.controlView(title, text, control, "Expand", item, "", CONTROL_TREE_VIEW);
    }

    /**
     * Control tree view get item count int.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @param item    the item
     * @return the int
     */
    public int controlTreeViewGetItemCount(String title, String text, String control, String item) {
        return this.controlTreeViewInt(title, text, control, "GetItemCount", item, "");
    }

    /**
     * Control tree view get selected item index int.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @return the int
     */
    public int controlTreeViewGetSelectedItemIndex(String title, String text, String control) {
        var vTitle = new Variant(title);
        var vText = new Variant(text);
        var vControl = new Variant(control);
        var vCommand = new Variant(GET_SELECTED);
        var vIndex = new Variant(1);
        var params = new Variant[] {vTitle, vText, vControl, vCommand, vIndex};
        return this.activeXComponent.invoke(CONTROL_TREE_VIEW, params).getInt();
    }

    /**
     * Control tree view get selected item text string.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @return the string
     */
    public String controlTreeViewGetSelectedItemText(String title, String text, String control) {
        var vTitle = new Variant(title);
        var vText = new Variant(text);
        var vControl = new Variant(control);
        var vCommand = new Variant(GET_SELECTED);
        var vIndex = new Variant(0);
        var params = new Variant[] {vTitle, vText, vControl, vCommand, vIndex};
        return this.activeXComponent.invoke(CONTROL_TREE_VIEW, params).getString();
    }

    /**
     * Control tree view get text string.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @param item    the item
     * @return the string
     */
    public String controlTreeViewGetText(String title, String text, String control, String item) {
        return this.controlTreeViewString(title, text, control, "GetText", item, "");
    }

    /**
     * Control tree view is checked int.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @return the int
     */
    public int controlTreeViewIsChecked(String title, String text, String control) {
        return this.controlView(title, text, control, "IsChecked", "", "", CONTROL_TREE_VIEW)
                .getInt();
    }

    /**
     * Control tree view select.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @param item    the item
     */
    public void controlTreeViewSelect(String title, String text, String control, String item) {
        this.controlView(title, text, control, "Select", item, "", CONTROL_TREE_VIEW);
    }

    /**
     * Control tree view uncheck.
     *
     * @param title   the title
     * @param text    the text
     * @param control the control
     * @param item    the item
     */
    public void controlTreeViewUncheck(String title, String text, String control, String item) {
        this.controlView(title, text, control, "Uncheck", item, "", CONTROL_TREE_VIEW);
    }

    /**
     * Statusbar get text by title test and part string.
     *
     * @param title the title
     * @param text  the text
     * @param part  the part
     * @return the string
     */
    public String statusbarGetTextByTitleTestAndPart(String title, String text, int part) {
        var vTitle = new Variant(title);
        var vText = new Variant(text);
        var vPart = new Variant(part);
        var params = new Variant[] {vTitle, vText, vPart};
        return this.activeXComponent.invoke(STATUSBAR_GET_TEXT, params).getString();
    }

    /**
     * Statusbar get text by title and text string.
     *
     * @param title the title
     * @param text  the text
     * @return the string
     */
    public String StatusbarGetTextByTitleAndText(String title, String text) {
        return this.winVariant(title, text, STATUSBAR_GET_TEXT).getString();
    }

    private Variant winVariant(String title, String text, String function) {
        var vTitle = new Variant(title);
        var vText = new Variant(text);
        var params = new Variant[] {vTitle, vText};
        return this.activeXComponent.invoke(function, params);
    }

    private Variant winVariant(String title, String function) {
        var vTitle = new Variant(title);
        var params = new Variant[] {vTitle};
        return this.activeXComponent.invoke(function, params);
    }

    /**
     * Win activate.
     *
     * @param title the title
     * @param text  the text
     */
    public void winActivate(String title, String text) {
        this.winVariant(title, text, "WinActivate");
    }

    /**
     * Win activate.
     *
     * @param title the title
     */
    public void winActivate(String title) {
        this.winVariant(title, "WinActivate");
    }

    /**
     * Win active.
     *
     * @param title the title
     * @param text  the text
     */
    public void winActive(String title, String text) {
        this.winVariant(title, text, "WinActive");
    }

    /**
     * Win active.
     *
     * @param title the title
     */
    public void winActive(String title) {
        this.winVariant(title, "WinActive");
    }

    /**
     * Win close.
     *
     * @param title the title
     * @param text  the text
     */
    public void winClose(String title, String text) {
        this.winVariant(title, text, "WinClose");
    }

    /**
     * Win close.
     *
     * @param title the title
     */
    public void winClose(String title) {
        this.winVariant(title, "WinClose");
    }

    /**
     * Win exists boolean.
     *
     * @param title the title
     * @param text  the text
     * @return the boolean
     */
    public boolean winExists(String title, String text) {
        var result = this.winVariant(title, text, WIN_EXISTS);
        return this.oneToTrue(result.getInt(), WIN_EXISTS);
    }

    /**
     * Win exists boolean.
     *
     * @param title the title
     * @return the boolean
     */
    public boolean winExists(String title) {
        var result = this.winVariant(title, WIN_EXISTS);
        return this.oneToTrue(result.getInt(), WIN_EXISTS);
    }

    /**
     * Win get caret pos x int.
     *
     * @return the int
     */
    public int winGetCaretPosX() {
        return this.activeXComponent.invoke("WinGetCaretPosX").getInt();
    }

    /**
     * Win get caret pos y int.
     *
     * @return the int
     */
    public int winGetCaretPosY() {
        return this.activeXComponent.invoke("WinGetCaretPosY").getInt();
    }

    /**
     * Win get class list string.
     *
     * @param title the title
     * @param text  the text
     * @return the string
     */
    public String winGetClassList(String title, String text) {
        var vTitle = new Variant(title);
        var vText = new Variant(text);
        var params = new Variant[] {vTitle, vText};
        Variant result = this.activeXComponent.invoke("WinGetClassList", params);
        return this.safeString(result);
    }

    /**
     * Win get client size width int.
     *
     * @param title the title
     * @param text  the text
     * @return the int
     */
    public int winGetClientSizeWidth(String title, String text) {
        var result = this.winVariant(title, text, "WinGetClientSizeWidth");
        return result.getInt();
    }

    /**
     * Win get client size height int.
     *
     * @param title the title
     * @param text  the text
     * @return the int
     */
    public int winGetClientSizeHeight(String title, String text) {
        var result = this.winVariant(title, text, "WinGetClientSizeHeight");
        return result.getInt();
    }

    /**
     * Win get client size width int.
     *
     * @param title the title
     * @return the int
     */
    public int winGetClientSizeWidth(String title) {
        var result = this.winVariant(title, "WinGetClientSizeWidth");
        return result.getInt();
    }

    /**
     * Win get client size height int.
     *
     * @param title the title
     * @return the int
     */
    public int winGetClientSizeHeight(String title) {
        var result = this.winVariant(title, "WinGetClientSizeHeight");
        return result.getInt();
    }

    private String safeString(Variant v) {
        return v.getvt() == 8 ? v.getString() : "";
    }

    /**
     * Win get handle string.
     *
     * @param title the title
     * @param text  the text
     * @return the string
     */
    public String winGetHandle(String title, String text) {
        var result = this.winVariant(title, text, "WinGetHandle");
        return result.getString();
    }

    /**
     * Win get handle string.
     *
     * @param title the title
     * @return the string
     */
    public String winGetHandle(String title) {
        var result = this.winVariant(title, "WinGetHandle");
        return result.getString();
    }

    /**
     * Win get pos x int.
     *
     * @param title the title
     * @param text  the text
     * @return the int
     */
    public int winGetPosX(String title, String text) {
        return this.winVariant(title, text, "WinGetPosX").getInt();
    }

    /**
     * Win get pos x int.
     *
     * @param title the title
     * @return the int
     */
    public int winGetPosX(String title) {
        return this.winVariant(title, "WinGetPosX").getInt();
    }

    /**
     * Win get pos y int.
     *
     * @param title the title
     * @param text  the text
     * @return the int
     */
    public int winGetPosY(String title, String text) {
        var result = this.winVariant(title, text, "WinGetPosY");
        return result.getInt();
    }

    /**
     * Win get pos y int.
     *
     * @param title the title
     * @return the int
     */
    public int winGetPosY(String title) {
        var result = this.winVariant(title, "WinGetPosY");
        return result.getInt();
    }

    /**
     * Win get pos width int.
     *
     * @param title the title
     * @param text  the text
     * @return the int
     */
    public int winGetPosWidth(String title, String text) {
        var result = this.winVariant(title, text, "WinGetPosWidth");
        return result.getInt();
    }

    /**
     * Win get pos width int.
     *
     * @param title the title
     * @return the int
     */
    public int winGetPosWidth(String title) {
        var result = this.winVariant(title, "WinGetPosWidth");
        return result.getInt();
    }

    /**
     * Win get pos height int.
     *
     * @param title the title
     * @param text  the text
     * @return the int
     */
    public int winGetPosHeight(String title, String text) {
        var result = this.winVariant(title, text, "WinGetPosHeight");
        return result.getInt();
    }

    /**
     * Win get pos height int.
     *
     * @param title the title
     * @return the int
     */
    public int winGetPosHeight(String title) {
        var result = this.winVariant(title, "WinGetPosHeight");
        return result.getInt();
    }

    /**
     * Win get process string.
     *
     * @param title the title
     * @param text  the text
     * @return the string
     */
    public String winGetProcess(String title, String text) {
        var v = this.winVariant(title, text, "WinGetProcess");
        return v.getString();
    }

    /**
     * Win get process string.
     *
     * @param title the title
     * @return the string
     */
    public String winGetProcess(String title) {
        var v = this.winVariant(title, "WinGetProcess");
        return v.getString();
    }

    /**
     * Win get state int.
     *
     * @param title the title
     * @param text  the text
     * @return the int
     */
    public int winGetState(String title, String text) {
        var result = this.winVariant(title, text, "WinGetState");
        return result.getInt();
    }

    /**
     * Win get state int.
     *
     * @param title the title
     * @return the int
     */
    public int winGetState(String title) {
        var result = this.winVariant(title, "WinGetState");
        return result.getInt();
    }

    /**
     * Win get text string.
     *
     * @param title the title
     * @param text  the text
     * @return the string
     */
    public String winGetText(String title, String text) {
        var result = this.winVariant(title, text, "WinGetText");
        return result.getString();
    }

    /**
     * Win get text string.
     *
     * @param title the title
     * @return the string
     */
    public String winGetText(String title) {
        var result = this.winVariant(title, "WinGetText");
        return result.getString();
    }

    /**
     * Win get title string.
     *
     * @param title the title
     * @param text  the text
     * @return the string
     */
    public String winGetTitle(String title, String text) {
        var result = this.winVariant(title, text, "WinGetTitle");
        return result.getvt() == 8 ? result.getString() : "";
    }

    /**
     * Win get title string.
     *
     * @param title the title
     * @return the string
     */
    public String winGetTitle(String title) {
        var result = this.winVariant(title, "WinGetTitle");
        return result.getvt() == 8 ? result.getString() : "";
    }

    /**
     * Win kill.
     *
     * @param title the title
     * @param text  the text
     */
    public void winKill(String title, String text) {
        this.winVariant(title, text, "WinKill");
    }

    /**
     * Win kill.
     *
     * @param title the title
     */
    public void winKill(String title) {
        this.winVariant(title, "WinKill");
    }

    /**
     * Win list string [ ] [ ].
     *
     * @param title the title
     * @param text  the text
     * @return the string [ ] [ ]
     */
    public String[][] winList(String title, String text) {
        var result = this.winVariant(title, text, "WinList");
        var arr = result.toSafeArray();
        var entries = arr.getInt(0, 0);
        var resultArr = new String[2][entries + 1];

        for (var i = 0; i <= entries; ++i) {
            resultArr[0][i] = arr.getString(0, i);
            resultArr[1][i] = arr.getString(1, i);
        }

        return resultArr;
    }

    /**
     * Win list string [ ] [ ].
     *
     * @param title the title
     * @return the string [ ] [ ]
     */
    public String[][] winList(String title) {
        var result = this.winVariant(title, "WinList");
        var arr = result.toSafeArray();
        var entries = arr.getInt(0, 0);
        var resultArr = new String[2][entries + 1];

        for (var i = 0; i <= entries; ++i) {
            resultArr[0][i] = arr.getString(0, i);
            resultArr[1][i] = arr.getString(1, i);
        }

        return resultArr;
    }

    /**
     * Win menu select item boolean.
     *
     * @param title the title
     * @param text  the text
     * @param item  the item
     * @return the boolean
     */
    public boolean winMenuSelectItem(String title, String text, String item) {
        var vTitle = new Variant(title);
        var vText = new Variant(text);
        var vItem = new Variant(item);
        var params = new Variant[] {vTitle, vText, vItem};
        Variant result = this.activeXComponent.invoke(WIN_MENU_SELECT_ITEM, params);
        return this.oneToTrue(result.getInt(), WIN_MENU_SELECT_ITEM);
    }

    /**
     * Win menu select item boolean.
     *
     * @param title the title
     * @param text  the text
     * @param item  the item
     * @param item2 the item 2
     * @return the boolean
     */
    public boolean winMenuSelectItem(String title, String text, String item, String item2) {
        var vTitle = new Variant(title);
        var vText = new Variant(text);
        var vItem = new Variant(item);
        var vItem2 = new Variant(item2);
        var params = new Variant[] {vTitle, vText, vItem, vItem2};
        Variant result = this.activeXComponent.invoke(WIN_MENU_SELECT_ITEM, params);
        return this.oneToTrue(result.getInt(), WIN_MENU_SELECT_ITEM);
    }

    /**
     * Win menu select item boolean.
     *
     * @param title the title
     * @param text  the text
     * @param item  the item
     * @param item2 the item 2
     * @param item3 the item 3
     * @return the boolean
     */
    public boolean winMenuSelectItem(String title, String text, String item, String item2, String item3) {
        var vTitle = new Variant(title);
        var vText = new Variant(text);
        var vItem = new Variant(item);
        var vItem2 = new Variant(item2);
        var vItem3 = new Variant(item3);
        var params = new Variant[] {vTitle, vText, vItem, vItem2, vItem3};
        Variant result = this.activeXComponent.invoke(WIN_MENU_SELECT_ITEM, params);
        return this.oneToTrue(result.getInt(), WIN_MENU_SELECT_ITEM);
    }

    /**
     * Win menu select item boolean.
     *
     * @param title the title
     * @param text  the text
     * @param item  the item
     * @param item2 the item 2
     * @param item3 the item 3
     * @param item4 the item 4
     * @param item5 the item 5
     * @param item6 the item 6
     * @param item7 the item 7
     * @return the boolean
     */
    public boolean winMenuSelectItem(
            String title,
            String text,
            String item,
            String item2,
            String item3,
            String item4,
            String item5,
            String item6,
            String item7) {
        var vTitle = new Variant(title);
        var vText = new Variant(text);
        var vItem = new Variant(item);
        var vItem2 = new Variant(item2);
        var vItem3 = new Variant(item3);
        var vItem4 = new Variant(item4);
        var vItem5 = new Variant(item5);
        var vItem6 = new Variant(item6);
        var vItem7 = new Variant(item7);
        var params = new Variant[] {vTitle, vText, vItem, vItem2, vItem3, vItem4, vItem5, vItem6, vItem7};
        Variant result = this.activeXComponent.invoke(WIN_MENU_SELECT_ITEM, params);
        return this.oneToTrue(result.getInt(), WIN_MENU_SELECT_ITEM);
    }

    /**
     * Win minimize all.
     */
    public void winMinimizeAll() {
        this.activeXComponent.invoke("WinMinimizeAll");
    }

    /**
     * Win minimize all undo.
     */
    public void winMinimizeAllUndo() {
        this.activeXComponent.invoke("WinMinimizeAllUndo");
    }

    /**
     * Win move.
     *
     * @param title  the title
     * @param text   the text
     * @param x      the x
     * @param y      the y
     * @param width  the width
     * @param height the height
     */
    public void winMove(String title, String text, int x, int y, int width, int height) {
        var vTitle = new Variant(title);
        var vText = new Variant(text);
        var vX = new Variant(x);
        var vY = new Variant(y);
        var vWidth = new Variant(width);
        var vHeight = new Variant(height);
        var params = new Variant[] {vTitle, vText, vX, vY, vWidth, vHeight};
        this.activeXComponent.invoke("WinMove", params);
    }

    /**
     * Win move.
     *
     * @param title the title
     * @param text  the text
     * @param x     the x
     * @param y     the y
     */
    public void winMove(String title, String text, int x, int y) {
        var vTitle = new Variant(title);
        var vText = new Variant(text);
        var vX = new Variant(x);
        var vY = new Variant(y);
        var params = new Variant[] {vTitle, vText, vX, vY};
        this.activeXComponent.invoke("WinMove", params);
    }

    /**
     * Win set on top.
     *
     * @param title     the title
     * @param text      the text
     * @param isTopMost the is top most
     */
    public void winSetOnTop(String title, String text, boolean isTopMost) {
        var vTitle = new Variant(title);
        var vText = new Variant(text);
        var flag = 0;
        if (isTopMost) {
            flag = 1;
        }

        var vFlag = new Variant(flag);
        var params = new Variant[] {vTitle, vText, vFlag};
        this.activeXComponent.invoke("WinSetOnTop", params);
    }

    /**
     * Win set state.
     *
     * @param title the title
     * @param text  the text
     * @param flag  the flag
     */
    public void winSetState(String title, String text, int flag) {
        var vTitle = new Variant(title);
        var vText = new Variant(text);
        var vFlag = new Variant(flag);
        var params = new Variant[] {vTitle, vText, vFlag};
        this.activeXComponent.invoke("WinSetState", params);
    }

    /**
     * Win set title.
     *
     * @param title    the title
     * @param text     the text
     * @param newtitle the newtitle
     */
    public void winSetTitle(String title, String text, String newtitle) {
        var vTitle = new Variant(title);
        var vText = new Variant(text);
        var vNewtitle = new Variant(newtitle);
        var params = new Variant[] {vTitle, vText, vNewtitle};
        this.activeXComponent.invoke("WinSetTitle", params);
    }

    /**
     * Win set trans boolean.
     *
     * @param title        the title
     * @param text         the text
     * @param transparency the transparency
     * @return the boolean
     */
    public boolean winSetTrans(String title, String text, int transparency) {
        var vTitle = new Variant(title);
        var vText = new Variant(text);
        var vTransparency = new Variant(transparency);
        var params = new Variant[] {vTitle, vText, vTransparency};
        Variant result = this.activeXComponent.invoke("WinSetTrans", params);
        return result.getInt() != 0;
    }

    /**
     * Win wait boolean.
     *
     * @param title   the title
     * @param text    the text
     * @param timeout the timeout
     * @return the boolean
     */
    public boolean winWait(String title, String text, int timeout) {
        return this.winVariantBool(title, text, timeout, WIN_WAIT);
    }

    /**
     * Win wait boolean.
     *
     * @param title the title
     * @return the boolean
     */
    public boolean winWait(String title) {
        return this.winVariantBool(title, WIN_WAIT);
    }

    /**
     * Win wait boolean.
     *
     * @param title the title
     * @param text  the text
     * @return the boolean
     */
    public boolean winWait(String title, String text) {
        return this.winVariantBool(title, text, WIN_WAIT);
    }

    /**
     * Win wait active boolean.
     *
     * @param title   the title
     * @param text    the text
     * @param timeout the timeout
     * @return the boolean
     */
    public boolean winWaitActive(String title, String text, int timeout) {
        return this.winVariantBool(title, text, timeout, WIN_WAIT_ACTIVE);
    }

    /**
     * Win wait active boolean.
     *
     * @param title the title
     * @param text  the text
     * @return the boolean
     */
    public boolean winWaitActive(String title, String text) {
        return this.winVariantBool(title, text, WIN_WAIT_ACTIVE);
    }

    /**
     * Win wait active boolean.
     *
     * @param title the title
     * @return the boolean
     */
    public boolean winWaitActive(String title) {
        return this.winVariantBool(title, WIN_WAIT_ACTIVE);
    }

    /**
     * Win wait close boolean.
     *
     * @param title   the title
     * @param text    the text
     * @param timeout the timeout
     * @return the boolean
     */
    public boolean winWaitClose(String title, String text, int timeout) {
        return this.winVariantBool(title, text, timeout, WIN_WAIT_CLOSE);
    }

    /**
     * Win wait close boolean.
     *
     * @param title the title
     * @return the boolean
     */
    public boolean winWaitClose(String title) {
        return this.winVariantBool(title, WIN_WAIT_CLOSE);
    }

    /**
     * Win wait close boolean.
     *
     * @param title the title
     * @param text  the text
     * @return the boolean
     */
    public boolean winWaitClose(String title, String text) {
        return this.winVariantBool(title, text, WIN_WAIT_CLOSE);
    }

    /**
     * Win wait no active boolean.
     *
     * @param title   the title
     * @param text    the text
     * @param timeout the timeout
     * @return the boolean
     */
    public boolean winWaitNoActive(String title, String text, int timeout) {
        return this.winVariantBool(title, text, timeout, WIN_WAIT_NOT_ACTIVE);
    }

    /**
     * Win wait no active boolean.
     *
     * @param title the title
     * @return the boolean
     */
    public boolean winWaitNoActive(String title) {
        return this.winVariantBool(title, WIN_WAIT_NOT_ACTIVE);
    }

    /**
     * Win wait no active boolean.
     *
     * @param title the title
     * @param text  the text
     * @return the boolean
     */
    public boolean winWaitNoActive(String title, String text) {
        return this.winVariantBool(title, text, WIN_WAIT_NOT_ACTIVE);
    }

    private boolean winVariantBool(String title, String text, int timeout, String function) {
        var vTitle = new Variant(title);
        var vText = new Variant(text);
        var vTimeout = new Variant(timeout);
        var params = new Variant[] {vTitle, vText, vTimeout};
        Variant result = this.activeXComponent.invoke(function, params);
        return this.oneToTrue(result.getInt(), function);
    }

    private boolean winVariantBool(String title, String text, String function) {
        var vTitle = new Variant(title);
        var vText = new Variant(text);
        var params = new Variant[] {vTitle, vText};
        Variant result = this.activeXComponent.invoke(function, params);
        return this.oneToTrue(result.getInt(), function);
    }

    private boolean winVariantBool(String title, String function) {
        var vTitle = new Variant(title);
        var params = new Variant[] {vTitle};
        Variant result = this.activeXComponent.invoke(function, params);
        return this.oneToTrue(result.getInt(), function);
    }

    /**
     * Statusbar get text by title string.
     *
     * @param title the title
     * @return the string
     */
    public String statusbarGetTextByTitle(String title) {
        return this.activeXComponent.invoke(STATUSBAR_GET_TEXT, title).getString();
    }

    private boolean oneToTrue(int i, String function) {
        if (i != 1) {
            throw new RuntimeException(function + IS_NOT_COMPLETE);
        } else {
            return true;
        }
    }

    private boolean oneToBool(int i) {
        return i == 1;
    }

    private boolean oneToTrue(Variant v, String function) {
        if (v.getvt() != 3 && v.getvt() != 2) {
            throw new RuntimeException(function + IS_NOT_COMPLETE);
        } else if (v.getInt() != 1) {
            throw new RuntimeException(function + IS_NOT_COMPLETE);
        } else {
            return true;
        }
    }
}
