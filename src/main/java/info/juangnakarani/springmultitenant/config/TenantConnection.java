package info.juangnakarani.springmultitenant.config;

import info.juangnakarani.springmultitenant.interceptor.RequestInterceptor;
import info.juangnakarani.springmultitenant.pojo.Tenant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public class TenantConnection {

    public static final String TENANT_MASTER = "tenant_master";

    private static Logger log = LoggerFactory.getLogger(RequestInterceptor.class);
    @Autowired
    private ResourceLoader resourceLoader;

    public Properties tenantProperties() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:tenant.properties");
        File file = resource.getFile();
        Properties tenantProperties = new Properties();
        tenantProperties.load(new FileInputStream(file));
        return tenantProperties;
    }


    public DataSource tenantDataSource(String dbName) {
        Properties prop = null;
        try {
            prop = this.tenantProperties();
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            String dbUrl = String.format("%s%s",prop.getProperty("db.baseurl"), dbName);
            dataSource.setUrl(dbUrl);
            dataSource.setDriverClassName("org.postgresql.Driver");
            dataSource.setUsername(prop.getProperty("db.username"));
            dataSource.setPassword(prop.getProperty("db.password"));

            return dataSource;
        } catch (IOException e) {
            log.info(e.toString());
        }
        return null;
    }

    public void createDatabase(String dbName){
        try {
            Connection c = DriverManager.getConnection(tenantProperties().getProperty("db.baseurl"), tenantProperties().getProperty("db.username"), tenantProperties().getProperty("db.password"));
            Statement statement = c.createStatement();
            String sql = String.format("CREATE DATABASE %s;", dbName);
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException | IOException e) {
            log.info(e.toString());
        }
    }

    public void initMasterDb(DataSource dataSource) {
        try {
            String sqlCreate = "CREATE TABLE IF NOT EXISTS public.tenants (name varchar NULL, CONSTRAINT tenants_un UNIQUE (name));";
            PreparedStatement psTable = dataSource.getConnection().prepareStatement(sqlCreate);
            psTable.executeUpdate();

            insertIntoMasterTenant("tenant_default");
        } catch (SQLException e) {
            log.info(e.toString());
        }
    }

    public void insertIntoMasterTenant(String name) {
        try {
            String sqlInsert = String.format("INSERT INTO public.tenants (name) VALUES('%s');", name);
            PreparedStatement psInsert = tenantDataSource(TENANT_MASTER).getConnection().prepareStatement(sqlInsert);
            psInsert.executeUpdate();
        } catch (SQLException e) {
            log.info(e.toString());
        }
    }

    public void createCustomerTable(String dbName) {
        try {
            String sqlCreate = "CREATE TABLE public.customers (id int8 NOT NULL, name varchar(255) NULL, CONSTRAINT customers_pkey PRIMARY KEY (id));";
            PreparedStatement psTable = tenantDataSource(dbName).getConnection().prepareStatement(sqlCreate);
            psTable.executeUpdate();

        } catch (SQLException e) {
            log.info(e.toString());
        }
    }

    public List<Tenant> listTenant(String dbName) throws SQLException, IOException {
        List<Tenant> tenantList = new ArrayList<>();
        PreparedStatement pstmt = TenantConnection.this.tenantDataSource(dbName).getConnection().prepareStatement("SELECT * FROM tenants");
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()) {
            String name = rs.getString("name");
            Tenant tenant = new Tenant();
            tenant.setName(name);
            tenantList.add(tenant);
        }
        return tenantList;
    }

}
