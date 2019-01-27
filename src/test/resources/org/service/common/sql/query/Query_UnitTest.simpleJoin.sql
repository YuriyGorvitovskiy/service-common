SELECT s.id AS story_id, s.name AS story_name, s.managed_by, t.id AS task_id, t.name AS task_name, t.assigned_to
  FROM story s
 INNER JOIN task t ON s.id = t.story_id
 WHERE s.id IN (13, 14, 15)
 ORDER BY s.managed_by ASC, t.assigned_to DESC