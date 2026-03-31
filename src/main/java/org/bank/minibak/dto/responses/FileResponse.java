package org.bank.minibak.dto.responses;

import org.springframework.core.io.Resource;

public record FileResponse(Resource resource, String contentType) {
}
