BEGIN;

--
-- CREACION DE SECUENCIAS
--

CREATE SEQUENCE sq_comentario_id;
CREATE SEQUENCE sq_convocatoria_id;
CREATE SEQUENCE sq_cuota_id;
CREATE SEQUENCE sq_cuota_jugador_id;
CREATE SEQUENCE sq_encuesta_id;
CREATE SEQUENCE sq_equipo_id;
CREATE SEQUENCE sq_evento_id;
CREATE SEQUENCE sq_jugador_id;
CREATE SEQUENCE sq_necesidad_id;
CREATE SEQUENCE sq_noticia_id;
CREATE SEQUENCE sq_opcion_id;
CREATE SEQUENCE sq_permiso_id;
CREATE SEQUENCE sq_posicion_id;
CREATE SEQUENCE sq_respuesta_id;
CREATE SEQUENCE sq_rol_id;
CREATE SEQUENCE sq_usuario_id;
CREATE SEQUENCE sq_usuario_equipo_id;

--
-- CREACIÓN TABLAS
--

-- COMENTARIO

CREATE TABLE comentario (
  id INT NOT NULL DEFAULT nextval('sq_comentario_id'),
  noticia_id INT NOT NULL,
  usuario_id INT NOT NULL,
  texto VARCHAR(255) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT (now()),
  modified_at TIMESTAMP,
  delete_at TIMESTAMP
);

-- CONVOCATORIA

CREATE TABLE convocatoria (
  id INT NOT NULL DEFAULT nextval('sq_convocatoria_id'),
  evento_id INT NOT NULL,
  jugador_id INT NOT NULL,
  asiste BOOL NOT NULL DEFAULT false,
  created_at TIMESTAMP NOT NULL DEFAULT (now()),
  modified_at TIMESTAMP,
  delete_at TIMESTAMP
);

-- CUOTA

CREATE TABLE cuota (
  id INT NOT NULL DEFAULT nextval('sq_cuota_id'),
  nombre VARCHAR(100) NOT NULL,
  cantidad NUMERIC(10,2) DEFAULT 0.00,
  equipo_id INT NOT NULL,
  fecha_fin TIMESTAMP,
  created_at TIMESTAMP NOT NULL DEFAULT (now()),
  modified_at TIMESTAMP,
  delete_at TIMESTAMP
);

-- CUOTA JUGADOR

CREATE TABLE cuota_jugador (
  id INT NOT NULL DEFAULT nextval('sq_cuota_jugador_id'),
  cuota_id INT NOT NULL,
  jugador_id INT NOT NULL,
  pagada bool NOT NULL DEFAULT false,
  fecha_pago TIMESTAMP
);

-- ENCUESTA

CREATE TABLE encuesta (
  id INT NOT NULL DEFAULT nextval('sq_encuesta_id'),
  titulo VARCHAR(100) NOT NULL,
  descripcion VARCHAR(255),
  multiple BOOL DEFAULT false,
  equipo_id INT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT (now()),
  modified_at TIMESTAMP,
  delete_at TIMESTAMP
);

-- EQUIPO

CREATE TABLE equipo (
  id INT NOT NULL DEFAULT nextval('sq_equipo_id'),
  nombre VARCHAR(100) NOT NULL,
  descripcion VARCHAR(255),
  created_at TIMESTAMP NOT NULL DEFAULT (now()),
  modified_at TIMESTAMP,
  delete_at TIMESTAMP
);

-- EVENTO

CREATE TABLE evento (
  id INT NOT NULL DEFAULT nextval('sq_evento_id'),
  titulo VARCHAR(100) NOT NULL,
  fecha_evento TIMESTAMP,
  equipo_id INT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT (now()),
  modified_at TIMESTAMP,
  delete_at TIMESTAMP
);

-- JUGADOR

CREATE TABLE jugador (
  id INT NOT NULL DEFAULT nextval('sq_equipo_id'),
  nombre VARCHAR(100) NOT NULL,
  dorsal INT,
  posicion_id INT,
  equipo_id INT NOT NULL,
  usuario_id INT,
  created_at TIMESTAMP NOT NULL DEFAULT (now()),
  modified_at TIMESTAMP,
  delete_at TIMESTAMP
);

-- NECESIDAD

