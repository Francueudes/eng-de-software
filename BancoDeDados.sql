use engsoft;

alter table tbreferencia add unidade varchar (100);

select * from tbpacient;

select * from tbreferencia;

create table tbreferencia(
id_ref int primary key auto_increment,
resumo varchar (1000),
diagnostico varchar(100),
prioridade varchar (100),
pedexame varchar (100),
resulexame varchar (100),
medicamento varchar (100),
alergia varchar (100),
medico varchar (100),
foreign key (id_ref) references tbpacient (id) on delete no action
);



