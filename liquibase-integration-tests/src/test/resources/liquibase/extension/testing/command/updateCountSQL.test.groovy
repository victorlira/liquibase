package liquibase.extension.testing.command

CommandTests.define {
    command = ["updateCountSQL"]
    signature = """
Short Description: Generate the SQL to deploy the specified number of changes
Long Description: Generate the SQL to deploy the specified number of changes
Required Args:
  count (Integer) The number of changes to generate SQL for
  url (String) The JDBC database connection URL
Optional Args:
  changeLogFile (String) The root changelog
    Default: null
  contexts (String) Changeset contexts to match
    Default: null
  labels (String) Changeset labels to match
    Default: null
  outputFile (String) File for writing the SQL
    Default: null
  password (String) Password to use to connect to the database
    Default: null
  username (String) Username to use to connect to the database
    Default: null
"""

    run {
        arguments = [
                count        : 1,
                changeLogFile: "changelogs/hsqldb/complete/simple.changelog.xml",
        ]

        expectedResults = [
                statusMessage: "Successfully executed updateCountSQL",
                statusCode   : 0
        ]
    }
}