CREATE TABLE necesidad (
  id INT NOT NULL DEFAULT nextval('sq_necesidad_id'),
  nombre VARCHAR(100) NOT NULL,
  evento_id INT NOT NULL,
  usuario_id INT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT (now()),
  modified_at TIMESTAMP,
  delete_at TIMESTAMP
);

-- NOTICIA

CREATE TABLE noticia (
  id INT NOT NULL DEFAULT nextval('sq_noticia_id'),
  titulo VARCHAR(100) NOT NULL,
  contenido VARCHAR(255),
  equipo_id INT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT (now()),
  modified_at TIMESTAMP,
  delete_at TIMESTAMP
);

-- OPCION

CREATE TABLE opcion (
  id INT NOT NULL DEFAULT nextval('sq_opcion_id'),
  encuesta_id INT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT (now()),
  modified_at TIMESTAMP,
  delete_at TIMESTAMP
);

-- PERMISO

CREATE TABLE permiso (
  id INT NOT NULL DEFAULT nextval('sq_permiso_id'),
  codigo VARCHAR(35) NOT NULL,
  descripcion VARCHAR(100),
  prioridad INT NOT NULL DEFAULT 0
);

-- POSICION

CREATE TABLE posicion (
  id INT NOT NULL DEFAULT nextval('sq_posicion_id'),
  codigo VARCHAR(35) UNIQUE NOT NULL,
  descripcion VARCHAR(100),
  created_at TIMESTAMP NOT NULL DEFAULT (now()),
  modified_at TIMESTAMP,
  delete_at TIMESTAMP
);

-- RESPUESTA

CREATE TABLE respuesta (
  id INT NOT NULL DEFAULT nextval('sq_respuesta_id'),
  valor BOOL NOT NULL DEFAULT false,
  usuario_id INT NOT NULL,
  opcion_id INT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT (now()),
  modified_at TIMESTAMP,
  delete_at TIMESTAMP
);

-- ROL

CREATE TABLE rol (
  id INT NOT NULL DEFAULT nextval('sq_rol_id'),
  codigo VARCHAR(35) NOT NULL,
  descripcion VARCHAR(100)
);

-- USUARIO

CREATE TABLE usuario (
  id INT NOT NULL DEFAULT nextval('sq_usuario_id'),
  email VARCHAR(100) UNIQUE NOT NULL,
  password VARCHAR(255),
  rol_id INT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT (now()),
  modified_at TIMESTAMP,
  delete_at TIMESTAMP
);

-- USUARIO EQUIPO

CREATE TABLE usuario_equipo (
  id INT NOT NULL DEFAULT nextval('sq_usuario_equipo_id'),
  usuario_id INT NOT NULL,
  equipo_id INT NOT NULL,
  permiso_id INT NOT NULL
);

--
-- COMENTARIOS
--

COMMENT ON TABLE comentario IS 'Tabla de comentarios';
COMMENT ON COLUMN comentario.id IS 'Id del comentario';
COMMENT ON COLUMN comentario.noticia_id IS 'Noticia del comentario';
COMMENT ON COLUMN comentario.usuario_id IS 'Usuario del comentario';
COMMENT ON COLUMN comentario.texto IS 'Texto del comentario';
COMMENT ON COLUMN comentario.created_at IS 'Fecha y hora de alta del comentario';
COMMENT ON COLUMN comentario.modified_at IS 'Fecha y hora de modificación del comentario';
COMMENT ON COLUMN comentario.delete_at IS 'Fecha y hora de baja del comentario';

COMMENT ON TABLE convocatoria IS 'Tabla de convocatorias';
COMMENT ON COLUMN convocatoria.id IS 'Id de la convocatoria';
COMMENT ON COLUMN convocatoria.evento_id IS 'Evento de la convocatoria';
COMMENT ON COLUMN convocatoria.jugador_id IS 'Jugador de la convocatoria';
COMMENT ON COLUMN convocatoria.created_at IS 'Fecha y hora de alta de la convocatoria';
COMMENT ON COLUMN convocatoria.modified_at IS 'Fecha y hora de modificación de la convocatoria';
COMMENT ON COLUMN convocatoria.delete_at IS 'Fecha y hora de baja de la convocatoria';

