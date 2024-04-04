package info.juangnakarani.springmultitenant.config;

import info.juangnakarani.springmultitenant.pojo.Tenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class MultiTenantConfig {

//    @Value("${defaultTenant}")
//    private String defaultTenant;


    @Autowired
    public MultitenantMaster multitenantMaster;

    @Bean
//    @ConfigurationProperties(prefix = "tenants")
    public DataSource dataSource() {
        Map<Object, Object> resolvedDataSources = new HashMap<>();
        try {
            List<Tenant> tenantList = multitenantMaster.listTenant();
            for(Tenant tenant : tenantList) {
                DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
                dataSourceBuilder.url("jdbc:postgresql://localhost:5432/"+ tenant.getName());
                dataSourceBuilder.driverClassName("org.postgresql.Driver");
                dataSourceBuilder.username("postgres");
                dataSourceBuilder.password("password");
                resolvedDataSources.put(tenant.getName(), dataSourceBuilder.build());
            }
        } catch (SQLException e) {
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