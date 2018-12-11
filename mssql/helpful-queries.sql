-- check if CDC enabled
SELECT name, database_id, is_cdc_enabled FROM sys.databases

-- enable CDC
USE SourceDB
GO
EXEC sys.sp_cdc_enable_db
GO

-- check which Tables have CDC enabled on them
SELECT
	s.name AS Schema_Name,
	tb.name AS Table_Name,
	tb.object_id,
	tb.type,
	tb.type_desc,
	tb.is_tracked_by_cdc,
	*
FROM
	sys.tables tb
INNER JOIN sys.schemas s on s.schema_id = tb.schema_id
WHERE
	tb.is_tracked_by_cdc = 1


-- enable CDC for each table that we need to track
EXEC sys.sp_cdc_enable_table
    @source_schema = N'dbo',
    @source_name   = N'Users', -- table name
    @role_name     = NULL,
    @supports_net_changes = 1
    GO