COMMENT ON TABLE cuota IS 'Tabla de cuotas';
COMMENT ON COLUMN cuota.id IS 'Id de la cuota';
COMMENT ON COLUMN cuota.nombre IS 'Nombre de la cuota';
COMMENT ON COLUMN cuota.cantidad IS 'Cantidad de la cuota';
COMMENT ON COLUMN cuota.equipo_id IS 'Equipo de la cuota';
COMMENT ON COLUMN cuota.fecha_fin IS 'Fecha y hora de fin de la cuota';
COMMENT ON COLUMN cuota.created_at IS 'Fecha y hora de alta de la cuota';
COMMENT ON COLUMN cuota.modified_at IS 'Fecha y hora de modificación de la cuota';
COMMENT ON COLUMN cuota.delete_at IS 'Fecha y hora de baja de la cuota';

COMMENT ON TABLE cuota_jugador IS 'Tabla de la relación entre cuotas y jugadores';
COMMENT ON COLUMN cuota_jugador.id IS 'Id de la relación';
COMMENT ON COLUMN cuota_jugador.cuota_id IS 'Cuota de la relación';
COMMENT ON COLUMN cuota_jugador.jugador_id IS 'Jugador de la relación';
COMMENT ON COLUMN cuota_jugador.pagada IS 'Indica si está pagada o no';
COMMENT ON COLUMN cuota_jugador.fecha_pago IS 'Fecha y hora de pago de la cuota';

COMMENT ON TABLE encuesta IS 'Tabla de encuestas';
COMMENT ON COLUMN encuesta.id IS 'Id de la encuesta';
COMMENT ON COLUMN encuesta.titulo IS 'Título de la encuesta';
COMMENT ON COLUMN encuesta.descripcion IS 'Descripción de la encuesta';
COMMENT ON COLUMN encuesta.multiple IS 'Indica si es de respuesa multiple la encuesta';
COMMENT ON COLUMN encuesta.equipo_id IS 'Equipo de la encuesta';
COMMENT ON COLUMN encuesta.created_at IS 'Fecha y hora de alta de la encuesta';
COMMENT ON COLUMN encuesta.modified_at IS 'Fecha y hora de modificación de la encuesta';
COMMENT ON COLUMN encuesta.delete_at IS 'Fecha y hora de baja de la encuesta';

COMMENT ON TABLE equipo IS 'Tabla de equipos';
COMMENT ON COLUMN equipo.id IS 'Id del equipo';
COMMENT ON COLUMN equipo.nombre IS 'Nombre del equipo';
COMMENT ON COLUMN equipo.descripcion IS 'Descripción del equipo';
COMMENT ON COLUMN equipo.created_at IS 'Fecha y hora de alta del equipo';
COMMENT ON COLUMN equipo.modified_at IS 'Fecha y hora de modificación del equipo';
COMMENT ON COLUMN equipo.delete_at IS 'Fecha y hora de baja del equipo';

COMMENT ON TABLE evento IS 'Tabla de eventos';
COMMENT ON COLUMN evento.id IS 'Id del evento';
COMMENT ON COLUMN evento.titulo IS 'Nombre del evento';
COMMENT ON COLUMN evento.fecha_evento IS 'Fecha y hora del evento';
COMMENT ON COLUMN evento.created_at IS 'Fecha y hora de alta del evento';
COMMENT ON COLUMN evento.modified_at IS 'Fecha y hora de modificación del evento';
COMMENT ON COLUMN evento.delete_at IS 'Fecha y hora de baja del evento';

COMMENT ON TABLE jugador IS 'Tabla de jugadores';
COMMENT ON COLUMN jugador.id IS 'Id del jugador';
COMMENT ON COLUMN jugador.nombre IS 'Nombre del jugador';
COMMENT ON COLUMN jugador.dorsal IS 'Dorsal del jugador';
COMMENT ON COLUMN jugador.posicion_id IS 'Posición del jugador';
COMMENT ON COLUMN jugador.equipo_id IS 'Equipo del jugador';
COMMENT ON COLUMN jugador.usuario_id IS 'Usuario del jugador';
COMMENT ON COLUMN jugador.created_at IS 'Fecha y hora de alta del jugador';
COMMENT ON COLUMN jugador.modified_at IS 'Fecha y hora de modificación del jugador';
COMMENT ON COLUMN jugador.delete_at IS 'Fecha y hora de baja del jugador';

