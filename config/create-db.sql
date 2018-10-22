#
#  For Production usage: CDO NOT USE alfresco as password!
#
CREATE USER 'alfresco'@'%' IDENTIFIED BY 'alfresco';
CREATE USER 'alfresco'@'localhost' IDENTIFIED BY 'alfresco';
CREATE DATABASE alfresco;
GRANT ALL PRIVILEGES ON alfresco.* TO alfresco@'%';
GRANT ALL PRIVILEGES ON alfresco.* TO alfresco@'localhost';