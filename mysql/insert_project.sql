use cluster_accountant;
#drop table project;
create table project (gid integer primary key,groupid char(50),pi_owner_userid char(50)) ENGINE=InnoDB;
insert into project values ('569325','gensci','donj');
select * from project;
