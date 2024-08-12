package liquibase.command.core;

import liquibase.command.*;
import liquibase.command.AbstractCliWrapperCommandStep;
import liquibase.integration.commandline.Main;

import java.time.LocalDateTime;

public class RollbackToDateSQLCommandStep extends AbstractCliWrapperCommandStep {
    public static final CommandArgumentDefinition<String> CHANGELOG_FILE_ARG;
    public static final CommandArgumentDefinition<String> URL_ARG;
    public static final CommandArgumentDefinition<String> USERNAME_ARG;
    public static final CommandArgumentDefinition<String> PASSWORD_ARG;
    public static final CommandArgumentDefinition<String> LABELS_ARG;
    public static final CommandArgumentDefinition<String> CONTEXTS_ARG;
    public static final CommandArgumentDefinition<String> OUTPUT_FILE_ARG;
    public static final CommandArgumentDefinition<String> ROLLBACK_SCRIPT_ARG;
    public static final CommandArgumentDefinition<LocalDateTime> DATE_ARG;

    static {
        CommandStepBuilder builder = new CommandStepBuilder(RollbackToDateSQLCommandStep.class);
        URL_ARG = builder.argument("url", String.class).required()
            .description("The JDBC database connection URL").build();
        USERNAME_ARG = builder.argument("username", String.class)
            .description("Username to use to connect to the database").build();
        PASSWORD_ARG = builder.argument("password", String.class)
            .description("Password to use to connect to the database").build();
        CHANGELOG_FILE_ARG = builder.argument("changeLogFile", String.class)
            .description("The root changelog").build();
        LABELS_ARG = builder.argument("labels", String.class)
            .description("Changeset labels to match").build();
        CONTEXTS_ARG = builder.argument("contexts", String.class)
            .description("Changeset contexts to match").build();
        OUTPUT_FILE_ARG = builder.argument("outputFile", String.class)
            .description("File for writing the SQL").build();
        ROLLBACK_SCRIPT_ARG = builder.argument("rollbackScript", String.class)
            .description("Rollback script to execute").build();
        DATE_ARG = builder.argument("date", LocalDateTime.class).required()
            .description("Date to rollback changes to").build();
    }

    @Override
    public String[] getName() {
        return new String[] {"rollbackToDateSQL"};
    }

    @Override
    public void run(CommandResultsBuilder resultsBuilder) throws Exception {
        CommandScope commandScope = resultsBuilder.getCommandScope();

        String[] args = createParametersFromArgs(createArgs(commandScope), "date");
        int statusCode = Main.run(args);
        addStatusMessage(resultsBuilder, statusCode);
        resultsBuilder.addResult("statusCode", statusCode);
    }
    @Override
    public void adjustCommandDefinition(CommandDefinition commandDefinition) {
        commandDefinition.setShortDescription("Generate the SQL to rollback changes made to the database based on the specific date");
        commandDefinition.setLongDescription("Generate the SQL to rollback changes made to the database based on the specific date");
    }
}