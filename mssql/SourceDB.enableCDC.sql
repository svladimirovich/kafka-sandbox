-- enable CDC for database
USE SourceDB
GO
EXEC sys.sp_cdc_enable_db
GO


-- enable CDC for each table that we need to track
EXEC sys.sp_cdc_enable_table
    @source_schema = N'dbo',
    @source_name   = N'Users',
    @role_name     = NULL,
    @supports_net_changes = 1
    GO


EXEC sys.sp_cdc_enable_table
    @source_schema = N'dbo',
    @source_name   = N'Topics',
    @role_name     = NULL,
    @supports_net_changes = 1
    GO


EXEC sys.sp_cdc_enable_table
    @source_schema = N'dbo',
    @source_name   = N'Articles',
    @role_name     = NULL,
    @supports_net_changes = 1
    GO


EXEC sys.sp_cdc_enable_table
    @source_schema = N'dbo',
    @source_name   = N'Subscriptions',
    @role_name     = NULL,
    @supports_net_changes = 1
    GO
