use cluster_accountant;
#drop table principal_investigator;
create table principal_investigator (uid integer primary key,userid char(50),first_name char(50),last_name char(50),alias char(100) unique key,email_address char(100),exempt boolean) ENGINE=InnoDB;
insert into principal_investigator values ('38757','kodosaru','Don','Johnson','Physics','kodosaru@ks.edu',FALSE);
select * from principal_investigator;
