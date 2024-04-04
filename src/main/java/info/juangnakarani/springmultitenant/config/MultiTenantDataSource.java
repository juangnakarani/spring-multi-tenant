package info.juangnakarani.springmultitenant.config;

import info.juangnakarani.springmultitenant.interceptor.TenantContext;
import info.juangnakarani.springmultitenant.pojo.Tenant;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;


public class MultiTenantDataSource extends AbstractRoutingDataSource {
    @Override
    protected Tenant determineCurrentLookupKey() {
        return TenantContext.getCurrentTenant();
    }

}
