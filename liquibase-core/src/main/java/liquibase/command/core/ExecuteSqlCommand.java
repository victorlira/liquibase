package liquibase.command.core;

import liquibase.Scope;
import liquibase.command.*;
import liquibase.database.Database;
import liquibase.exception.LiquibaseException;
import liquibase.executor.Executor;
import liquibase.executor.ExecutorService;
import liquibase.statement.core.RawSqlStatement;
import liquibase.util.FileUtil;
import liquibase.util.StringUtil;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class ExecuteSqlCommand extends AbstractCommand {

    public static final CommandArgumentDefinition<Database> DATABASE_ARG;
    public static final CommandArgumentDefinition<String> SQL_ARG;
    public static final CommandArgumentDefinition<String> SQLFILE_ARG;
    public static final CommandArgumentDefinition<String> DELIMTER_ARG;

    static {
        final CommandArgumentDefinition.Builder builder = new CommandArgumentDefinition.Builder(ExecuteSqlCommand.class);
        DATABASE_ARG = builder.define("database", Database.class).required().build();
        SQL_ARG = builder.define("sql", String.class).build();
        SQLFILE_ARG = builder.define("sqlFile", String.class).build();
        DELIMTER_ARG = builder.define("delimiter", String.class).defaultValue(";").build();
    }

    @Override
    public String[] getName() {
        return new String[]{"executeSql"};
    }
    @Override
    public void run(CommandScope commandScope) throws Exception {
        Database database = DATABASE_ARG.getValue(commandScope);
        String sql = SQL_ARG.getValue(commandScope);
        String sqlFile = SQLFILE_ARG.getValue(commandScope);

        Executor executor = Scope.getCurrentScope().getSingleton(ExecutorService.class).getExecutor("jdbc", database);
        String sqlText;
        if (sqlFile == null) {
            sqlText = sql;
        } else {
            File file = new File(sqlFile);
            if (! file.exists()){
              throw new LiquibaseException(String.format("The file '%s' does not exist", file.getCanonicalPath()));
            }
            sqlText = FileUtil.getContents(file);
        }

        String out = "";
        String[] sqlStrings = StringUtil.processMutliLineSQL(sqlText, true, true, DELIMTER_ARG.getValue(commandScope));
        for (String sqlString : sqlStrings) {
            if (sqlString.toLowerCase().matches("\\s*select .*")) {
                List<Map<String, ?>> rows = executor.queryForList(new RawSqlStatement(sqlString));
                out += "Output of "+sqlString+":\n";
                if (rows.isEmpty()) {
                    out += "-- Empty Resultset --\n";
                } else {
                    SortedSet<String> keys = new TreeSet<>();
                    for (Map<String, ?> row : rows) {
                        keys.addAll(row.keySet());
                    }
                    out += StringUtil.join(keys, " | ")+" |\n";

                    for (Map<String, ?> row : rows) {
                        for (String key : keys) {
                            out += row.get(key)+" | ";
                        }
                        out += "\n";
                    }
                }
            } else {
                executor.execute(new RawSqlStatement(sqlString));
                out += "Successfully Executed: "+ sqlString+"\n";
            }
            out += "\n";
        }
        database.commit();

        Scope.getCurrentScope().getUI().sendMessage(out.trim());
    }

}