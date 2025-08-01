package com.alaeldin.user_service.service;

import com.alaeldin.user_service.dto.LoginRequest;
import com.alaeldin.user_service.dto.UserDtoResponse;

public interface LoginService
{
     UserDtoResponse login(LoginRequest loginRequest) throws Exception;

}