COMMENT ON TABLE necesidad IS 'Tabla de necesidades';
COMMENT ON COLUMN necesidad.id IS 'Id de la necesidad';
COMMENT ON COLUMN necesidad.nombre IS 'Nombre de la necesidad';
COMMENT ON COLUMN necesidad.evento_id IS 'Evento que tiene la necesidad';
COMMENT ON COLUMN necesidad.usuario_id IS 'Usuario que se encarga de la necesidad';
COMMENT ON COLUMN necesidad.created_at IS 'Fecha y hora de alta de la necesidad';
COMMENT ON COLUMN necesidad.modified_at IS 'Fecha y hora de modificación de la necesidad';
COMMENT ON COLUMN necesidad.delete_at IS 'Fecha y hora de baja de la necesidad';

COMMENT ON TABLE noticia IS 'Tabla de noticias';
COMMENT ON COLUMN noticia.id IS 'Id de la noticia';
COMMENT ON COLUMN noticia.titulo IS 'Título de la noticia';
COMMENT ON COLUMN noticia.contenido IS 'Contenido de la noticia';
COMMENT ON COLUMN noticia.equipo_id IS 'Equipo de la noticia';
COMMENT ON COLUMN noticia.created_at IS 'Fecha y hora de alta de la noticia';
COMMENT ON COLUMN noticia.modified_at IS 'Fecha y hora de modificación de la noticia';
COMMENT ON COLUMN noticia.delete_at IS 'Fecha y hora de baja de la noticia';

COMMENT ON TABLE opcion IS 'Tabla de opciones';
COMMENT ON COLUMN opcion.id IS 'Id de la opción';
COMMENT ON COLUMN opcion.encuesta_id IS 'Encuesta de la opción';
COMMENT ON COLUMN opcion.created_at IS 'Fecha y hora de alta de la opción';
COMMENT ON COLUMN opcion.modified_at IS 'Fecha y hora de modificación de la opción';
COMMENT ON COLUMN opcion.delete_at IS 'Fecha y hora de baja de la opción';

COMMENT ON TABLE permiso IS 'Tabla de permisos';
COMMENT ON COLUMN permiso.id IS 'Id del permiso';
COMMENT ON COLUMN permiso.codigo IS 'Código del permiso';
COMMENT ON COLUMN permiso.descripcion IS 'Descripción del permiso';
COMMENT ON COLUMN permiso.prioridad IS 'Prioridad del permiso';

COMMENT ON TABLE posicion IS 'Tabla de posiciones';
COMMENT ON COLUMN posicion.id IS 'Id de la posicion';
COMMENT ON COLUMN posicion.codigo IS 'Código de la posicion';
COMMENT ON COLUMN posicion.descripcion IS 'Descripción de la posicion';
COMMENT ON COLUMN posicion.created_at IS 'Fecha y hora de alta de la posicion';
COMMENT ON COLUMN posicion.modified_at IS 'Fecha y hora de modificación de la posicion';
COMMENT ON COLUMN posicion.delete_at IS 'Fecha y hora de baja de la posicion';

COMMENT ON TABLE respuesta IS 'Tabla de respuestas';
COMMENT ON COLUMN respuesta.id IS 'Id de la respuesta';
COMMENT ON COLUMN respuesta.valor IS 'Valor de la respuesta';
COMMENT ON COLUMN respuesta.usuario_id IS 'Jugador de la respuesta';
COMMENT ON COLUMN respuesta.opcion_id IS 'Opción de la respuesta';
COMMENT ON COLUMN respuesta.created_at IS 'Fecha y hora de alta de la respuesta';
COMMENT ON COLUMN respuesta.modified_at IS 'Fecha y hora de modificación de la respuesta';
COMMENT ON COLUMN respuesta.delete_at IS 'Fecha y hora de baja de la respuesta';

COMMENT ON TABLE rol IS 'Tabla de roles';
COMMENT ON COLUMN rol.id IS 'Id del rol';
COMMENT ON COLUMN rol.codigo IS 'Código del rol';
COMMENT ON COLUMN rol.descripcion IS 'Descripción del rol';

COMMENT ON TABLE usuario IS 'Tabla de usuarios';
COMMENT ON COLUMN usuario.id IS 'Id del usuario';
COMMENT ON COLUMN usuario.email IS 'Email del usuario';
COMMENT ON COLUMN usuario.password IS 'Contraseña del usuario';
COMMENT ON COLUMN usuario.rol_id IS 'Rol asociado al usuario';
COMMENT ON COLUMN usuario.created_at IS 'Fecha y hora de alta del usuario';
COMMENT ON COLUMN usuario.modified_at IS 'Fecha y hora de modificación del usuario';
COMMENT ON COLUMN usuario.delete_at IS 'Fecha y hora de baja del usuario';

