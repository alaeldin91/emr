package com.alaeldin.user_service.application.service;

import com.alaeldin.user_service.application.dto.LoginRequest;
import com.alaeldin.user_service.application.dto.UserDtoResponse;

public interface LoginService
{
     UserDtoResponse login(LoginRequest loginRequest) throws Exception;

}
