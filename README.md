# Spring boot multi tenant database

## Architecture
### Separated database
In this approach, each tenant’s data is kept in a separate database instance and is isolated from other tenants.
![img per database](https://www.baeldung.com/wp-content/uploads/2022/08/database_per_tenant.png)

## End-point
### 1. Create database
```bash
http://localhost:8080/database/create?dbName=test_db_tenant_new
```

## CRUD customer
To access tenant request must have header ```x-tenant-name```.
### 1. Create customer
mehtod POST
```bash
http://localhost:8080/database/create?dbName=test_db_tenant_new
```
### 2. Update customer
mehtod PUT
```bash
http://localhost:8080/database/create
```
### 3. GET customer
mehtod GET
```bash
http://localhost:8080/database/get/{id}
```
### 4. List customer
mehtod GET
```bash
http://localhost:8080/database/list
```

 