COMMENT ON TABLE usuario_equipo IS 'Tabla intermedia de usuario equipo y permisos';
COMMENT ON COLUMN usuario_equipo.id IS 'Id de la relación';
COMMENT ON COLUMN usuario_equipo.usuario_id IS 'usuario de la relación';
COMMENT ON COLUMN usuario_equipo.equipo_id IS 'Equipo de la relación';
COMMENT ON COLUMN usuario_equipo.permiso_id IS 'Permiso de la relación';

-- 
-- PROPIETARIOS SECUENCIAS
--

ALTER SEQUENCE sq_comentario_id OWNED BY comentario.id;
ALTER SEQUENCE sq_convocatoria_id OWNED BY convocatoria.id;
ALTER SEQUENCE sq_cuota_id OWNED BY cuota.id;
ALTER SEQUENCE sq_cuota_jugador_id OWNED BY cuota_jugador.id;
ALTER SEQUENCE sq_encuesta_id OWNED BY encuesta.id;
ALTER SEQUENCE sq_equipo_id OWNED BY equipo.id;
ALTER SEQUENCE sq_evento_id OWNED BY evento.id;
ALTER SEQUENCE sq_jugador_id OWNED BY jugador.id;
ALTER SEQUENCE sq_necesidad_id OWNED BY necesidad.id;
ALTER SEQUENCE sq_noticia_id OWNED BY noticia.id;
ALTER SEQUENCE sq_opcion_id OWNED BY opcion.id;
ALTER SEQUENCE sq_permiso_id OWNED BY permiso.id;
ALTER SEQUENCE sq_posicion_id OWNED BY posicion.id;
ALTER SEQUENCE sq_respuesta_id OWNED BY respuesta.id;
ALTER SEQUENCE sq_rol_id OWNED BY rol.id;
ALTER SEQUENCE sq_usuario_id OWNED BY usuario.id;
ALTER SEQUENCE sq_usuario_equipo_id OWNED BY usuario_equipo.id;

--
-- CLAVES PRIMARIAS
--

ALTER TABLE comentario ADD CONSTRAINT pk_comentario_id PRIMARY KEY (id);
ALTER TABLE convocatoria ADD CONSTRAINT pk_convocatoria_id PRIMARY KEY (id);
ALTER TABLE cuota ADD CONSTRAINT pk_cuota_id PRIMARY KEY (id);
ALTER TABLE cuota_jugador ADD CONSTRAINT pk_cuota_jugador_id PRIMARY KEY (id);
ALTER TABLE encuesta ADD CONSTRAINT pk_encuesta_id PRIMARY KEY (id);
ALTER TABLE equipo ADD CONSTRAINT pk_equipo_id PRIMARY KEY (id);
ALTER TABLE evento ADD CONSTRAINT pk_evento_id PRIMARY KEY (id);
ALTER TABLE jugador ADD CONSTRAINT pk_jugador_id PRIMARY KEY (id);
ALTER TABLE necesidad ADD CONSTRAINT pk_necesidad_id PRIMARY KEY (id);
ALTER TABLE noticia ADD CONSTRAINT pk_noticia_id PRIMARY KEY (id);
ALTER TABLE opcion ADD CONSTRAINT pk_opcion_id PRIMARY KEY (id);
ALTER TABLE permiso ADD CONSTRAINT pk_permiso_id PRIMARY KEY (id);
ALTER TABLE posicion ADD CONSTRAINT pk_posicion_id PRIMARY KEY (id);
ALTER TABLE respuesta ADD CONSTRAINT pk_respuesta_id PRIMARY KEY (id);
ALTER TABLE rol ADD CONSTRAINT pk_rol_id PRIMARY KEY (id);
ALTER TABLE usuario ADD CONSTRAINT pk_usuario_id PRIMARY KEY (id);
ALTER TABLE usuario_equipo ADD CONSTRAINT pk_usuario_equipo_id PRIMARY KEY (id);

--
-- CLAVES FORANEAS
--

