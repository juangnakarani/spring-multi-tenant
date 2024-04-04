package info.juangnakarani.springmultitenant.config;

import info.juangnakarani.springmultitenant.pojo.Tenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public class MultiTenantConnectionProvider {
    @Autowired
    private ResourceLoader resourceLoader;

    @Bean
    public Properties tenantProperties() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:tenant.properties");
        File file = resource.getFile();
        Properties tenantProperties = new Properties();
        tenantProperties.load(new FileInputStream(file));
        return tenantProperties;
    }

    @Bean
    public DataSource multitenantMasterDataSource() throws IOException {
        Properties prop = this.tenantProperties();
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(prop.getProperty("db.url"));
        dataSource.setUsername(prop.getProperty("db.username"));
        dataSource.setPassword(prop.getProperty("db.password"));

        return dataSource;
    }

    @Bean
    public List<Tenant> listTenant() throws SQLException, IOException {
        List<Tenant> tenantList = new ArrayList<>();
        PreparedStatement pstmt = MultiTenantConnectionProvider.this.multitenantMasterDataSource().getConnection().prepareStatement("select * from tenants");
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
