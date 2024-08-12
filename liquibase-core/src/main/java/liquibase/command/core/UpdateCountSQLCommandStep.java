package liquibase.command.core;

import liquibase.command.*;
import liquibase.integration.commandline.Main;

public class UpdateCountSQLCommandStep extends AbstractCliWrapperCommandStep {
    public static final CommandArgumentDefinition<String> CHANGELOG_FILE_ARG;
    public static final CommandArgumentDefinition<String> URL_ARG;
    public static final CommandArgumentDefinition<String> USERNAME_ARG;
    public static final CommandArgumentDefinition<String> PASSWORD_ARG;
    public static final CommandArgumentDefinition<String> LABELS_ARG;
    public static final CommandArgumentDefinition<String> CONTEXTS_ARG;
    public static final CommandArgumentDefinition<String> OUTPUT_FILE_ARG;
    public static final CommandArgumentDefinition<Integer> COUNT_ARG;

    static {
        CommandStepBuilder builder = new CommandStepBuilder(UpdateCountSQLCommandStep.class);
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
        COUNT_ARG = builder.argument("count", Integer.class).required()
            .description("The number of changes to generate SQL for").build();
    }

    @Override
    public String[] getName() {
        return new String[] {"updateCountSQL"};
    }

    @Override
    public void run(CommandResultsBuilder resultsBuilder) throws Exception {
        CommandScope commandScope = resultsBuilder.getCommandScope();

        String[] args = createParametersFromArgs(createArgs(commandScope), "count");
        int statusCode = Main.run(args);
        addStatusMessage(resultsBuilder, statusCode);
        resultsBuilder.addResult("statusCode", statusCode);
    }

    @Override
    public void adjustCommandDefinition(CommandDefinition commandDefinition) {
        commandDefinition.setShortDescription("Generate the SQL to deploy the specified number of changes");
        commandDefinition.setLongDescription("Generate the SQL to deploy the specified number of changes");
    }
}