create database ProyectoUnidad2;

use ProyectoUnidad2;

select * from estudiante;
select * from reaccion join estudiante limit 5;

DROP DATABASE ProyectoUnidad2;

--  CONSULTAS
-- 1. Conteos generales
SELECT COUNT(*) AS total_estudiantes FROM estudiante;
SELECT COUNT(*) AS total_hobbies FROM hobby;
SELECT COUNT(*) AS total_reacciones FROM reaccion;
SELECT COUNT(*) AS total_matches FROM matches;
SELECT COUNT(*) AS total_relaciones_hobby FROM estudiante_hobby;

-- 2. Verificar integridad y duplicados
-- 2.1 Correos duplicados
SELECT correo_inst, COUNT(*) AS repeticiones
FROM estudiante
GROUP BY correo_inst
HAVING COUNT(*) > 1;

-- 2.2 Hobbies duplicados por nombre
SELECT nombre, COUNT(*) AS repeticiones
FROM hobby
GROUP BY nombre
HAVING COUNT(*) > 1;

-- 2.3 Relaciones duplicadas en estudiante_hobby
SELECT id_estudiante, id_hobby, COUNT(*) AS repeticiones
FROM estudiante_hobby
GROUP BY id_estudiante, id_hobby
HAVING COUNT(*) > 1;

-- 2.4 Reacciones duplicadas entre el mismo emisor y receptor
SELECT id_emisor, id_receptor, COUNT(*) AS repeticiones
FROM reaccion
GROUP BY id_emisor, id_receptor
HAVING COUNT(*) > 1;

-- 2.5 Matches duplicados exactos
SELECT estudiante1_id, estudiante2_id, COUNT(*) AS repeticiones
FROM matches
GROUP BY estudiante1_id, estudiante2_id
HAVING COUNT(*) > 1;

-- 2.6 Matches invertidos (A,B) y (B,A)
SELECT m1.id_match AS match1, m2.id_match AS match2,
       m1.estudiante1_id, m1.estudiante2_id
FROM matches m1
JOIN matches m2
  ON m1.estudiante1_id = m2.estudiante2_id
 AND m1.estudiante2_id = m2.estudiante1_id
 AND m1.id_match <> m2.id_match;
 
 -- 3. Verificar consistencia de relaciones
 -- 3.1 Reacciones con estudiantes inexistentes
 SELECT r.*
FROM reaccion r
LEFT JOIN estudiante e1 ON r.id_emisor = e1.id_estudiante
LEFT JOIN estudiante e2 ON r.id_receptor = e2.id_estudiante
WHERE e1.id_estudiante IS NULL OR e2.id_estudiante IS NULL;

-- 3.2 Matches con estudiantes inexistentes
SELECT m.*
FROM matches m
LEFT JOIN estudiante e1 ON m.estudiante1_id = e1.id_estudiante
LEFT JOIN estudiante e2 ON m.estudiante2_id = e2.id_estudiante
WHERE e1.id_estudiante IS NULL OR e2.id_estudiante IS NULL;

-- 3.3 Relaciones hobby con ids inexistentes
SELECT eh.*
FROM estudiante_hobby eh
LEFT JOIN estudiante e ON eh.id_estudiante = e.id_estudiante
LEFT JOIN hobby h ON eh.id_hobby = h.id_hobby
WHERE e.id_estudiante IS NULL OR h.id_hobby IS NULL;

-- 4. Verificar reglas de negocio visibles
-- 4.1 Estudiantes activos vs inactivos
SELECT activo, COUNT(*) AS total
FROM estudiante
GROUP BY activo;

-- 4.2 Estudiantes sin hobbies
SELECT e.id_estudiante, e.nombre, e.ap_pat, e.correo_inst
FROM estudiante e
LEFT JOIN estudiante_hobby eh ON e.id_estudiante = eh.id_estudiante
WHERE eh.id_hobby IS NULL;

-- 4.3 Matches cuyo origen realmente fue LIKE mutuo
SELECT m.id_match, m.estudiante1_id, m.estudiante2_id
FROM matches m
LEFT JOIN reaccion r1
  ON r1.id_emisor = m.estudiante1_id
 AND r1.id_receptor = m.estudiante2_id
 AND r1.tipo = 'LIKE'
LEFT JOIN reaccion r2
  ON r2.id_emisor = m.estudiante2_id
 AND r2.id_receptor = m.estudiante1_id
 AND r2.tipo = 'LIKE'
WHERE r1.id_reaccion IS NULL OR r2.id_reaccion IS NULL;

-- 5. Consultas “bonitas” para inspección manual
-- 5.1 Ver estudiantes
SELECT id_estudiante, nombre, ap_pat, ap_mat, correo_inst, carrera, activo
FROM estudiante
ORDER BY id_estudiante;

-- 5.2 Ver hobbies por estudiante
SELECT e.id_estudiante, e.nombre, e.ap_pat, h.nombre AS hobby
FROM estudiante e
JOIN estudiante_hobby eh ON e.id_estudiante = eh.id_estudiante
JOIN hobby h ON eh.id_hobby = h.id_hobby
ORDER BY e.id_estudiante, h.nombre;

-- 5.3 Ver reacciones
SELECT r.id_reaccion,
       e1.nombre AS emisor,
       e2.nombre AS receptor,
       r.tipo,
       r.fecha
FROM reaccion r
JOIN estudiante e1 ON r.id_emisor = e1.id_estudiante
JOIN estudiante e2 ON r.id_receptor = e2.id_estudiante
ORDER BY r.id_reaccion;

-- 5.4 Ver matches con nombres
SELECT m.id_match,
       e1.nombre AS estudiante1,
       e2.nombre AS estudiante2,
       m.fecha_match
FROM matches m
JOIN estudiante e1 ON m.estudiante1_id = e1.id_estudiante
JOIN estudiante e2 ON m.estudiante2_id = e2.id_estudiante
ORDER BY m.id_match;

DELETE FROM matches
WHERE id_match = 2;

INSERT INTO estudiante_hobby (id_estudiante, id_hobby) VALUES (1, 1);
INSERT INTO estudiante_hobby (id_estudiante, id_hobby) VALUES (1, 3);