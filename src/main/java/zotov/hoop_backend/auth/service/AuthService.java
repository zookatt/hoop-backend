package zotov.hoop_backend.auth.service;

import zotov.hoop_backend.auth.dto.ChangePasswordDTORequest;

public interface AuthService {

    void changePassword(ChangePasswordDTORequest request);
}
