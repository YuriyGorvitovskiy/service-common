SELECT s.id AS s_id, s.name AS s_name, s.managed_by AS s_managed_by, t.id AS t_id, t.name AS t_name, t.assigned_to AS t_assigned_to
  FROM story s
 INNER JOIN task t ON t.story_id = s.id
 WHERE s.id IN (13, 14, 15)
 ORDER BY s.managed_by ASC, t.assigned_to DESC