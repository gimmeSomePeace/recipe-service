package com.gimmesomepeace.recipes.exception;

public class ResourceNotFoundException extends RuntimeException {
    private final ResourceType resourceType;
    private final Long resourceId;

    public ResourceNotFoundException(ResourceType resourceType, Long resourceId) {
        super(resourceType + " not found with id " + resourceId);
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public Long getResourceId() {
        return resourceId;
    }
}

