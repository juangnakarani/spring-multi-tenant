package info.juangnakarani.springmultitenant.config;

import info.juangnakarani.springmultitenant.pojo.Tenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Configuration
public class MultiTenantConfig {

//    @Value("${defaultTenant}")
//    private String defaultTenant;


    @Autowired
    public MultiTenantConnection multiTenantConnection;

    @Bean
    public DataSource dataSource() throws IOException, SQLException {
        multiTenantConnection.createDatabase("tenant_master");
        multiTenantConnection.initMasterDb();

        Properties prop = multiTenantConnection.tenantProperties();
        Map<Object, Object> resolvedDataSources = new HashMap<>();
        try {
            List<Tenant> tenantList = multiTenantConnection.listTenant();
            for(Tenant tenant : tenantList) {
                DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
                dataSourceBuilder.url("jdbc:postgresql://localhost:5432/"+ tenant.getName());
                dataSourceBuilder.driverClassName("org.postgresql.Driver");
                dataSourceBuilder.username(prop.getProperty("db.username"));
                dataSourceBuilder.password(prop.getProperty("db.password"));
                resolvedDataSources.put(tenant.getName(), dataSourceBuilder.build());
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

        String firstKey = (String) resolvedDataSources.keySet().iterator().next();


        AbstractRoutingDataSource dataSource = new MultiTenantDataSource();
        dataSource.setDefaultTargetDataSource(resolvedDataSources.get(firstKey));
        dataSource.setTargetDataSources(resolvedDataSources);

        dataSource.afterPropertiesSet();
        return dataSource;
    }

}