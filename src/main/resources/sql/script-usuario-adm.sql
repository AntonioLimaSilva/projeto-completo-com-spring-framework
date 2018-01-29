insert into usuario (nome, email, senha, ativo, data_nascimento) values
('administrador', 'adm@gmail.com', '$2a$10$TXi3LCJSt8H5pUK7PQa.gOMP2m7BP0CWEvEGwcDXwBhpS/K73bHCC', 1,'1989-11-23');

insert into grupo (nome) values ('Administrador');
insert into grupo (nome) values ('Vendedor');

insert into usuario_grupo (id_usuario, id_grupo) values (1, 1);
insert into usuario_grupo (id_usuario, id_grupo) values (1, 2);

insert into permissao (nome) values ('ROLE_CADASTRAR_VENDA');
insert into permissao (nome) values ('ROLE_CADASTRAR_CERVEJA');
insert into permissao (nome) values ('ROLE_CADASTRAR_CLIENTE');
insert into permissao (nome) values ('ROLE_CADASTRAR_USUARIO');
insert into permissao (nome) values ('ROLE_CADASTRAR_CIDADE');
insert into permissao (nome) values ('ROLE_CADASTRAR_ESTILO');

insert into grupo_permissao (id_grupo, id_permissao) values (1, 1);
insert into grupo_permissao (id_grupo, id_permissao) values (1, 2);
insert into grupo_permissao (id_grupo, id_permissao) values (1, 3);
insert into grupo_permissao (id_grupo, id_permissao) values (1, 4);
insert into grupo_permissao (id_grupo, id_permissao) values (1, 5);
insert into grupo_permissao (id_grupo, id_permissao) values (1, 6);

insert into estado (nome, sigla) values ('Acre', 'AC');
insert into estado (nome, sigla) values ('Bahia', 'BA');
insert into estado (nome, sigla) values ('Goiás', 'GO');
insert into estado (nome, sigla) values ('Minas Gerais', 'MG');
insert into estado (nome, sigla) values ('Santa Catarina', 'SC');
insert into estado (nome, sigla) values ('São Paulo', 'SP');


insert into cidade (nome, id_estado) values ('Rio Branco', 1);
insert into cidade (nome, id_estado) values ('Cruzeiro do Sul', 1);
insert into cidade (nome, id_estado) values ('Salvador', 2);
insert into cidade (nome, id_estado) values ('Porto Seguro', 2);
insert into cidade (nome, id_estado) values ('Santana', 2);
insert into cidade (nome, id_estado) values ('Goiânia', 3);
insert into cidade (nome, id_estado) values ('Itumbiara', 3);
insert into cidade (nome, id_estado) values ('Novo Brasil', 3);
insert into cidade (nome, id_estado) values ('Belo Horizonte', 4);
insert into cidade (nome, id_estado) values ('Uberlândia', 4);
insert into cidade (nome, id_estado) values ('Montes Claros', 4);
insert into cidade (nome, id_estado) values ('Florianópolis', 5);
insert into cidade (nome, id_estado) values ('Criciúma', 5);
insert into cidade (nome, id_estado) values ('Camboriú', 5);
insert into cidade (nome, id_estado) values ('Lages', 5);
insert into cidade (nome, id_estado) values ('São Paulo', 6);
insert into cidade (nome, id_estado) values ('Ribeirão Preto', 6);
insert into cidade (nome, id_estado) values ('Campinas', 6);
insert into cidade (nome, id_estado) values ('Santos', 6);