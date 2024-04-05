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

To access customer tenant request must have header ```x-tenant-name``` e.g: ```x-tenant-name=test_db_tenant_new```.
### 2. Create customer
mehtod POST
```bash
http://localhost:8080/database/create?dbName=test_db_tenant_new
```
### 3. Update customer
mehtod PUT
```bash
http://localhost:8080/database/create
```
### 4. GET customer
mehtod GET
```bash
http://localhost:8080/database/get/{id}
```
### 5. List customer
mehtod GET
```bash
http://localhost:8080/database/list
```

 

