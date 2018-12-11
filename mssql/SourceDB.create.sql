USE [master]
GO
/****** Object:  Database [SourceDB]    Script Date: 10.12.2018 16:47:12 ******/
CREATE DATABASE [SourceDB]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'SourceDB', FILENAME = N'/var/opt/mssql/data/SourceDB.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'SourceDB_log', FILENAME = N'/var/opt/mssql/data/SourceDB_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
GO
ALTER DATABASE [SourceDB] SET COMPATIBILITY_LEVEL = 140
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [SourceDB].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [SourceDB] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [SourceDB] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [SourceDB] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [SourceDB] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [SourceDB] SET ARITHABORT OFF 
GO
ALTER DATABASE [SourceDB] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [SourceDB] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [SourceDB] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [SourceDB] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [SourceDB] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [SourceDB] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [SourceDB] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [SourceDB] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [SourceDB] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [SourceDB] SET  DISABLE_BROKER 
GO
ALTER DATABASE [SourceDB] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [SourceDB] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [SourceDB] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [SourceDB] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [SourceDB] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [SourceDB] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [SourceDB] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [SourceDB] SET RECOVERY FULL 
GO
ALTER DATABASE [SourceDB] SET  MULTI_USER 
GO
ALTER DATABASE [SourceDB] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [SourceDB] SET DB_CHAINING OFF 
GO
ALTER DATABASE [SourceDB] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [SourceDB] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [SourceDB] SET DELAYED_DURABILITY = DISABLED 
GO
EXEC sys.sp_db_vardecimal_storage_format N'SourceDB', N'ON'
GO
ALTER DATABASE [SourceDB] SET QUERY_STORE = OFF
GO



USE [SourceDB]
GO
/****** Object:  Table [dbo].[Articles]    Script Date: 10.12.2018 16:47:12 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Articles](
	[id] [uniqueidentifier] NOT NULL,
	[title] [nvarchar](250) NOT NULL,
	[shortText] [text] NULL,
	[longText] [text] NULL,
	[authorId] [uniqueidentifier] NOT NULL,
 CONSTRAINT [PK_Articles] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Subscriptions]    Script Date: 10.12.2018 16:47:12 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Subscriptions](
	[id] [uniqueidentifier] NOT NULL,
	[userId] [uniqueidentifier] NOT NULL,
	[topicId] [uniqueidentifier] NOT NULL,
 CONSTRAINT [PK_Subscriptions] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Topics]    Script Date: 10.12.2018 16:47:12 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Topics](
	[id] [uniqueidentifier] NOT NULL,
	[title] [nvarchar](250) NOT NULL,
	[descriptionHtml] [text] NULL,
	[creationDate] [timestamp] NOT NULL,
	[ownerId] [uniqueidentifier] NOT NULL,
 CONSTRAINT [PK_Topics] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Users]    Script Date: 10.12.2018 16:47:12 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Users](
	[id] [uniqueidentifier] NOT NULL,
	[login] [nvarchar](50) NOT NULL,
	[firstName] [nvarchar](50) NULL,
	[lastName] [nvarchar](50) NULL,
	[email] [nvarchar](250) NULL,
 CONSTRAINT [PK_Users] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Index [IX_Subscriptions]    Script Date: 10.12.2018 16:47:12 ******/
CREATE UNIQUE NONCLUSTERED INDEX [IX_Subscriptions] ON [dbo].[Subscriptions]
(
	[topicId] ASC,
	[userId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
ALTER TABLE [dbo].[Articles]  WITH CHECK ADD  CONSTRAINT [FK_Articles_Users] FOREIGN KEY([authorId])
REFERENCES [dbo].[Users] ([id])
GO
ALTER TABLE [dbo].[Articles] CHECK CONSTRAINT [FK_Articles_Users]
GO
ALTER TABLE [dbo].[Subscriptions]  WITH CHECK ADD  CONSTRAINT [FK_Subscriptions_Topics] FOREIGN KEY([topicId])
REFERENCES [dbo].[Topics] ([id])
GO
ALTER TABLE [dbo].[Subscriptions] CHECK CONSTRAINT [FK_Subscriptions_Topics]
GO
ALTER TABLE [dbo].[Subscriptions]  WITH CHECK ADD  CONSTRAINT [FK_Subscriptions_Users] FOREIGN KEY([userId])
REFERENCES [dbo].[Users] ([id])
GO
ALTER TABLE [dbo].[Subscriptions] CHECK CONSTRAINT [FK_Subscriptions_Users]
GO
USE [master]
GO
ALTER DATABASE [SourceDB] SET  READ_WRITE 
GO
