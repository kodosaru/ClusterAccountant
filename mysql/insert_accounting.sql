use cluster_accountant;
#drop table accounting;
create table accounting (primary key id(job_number,task_number,pe_taskid), job_number char(50),hostname char(50),_group char(50),index using btree (_group),owner char(50),index using btree (owner), start_time bigint, index using btree (start_time),end_time bigint, index using btree (end_time),cpu float,task_number char(5),pe_taskid char(50)) ENGINE=InnoDB;
