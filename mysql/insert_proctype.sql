use cluster_accountant;
#drop table processor_type;
create table processor_type (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,date_installed date, regex char(100),processor_type char(100),processor_qty integer,node_qty integer,su_charge float,cluster char(50),disabled boolean) ENGINE=InnoDB;
insert into processor_type values(NULL,'2012-10-05','^ns[3-6].ks.edu$','2.7 GHz Intel Xeon E5-2680 nodes (64GB Memory)',16,4,2.7,'scluster',FALSE);
insert into processor_type values(NULL,'2013-01-07','^aether.ks.edu$','2.93 GHz Intel Xeon X5670 nodes (96GB Memory)',16,1,1.9,'scluster',FALSE);
select * from processor_type;
