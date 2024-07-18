# Use an official MySQL 8.0 image as a base
FROM mysql:8.0

# Set the environment variables
ENV MYSQL_ROOT_PASSWORD=123
ENV MYSQL_USER=product_user
ENV MYSQL_PASSWORD=product_user_password
ENV MYSQL_DATABASE=DentistryManagementSystem

# Copy the initDB.sql file to the container
COPY initDB.sql /docker-entrypoint-initdb.d/initDB.sql

# Expose the MySQL port
EXPOSE 3306

# Run the command to start MySQL when the container starts
CMD ["mysqld"]