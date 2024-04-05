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
public class MultiTenantConnection {

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


    public DataSource tenantDataSource() throws IOException {
        Properties prop = this.tenantProperties();
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(prop.getProperty("db.url"));
        dataSource.setUsername(prop.getProperty("db.username"));
        dataSource.setPassword(prop.getProperty("db.password"));

        return dataSource;
    }

    public void createDatabase(String dbName){
        try {
            Connection c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/", tenantProperties().getProperty("db.username"), tenantProperties().getProperty("db.password"));
            Statement statement = c.createStatement();
            String sql = String.format("CREATE DATABASE %s;", dbName);
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException | IOException e) {
            log.info(e.toString());
        }
    }

    public void initMasterDb() throws IOException, SQLException {
        PreparedStatement psTabble = tenantDataSource().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS public.tenants (name varchar NULL);");
        psTabble.executeUpdate();

        PreparedStatement psInsert = tenantDataSource().getConnection().prepareStatement("INSERT INTO public.tenants (name) VALUES('default');");
        psInsert.executeUpdate();
    }

    public List<Tenant> listTenant() throws SQLException, IOException {
        List<Tenant> tenantList = new ArrayList<>();
        PreparedStatement pstmt = MultiTenantConnection.this.tenantDataSource().getConnection().prepareStatement("select * from tenants");
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
