#!/bin/bash

mysql -u root < insert_accounting.sql
mysql -u root < insert_contribution.sql
mysql -u root < insert_fileposition.sql
mysql -u root < insert_pi.sql
mysql -u root < insert_proctype.sql
mysql -u root < insert_project.sql
