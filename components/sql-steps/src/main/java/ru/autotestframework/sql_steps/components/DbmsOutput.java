package ru.autotestframework.sql_steps.components;

import com.google.common.collect.Lists;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * This class execute procedures for prepare dbms_output
 */
@Slf4j
class DbmsOutput {

    private CallableStatement enable_stmt;
    private CallableStatement disable_stmt;
    private CallableStatement show_stmt;

    public DbmsOutput(Connection conn) throws SQLException {
        enable_stmt = conn.prepareCall("begin dbms_output.enable(:1); end;");
        disable_stmt = conn.prepareCall("begin dbms_output.disable; end;");

        show_stmt = conn.prepareCall("declare " + " l_line varchar2(2000); "
                + " l_done number; "
                + " l_buffer long; "
                + "begin "
                + " loop "
                + " exit when length(l_buffer)+255 > :maxbytes OR l_done = 1; "
                + " dbms_output.get_line( l_line, l_done ); "
                + " l_buffer := l_buffer || l_line || chr(100); "
                + " end loop; "
                + " :done := l_done; "
                + " :buffer := l_buffer; "
                + "end;");
    }

    /**
     * Execute update script
     *
     * @param size sets the designated parameter to the given Java int value.
     */
    public void enable(int size) throws SQLException {
        enable_stmt.setInt(1, size);
        enable_stmt.executeUpdate();
    }

    /**
     * Execute disable script
     */
    public void disable() throws SQLException {
        disable_stmt.executeUpdate();
    }

    /**
     * Get the log of the executed script
     *
     * @return List<String> logs data
     */
    public List<String> getLogs() throws SQLException {
        List<String> logs = Lists.newArrayList();
        show_stmt.registerOutParameter(2, java.sql.Types.INTEGER);
        show_stmt.registerOutParameter(3, java.sql.Types.VARCHAR);

        do {
            show_stmt.setInt(1, 32000);
            show_stmt.executeUpdate();
            logs.add(show_stmt.getString(3));
        } while (show_stmt.getInt(2) != 1);

        log.info(String.valueOf(logs));
        return logs;
    }
    /**
     * Releases statements object's database and JDBC resources immediately
     */
    public void close() throws SQLException {
        enable_stmt.close();
        disable_stmt.close();
        show_stmt.close();
    }
}
