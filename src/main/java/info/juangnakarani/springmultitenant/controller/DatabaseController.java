package info.juangnakarani.springmultitenant.controller;

import info.juangnakarani.springmultitenant.config.TenantConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.io.IOException;

@RestController
@RequestMapping("/database")
public class DatabaseController {

    @Autowired
    TenantConnection tenantConnection;

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestParam String dbName){
        tenantConnection.createDatabase(dbName);
        tenantConnection.createCustomerTable(dbName);

        DataSource tenantMasterDataSource = tenantConnection.tenantDataSource(dbName);
        tenantConnection.insertIntoMasterTenant(tenantMasterDataSource, dbName);
        return new ResponseEntity<>("Database created", HttpStatus.OK);
    }
}
