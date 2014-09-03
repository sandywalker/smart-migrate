Smart-Migrate
=============

Java based data migration tool,Cross-platform,Cross-database

The app is tested with mysql and oracle, sqlserver,db2 or other database are also support theoretically ,but i dont have enough time
to test it in all platforms/databases,so if you use this tool,make sure to test with theses databases and report bug to me,
thanks!

Features
=============

* Java based,Cross-platform
* Cross-database
* Support multi tables with relations
* Support dict mapping
* Auto field mapping
* Support data rollback
* Support auto i18n (now supported:EN,zh-CN)


How to Use
=============

This project is coded by NetBeans IDE 8.0 under jdk 1.6.0,use maven3.0 to manage the project's build.

You can run/debug the code by NetBeans IDE,or you can build an executable jar by **maven** with **maven-assembly-plugin**

If you want to add i18n support for your own languages,just create bundle file in **org/smart/migrate/ui** directory
