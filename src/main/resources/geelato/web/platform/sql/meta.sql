-- 模型变更，表名变更
-- @sql metaResetOrDeleteTable
UPDATE platform_dev_column
SET table_name = '$.newEntityName' , del_status = $.delStatus , enable_status = $.enableStatus , column_comment = CONCAT('$.remark',column_comment)
WHERE table_name = '$.entityName';
UPDATE platform_dev_table_foreign
SET main_table = '$.newEntityName' , del_status = $.delStatus , enable_status = $.enableStatus , description = CONCAT('$.remark',description)
WHERE main_table = '$.entityName';
UPDATE platform_dev_table_foreign SET foreign_table = '$.newEntityName' WHERE foreign_table = '$.entityName';
UPDATE platform_dev_view
SET entity_name = '$.newEntityName' , del_status = $.delStatus , enable_status = $.enableStatus , description = CONCAT('$.remark',description)
WHERE entity_name = '$.entityName';
@if $.isTable
ALTER TABLE $.entityName COMMENT = '$.newComment';
RENAME TABLE $.entityName TO $.newEntityName;
@/if

-- 模型变更，字段变更
-- @sql metaDeleteColumn
UPDATE platform_dev_table_foreign
SET del_status = $.delStatus , enable_status = $.enableStatus , description = CONCAT('$.remark',description)
WHERE FIND_IN_SET('$.columnName',main_table_col) OR FIND_IN_SET('$.columnName',foreign_table_col);

-- 模型变更，字段变更
-- @sql metaResetColumn
UPDATE platform_dev_table_foreign SET main_table_col = '$.formname' WHERE FIND_IN_SET('$.modelname',main_table_col);
UPDATE platform_dev_table_foreign SET foreign_table_col = '$.formname' WHERE FIND_IN_SET('$.modelname',foreign_table_col);
alter table $.modeltableName CHANGE COLUMN `$.modelname` `$.formname` $.formtype
    @if !$.formnullable
        not null
    @/if
    @if $.formdefaultValue!='' && $.formdefaultValue!=null
        @if $.formdataType=='BIT'
            DEFAULT $.formdefaultValue
        @/if
        @if $.formdataType!='BIT'
            DEFAULT '$.formdefaultValue'
        @/if
    @/if
    @if $.formautoIncrement
        AUTO_INCREMENT
    @/if
    @if $.formcomment!='' && $.formcomment!=null
        COMMENT '$.formcomment'
    @/if
;
alter table $.modeltableName add $.modelname $.modeltype
  @if !$.modelnullable
    not null
  @/if
  @if $.modeldefaultValue!='' && $.modeldefaultValue!=null
    @if $.modeldataType=='BIT'
      DEFAULT $.modeldefaultValue
    @/if
    @if $.modeldataType!='BIT'
      DEFAULT '$.modeldefaultValue'
    @/if
  @/if
  @if $.modelautoIncrement
     AUTO_INCREMENT
  @/if
  @if $.modelcomment!='' && $.modelcomment!=null
    COMMENT '$.modelcomment'
  @/if
;
UPDATE $.modeltableName set $.modelname = $.formname where 1=1;