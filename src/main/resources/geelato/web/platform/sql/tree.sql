/**
* 格式说明：每条语句之间必须用注释“@sql ”进行分割，@sql后跟sqlId
*/

-- 基于platform_tree_node，支持动态业务表组合
-- @sql select_tree_node_left_join
select
  tn.id          tn_id,
  tn.text        tn_text,
  tn.parent      tn_parent,
  tn.icon        tn_icon,
  tn.`type`      tn_type,
  tn.tree_entity tn_tree_entity,
  tn.meta        tn_meta,
  t.*
from platform_tree_node tn left join $.tableName t on tn.id = t.tree_node_id
where tn.tree_id = $.treeId

-- @sql select_platform_menu
SELECT tn.id,tn.`TEXT` name,tn.parent parentId,tn.`type` component,tn.icon icon,tn.meta,tn.tree_id treeId
FROM platform_tree_node tn,platform_app_user app
WHERE tn.tree_id = app.id AND app.user_id=$.userId AND tn.flag='menuItem'
UNION
SELECT tn.id,tn.`TEXT` name,tn.parent parentId,tn.`type` component,tn.icon icon,tn.meta,tn.tree_id treeId
FROM platform_tree_node tn,platform_app_user app
WHERE tn.tree_id = app.id AND app.user_id=$.userId AND tn.id IN (SELECT tn.parent parentId FROM platform_tree_node tn,platform_app_user app
WHERE tn.tree_id = app.id AND app.user_id=$.userId AND tn.flag='menuItem')
UNION
SELECT id,`name`,'0' parentId,'default' component,icon,'' meta,id treeId
FROM platform_app_user au
WHERE au.user_id=$.userId
ORDER BY treeId,parentId ASC;

-- @sql select_platform_tree_node_app_page
SELECT
  p1.id,
  p1.pid,
  p2.id as appId,
  p3.id as pageId,
  p1.flag,
  p1.icon,
  p1.type,
  p1.meta,
  p1.tree_entity as entity,
  p1.text,
  p1.seq_no as seqNo,
  p1.tenant_code as tenantCode
FROM platform_tree_node p1
LEFT JOIN platform_app p2 ON p2.id = p1.tree_id
LEFT JOIN platform_app_page p3 ON p3.extend_id = p1.id AND p3.app_id = p2.id
WHERE 1=1
@if $.appId!=null&&$.appId!=''
  AND p2.id = '$.appId'
@/if
ORDER BY p2.seq_no ASC,p1.seq_no ASC