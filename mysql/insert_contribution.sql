use cluster_accountant;
#drop table contribution;
create table contribution (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, date_contributed date,contributor_userid char(50),credited_to_userid char(50),host_name char(50), processor_type char(100),no_of_processors int,split_percentage int,su_charge float) ENGINE=InnoDB;
insert into contribution values(NULL,'2011-12-01','kodosaru','kodosaru','aether.ks.edu','2.93 GHz Intel Xeon X5570 Blade = 96GB Memory',12,100,1.9);
select * from contribution;
