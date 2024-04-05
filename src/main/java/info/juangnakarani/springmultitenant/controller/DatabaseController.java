package info.juangnakarani.springmultitenant.controller;

import info.juangnakarani.springmultitenant.config.MultiTenantConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/database")
public class DatabaseController {

    @Autowired
    MultiTenantConnection multiTenantConnection;

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestParam String dbName){
        multiTenantConnection.createDatabase(dbName);
        return new ResponseEntity<>("Database created", HttpStatus.OK);
    }
}
