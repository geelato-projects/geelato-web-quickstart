/**
* 格式说明：每条语句之间必须用注释“@sql ”进行分割，@sql后跟sqlId
*/

-- 创建表
-- @sql createOneTable
CREATE TABLE IF NOT EXISTS $.tableName (
  @for i in $.addList
    `$.addList[i].name` $.addList[i].type
    @if !$.addList[i].nullable
      not null
    @/if
    @if $.addList[i].defaultValue!='' && $.addList[i].defaultValue!=null
      DEFAULT $.addList[i].defaultValue
    @/if
    @if $.addList[i].comment!='' && $.addList[i].comment!=null
      COMMENT $.addList[i].comment
    @/if
    @if i<$.addList.length-1
      ,
    @/if
  @/for
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT $.tableTitle;
UPDATE platform_dev_table SET table_name = '$.tableName' WHERE entity_name = '$.tableName';
@for i in $.uniqueList
  alter table $.tableName add unique key(`$.uniqueList[i].name`);
@/for
@if $.primaryKey!='' && $.primaryKey!=null
  ALTER TABLE $.tableName ADD PRIMARY KEY ($.primaryKey);
@/if

-- 更新表
-- @sql upgradeOneTable
@if $.tableTitle!='' && $.tableTitle!=null
  ALTER TABLE $.tableName COMMENT = $.tableTitle;
@/if

@for i in $.addList
  alter table $.tableName add $.addList[i].name $.addList[i].type
  @if !$.addList[i].nullable
    not null
  @/if
  @if $.addList[i].defaultValue!='' && $.addList[i].defaultValue!=null
    DEFAULT $.addList[i].defaultValue
  @/if
  @if $.addList[i].comment!='' && $.addList[i].comment!=null
    COMMENT $.addList[i].comment
  @/if
  ;
@/for

@for i in $.modifyList
  alter table $.tableName modify `$.modifyList[i].name` $.modifyList[i].type
  @if !$.modifyList[i].nullable
    not null
  @/if
  @if $.modifyList[i].defaultValue!='' && $.modifyList[i].defaultValue!=null
    DEFAULT $.modifyList[i].defaultValue
  @/if
  @if $.modifyList[i].comment!='' && $.modifyList[i].comment!=null
    COMMENT $.modifyList[i].comment
  @/if
  ;
@/for
@for i in $.indexList
  alter table $.tableName drop index `$.indexList[i].keyName`;
@/for

@if $.primaryKey!='' && $.primaryKey!=null
 ALTER TABLE $.tableName DROP PRIMARY KEY,ADD PRIMARY KEY ($.primaryKey);
@/if

@for i in $.uniqueList
alter table $.tableName add unique key(`$.uniqueList[i].name`);
@/for

-- 更改表
-- @sql createOrUpdateOneTable
@if !$.existsTable
  CREATE TABLE IF NOT EXISTS $.tableName (
    @for i in $.createList
      `$.createList[i].name` $.createList[i].type
      @if !$.createList[i].nullable
        not null
      @/if
      @if $.createList[i].defaultValue!='' && $.createList[i].defaultValue!=null
        DEFAULT $.createList[i].defaultValue
      @/if
      @if i<$.createList.length-1
        ,
      @/if
    @/for
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
@/if

@for i in $.uniqueList
  alter table $.tableName add unique key(`$.uniqueList[i].name`);
@/for

@if $.existsTable
  @for i in $.addList
    alter table $.tableName add $.addList[i].name $.addList[i].type
    @if !$.addList[i].nullable
      not null
    @/if
    @if $.addList[i].defaultValue!='' && $.addList[i].defaultValue!=null
      DEFAULT $.addList[i].defaultValue
    @/if
    ;
  @/for

  @for i in $.modifyList
    alter table $.tableName modify `$.modifyList[i].name` $.modifyList[i].type
    @if !$.modifyList[i].nullable
      not null
    @/if
    @if $.modifyList[i].defaultValue!='' && $.modifyList[i].defaultValue!=null
      DEFAULT $.modifyList[i].defaultValue
    @/if
    ;
  @/for
@/if

-- 创建表，id字段自增
-- @sql createOneTableAutoIncrement
CREATE TABLE IF NOT EXISTS $.tableName (id bigint(20) NOT NULL AUTO_INCREMENT,PRIMARY KEY (id)) ENGINE=InnoDB
DEFAULT CHARSET=utf8;
ALTER TABLE $.tableName
@for i in $.addList
  ADD COLUMN $.addList[i].name $.addList[i].type
  @if $.addList[i].nullable
    not null
  @/if
  @if $.addList[i].defaultValue!='' && $.addList[i].defaultValue!=null
    DEFAULT $.addList[i].defaultValue
  @/if
  @if i<$.addList.length-1
    ,
  @/if
@/for
@for i in $.uniqueList
  alter table $.tableName add unique key(`$.uniqueList[i].name`);
@/for

-- 删除表
-- @sql dropOneTable
DROP TABLE IF EXISTS $.tableName;

-- 查看列信息
-- @sql queryColumnsByTableName
SELECT * FROM information_schema.`COLUMNS` WHERE TABLE_SCHEMA='geelato' and TABLE_NAME='$.tableName';

-- 从数据库字典中同步表信息到平台的元数据表中
-- @sql syncTableSchemaToConfig
INSERT INTO xpm.platform_dev_table (
  TABLE_SCHEMA,
  TABLE_NAME,
  TABLE_TYPE,
  TABLE_COMMENT,
  title,
  linked,
  activity,
  create_date,
  update_date,
  creator,
  updater,
  description
) SELECT
    TABLE_SCHEMA,
    TABLE_NAME,
    TABLE_TYPE,
    TABLE_COMMENT,
    TABLE_COMMENT AS title,
    1 AS linked,
    1 AS activity,
    NOW() AS create_date,
    NOW() AS update_date,
    1 AS creator,
    1 AS updater,
    '' AS description
  FROM
    information_schema. TABLES
  WHERE
    TABLE_SCHEMA = 'xpm'
    AND TABLE_NAME NOT IN (
      SELECT
        table_name
      FROM
        xpm.platform_dev_table
    )

-- 在数据库中增加字段 INT (1)
-- @sql addColumn
ALTER TABLE $.tableName ADD COLUMN $.name $.type
@if $.nullable
  not null
@/if
@if $.defaultValue!=null
  DEFAULT $.defaultValue;
@/if


-- 在数据库中创建修改视图
-- @sql createOneView
CREATE OR REPLACE VIEW $.viewName
AS
    $.viewSql
;

-- 在数据库中创建修改视图
-- @sql createOneView
CREATE OR REPLACE VIEW $.viewName
AS
    $.viewSql
;