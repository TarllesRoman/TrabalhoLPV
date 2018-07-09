CREATE TABLE aluno(
  id serial,
  nome character varying(64),
  sexo character varying(16),
  email character varying(64),
  cpf character varying(64),
  whatsapp character varying(64),
  altura double precision,
  peso double precision,
  data_nascimento date,
  CONSTRAINT pk_aluno_id PRIMARY KEY (id),
  CONSTRAINT uk_aluno_email UNIQUE (email)
);

CREATE TABLE atividade(
  id serial,
  id_aluno integer NOT NULL,
  data date NOT NULL,
  tempo character varying(16) NOT NULL,
  atividade character varying(64),
  duracao double precision,
  distancia double precision,
  calorias double precision,
  passos integer,
  CONSTRAINT pk_atividade_id_idaluno_data_tempo PRIMARY KEY (id, id_aluno, data, tempo),
  CONSTRAINT fk_atividade_aluno_id FOREIGN KEY (id_aluno)
      REFERENCES aluno (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT uk_atividade_idaluno_data_tempo UNIQUE (id_aluno, data, tempo),
  CONSTRAINT uk_id UNIQUE (id)
);

CREATE TABLE atividade_completa(
  id serial,
  id_atividade integer,
  velocidade_media double precision,
  velocidade_maxima double precision,
  ritmo_medio double precision,
  ritmo_maximo double precision,
  menor_elevacao double precision,
  maior_elevacao double precision,
  CONSTRAINT pk_atividadecompleta_id PRIMARY KEY (id),
  CONSTRAINT fk_atividadecompleta_atividade_id FOREIGN KEY (id_atividade)
      REFERENCES atividade (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE TABLE ritmo(
  id serial,
  id_atividade integer,
  km double precision,
  tempo double precision,
  CONSTRAINT pk_ritmo_id PRIMARY KEY (id),
  CONSTRAINT fk_ritmo_atividade_id FOREIGN KEY (id_atividade)
      REFERENCES public.atividade (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE TABLE usuario(
  id serial,
  usuario character varying(16),
  senha character varying(16),
  papel character varying(64),
  CONSTRAINT pk_usuario_id PRIMARY KEY (id)
);

INSERT INTO usuario (usuario, senha, papel) VALUES ('admin', 'admin', 'Administrador');