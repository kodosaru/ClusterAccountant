use cluster_accountant;
#drop table fileposition;
create table fileposition (filename char(50) primary key,offset bigint) ENGINE=InnoDB;
select * from fileposition;
