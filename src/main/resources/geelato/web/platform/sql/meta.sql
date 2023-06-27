-- 模型变更，表名变更
-- @sql metaResetOrDeleteTable
UPDATE platform_dev_column SET table_name = '$.newEntityName' , del_status = $.delStatus , enable_status = $.enableStatus WHERE table_name = '$.entityName';
UPDATE platform_dev_table_foreign SET main_table = '$.newEntityName' , del_status = $.delStatus , enable_status = $.enableStatus WHERE main_table = '$.entityName';
UPDATE platform_dev_table_foreign SET foreign_table = '$.newEntityName' WHERE foreign_table = '$.entityName';
UPDATE platform_dev_view SET entity_name = '$.newEntityName' , del_status = $.delStatus , enable_status = $.enableStatus WHERE entity_name = '$.entityName';
@if $.isTable
    ALTER TABLE $.entityName COMMENT = '$.newComment';
    RENAME TABLE $.entityName TO $.newEntityName;
@/if

-- 模型变更，字段变更
-- @sql metaDeleteColumn
UPDATE platform_dev_table_foreign
SET del_status = $.delStatus , enable_status = $.enableStatus , description = CONCAT('$.remark',description)
WHERE FIND_IN_SET('$.name',main_table_col) OR FIND_IN_SET('$.name',foreign_table_col);
@if $.isColumn
    alter table $.tableName CHANGE COLUMN `$.name` `$.newName` $.type
        @if !$.nullable
        not null
        @/if
        @if $.defaultValue!='' && $.defaultValue!=null
        @if $.dataType=='BIT'
        DEFAULT $.defaultValue
        @/if
        @if $.dataType!='BIT'
        DEFAULT '$.defaultValue'
        @/if
        @/if
        @if $.autoIncrement
        AUTO_INCREMENT
        @/if
        @if $.comment!='' && $.comment!=null
        COMMENT '$.comment'
        @/if
    ;
@/if

-- 模型变更，字段变更
-- @sql metaResetColumn
UPDATE platform_dev_table_foreign SET main_table_col = '$.formname' WHERE FIND_IN_SET('$.modelname',main_table_col);
UPDATE platform_dev_table_foreign SET foreign_table_col = '$.formname' WHERE FIND_IN_SET('$.modelname',foreign_table_col);
@if $.isColumn
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
@/if
@if $.isCopy
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
@/if