BEGIN;

INSERT INTO rol (codigo, descripcion) VALUES
('ADMINISTRADOR', 'Rol para usuarios administrador'),
('USUARIO', 'Rol para usuarios sin privilegios');

INSERT INTO permiso (codigo, descripcion, prioridad) VALUES
('SEGUIDOR', 'Permiso de los usuarios seguidores de un equipo', 0),
('JUGADOR', 'Permiso de los usuarios que son jugadores de un equipo', 1),
('STAFF', 'Permiso de los usuarios que son staff de un equipo', 2),
('ADMINISTRADOR', 'Permiso de los usuarios que son administradores de un equipo', 3);

INSERT INTO posicion (codigo, descripcion) VALUES
('POR', 'Portero'),
('DFC', 'Defensa central'),
('DCI', 'Central izquierdo'),
('DCD', 'Central derecho'),
('LI', 'Lateral izquierdo'),
('LD', 'Lateral derecho'),
('MI', 'Interior izquierdo'),
('MD', 'Interior derecho'),
('MC', 'Medio centro'),
('MCD', 'Medio centro defensivo'),
('MCO', 'Media punta'),
('EI', 'Extremo izquierdo'),
('ED', 'Extremo derecho'),
('DC', 'Delantero centro'),
('SD', 'Segundo delantero'),
('PG', 'Base'),
('SG', 'Escolta'),
('SF', 'Alero'),
('PF', 'Ala pivot'),
('C', 'Pivot'),
('EXT', 'Extremo'),
('LT', 'Lateral'),
('PV', 'Pivote'),
('CT', 'Central');

INSERT INTO usuario (email, password, rol_id)
SELECT 'admin@teamapp.com', 'zYALHvVQj5KZ769G0UbB5W6mNlo=', r.id 
FROM rol r 
WHERE r.codigo = 'ADMINISTRADOR';

END;