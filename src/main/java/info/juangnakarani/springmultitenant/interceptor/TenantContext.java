package info.juangnakarani.springmultitenant.interceptor;

import info.juangnakarani.springmultitenant.pojo.Tenant;

public class TenantContext {
    private static final ThreadLocal<String> currentTenant = new InheritableThreadLocal<>();

    public static String getCurrentTenant() {
        return currentTenant.get();
    }

    public static void setCurrentTenant(Tenant tenant) {
        currentTenant.set(tenant.getName());
    }

    public static void clear() {
        currentTenant.remove();
    }
}
