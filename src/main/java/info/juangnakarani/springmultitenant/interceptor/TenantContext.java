package info.juangnakarani.springmultitenant.interceptor;

import info.juangnakarani.springmultitenant.pojo.Tenant;

public class TenantContext {
    private static ThreadLocal<Tenant> currentTenant = new InheritableThreadLocal<>();

    public static Tenant getCurrentTenant() {
        return currentTenant.get();
    }

    public static void setCurrentTenant(Tenant tenant) {
        currentTenant.set(tenant);
    }

    public static void clear() {
        currentTenant.remove();
    }
}