ALTER TABLE comentario ADD CONSTRAINT fk_comentario_noticia_id FOREIGN KEY (noticia_id) REFERENCES noticia(id);
ALTER TABLE comentario ADD CONSTRAINT fk_comentario_usuario_id FOREIGN KEY (usuario_id) REFERENCES usuario(id);
ALTER TABLE convocatoria ADD CONSTRAINT fk_convocatoria_evento_id FOREIGN KEY (evento_id) REFERENCES evento(id);
ALTER TABLE convocatoria ADD CONSTRAINT fk_convocatoria_jugador_id FOREIGN KEY (jugador_id) REFERENCES jugador(id);
ALTER TABLE cuota ADD CONSTRAINT fk_cuota_equipo_id FOREIGN KEY (equipo_id) REFERENCES equipo(id);
ALTER TABLE cuota_jugador ADD CONSTRAINT fk_cuota_jugador_cuota_id FOREIGN KEY (cuota_id) REFERENCES cuota(id);
ALTER TABLE cuota_jugador ADD CONSTRAINT fk_cuota_jugador_jugador_id FOREIGN KEY (jugador_id) REFERENCES jugador(id);
ALTER TABLE encuesta ADD CONSTRAINT fk_encuesta_equipo_id FOREIGN KEY (equipo_id) REFERENCES equipo(id);
ALTER TABLE evento ADD CONSTRAINT fk_evento_equipo_id FOREIGN KEY (equipo_id) REFERENCES equipo(id);
ALTER TABLE jugador ADD CONSTRAINT fk_jugador_equipo_id FOREIGN KEY (equipo_id) REFERENCES equipo(id);
ALTER TABLE jugador ADD CONSTRAINT fk_jugador_posicion_id FOREIGN KEY (posicion_id) REFERENCES posicion(id);
ALTER TABLE jugador ADD CONSTRAINT fk_jugador_usuario_id FOREIGN KEY (usuario_id) REFERENCES usuario(id);
ALTER TABLE necesidad ADD CONSTRAINT fk_necesidad_evento_id FOREIGN KEY (evento_id) REFERENCES evento(id);
ALTER TABLE necesidad ADD CONSTRAINT fk_necesidad_usuario_id FOREIGN KEY (usuario_id) REFERENCES usuario(id);
ALTER TABLE noticia ADD CONSTRAINT fk_noticia_equipo_id FOREIGN KEY (equipo_id) REFERENCES equipo(id);
ALTER TABLE opcion ADD CONSTRAINT fk_opcion_encuesta_id FOREIGN KEY (encuesta_id) REFERENCES encuesta(id);
ALTER TABLE respuesta ADD CONSTRAINT fk_respuesta_usuario_id FOREIGN KEY (usuario_id) REFERENCES usuario(id);
ALTER TABLE respuesta ADD CONSTRAINT fk_respuesta_opcion_id FOREIGN KEY (opcion_id) REFERENCES opcion(id);
ALTER TABLE usuario ADD CONSTRAINT fk_usuario_rol_id FOREIGN KEY (rol_id) REFERENCES rol(id);
ALTER TABLE usuario_equipo ADD CONSTRAINT fk_usuario_equipo_usuario_id FOREIGN KEY (usuario_id) REFERENCES usuario(id);
ALTER TABLE usuario_equipo ADD CONSTRAINT fk_usuario_equipo_equipo_id FOREIGN KEY (equipo_id) REFERENCES equipo(id);
ALTER TABLE usuario_equipo ADD CONSTRAINT fk_usuario_equipo_permiso_id FOREIGN KEY (permiso_id) REFERENCES permiso(id);

--
-- INDEXes
--

CREATE UNIQUE INDEX ux_encuesta_titulo ON encuesta(titulo);
CREATE UNIQUE INDEX ux_equipo_nombre ON equipo(nombre);
CREATE UNIQUE INDEX ux_evento_titulo ON evento(titulo);
CREATE UNIQUE INDEX ux_jugador_nombre ON jugador(nombre);
CREATE UNIQUE INDEX ux_necesidad_nombre ON necesidad(nombre);
CREATE UNIQUE INDEX ux_noticia_titulo ON noticia(titulo);
CREATE UNIQUE INDEX ux_permiso_codigo ON permiso(codigo);
CREATE UNIQUE INDEX ux_posicion_codigo ON posicion(codigo);
CREATE UNIQUE INDEX ux_rol_email ON rol(codigo);
CREATE UNIQUE INDEX ux_usuario_email ON usuario(email);

END;