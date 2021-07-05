package com.eden.orchid.api;

import com.google.inject.ImplementedBy;

@ImplementedBy(OrchidSecurityManagerImpl.class)
public class OrchidSecurityManager extends SecurityManager {
}
