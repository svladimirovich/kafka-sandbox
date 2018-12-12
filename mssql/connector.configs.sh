# https://docs.confluent.io/current/connect/kafka-connect-cdc-mssql/index.html
# This sh*t is not working
# Keeps giving me
# ERROR Exception thrown while querying for ChangeKey{databaseName=SourceDB, schemaName=dbo, tableName=Subscriptions}
#   (io.confluent.connect.cdc.mssql.QueryService:94) java.lang.NullPointerException: sourceOffset cannot be null.
#   at com.google.common.base.Preconditions.checkNotNull(Preconditions.java:226)
curl -X POST \
  http://localhost:3030/api/kafka-connect/connectors \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: adc213d0-dc6a-4c45-aa74-3b73fb798940' \
  -H 'cache-control: no-cache' \
  -d '{
  "name" : "MyMSSqlSourceConnector",
  "config" : {
    "connector.class" : "io.confluent.connect.cdc.mssql.MsSqlSourceConnector",
    "tasks.max" : "1",
    "topics" : "SourceDB.dbo.Users,SourceDB.dbo.Articles,SourceDB.dbo.Subscriptions",
    "initial.database" : "SourceDB",
    "username" : "sa",
    "password" : "simpleTest1",
    "server.name" : "mssql-source",
    "server.port" : "1433",
	"change.tracking.tables" : "dbo.Users,dbo.Articles,dbo.Subscriptions"    
  }
}'




# Also tried this crap, doesn't work at all. It even shows as sink instead of source in the landoop UI, WTF?
# https://debezium.io/docs/connectors/sqlserver/
curl -X PUT \
  /api/kafka-connect/connectors/my-mssql-connector/config \
  -H 'Content-Type: application/json' \
  -H 'Accept: application/json' \
  -d '{
	"name": "MyDebeziumSqlConnector",
	"config": {
		"connector.class": "io.debezium.connector.sqlserver.SqlServerConnector",
		"database.user": "sa",
		"database.dbname": "SourceDB",
		"database.hostname": "mssql-source",
		"database.password": "simpleTest1",
		"database.history.kafka.bootstrap.servers": "kafka-cluster:9092",
		"database.history.kafka.topic": "dbhistory-fulfillment",
		"database.server.name": "mssql-source",
		"database.port": "1433",
		"table.whitelist": "Users"
	}
}'