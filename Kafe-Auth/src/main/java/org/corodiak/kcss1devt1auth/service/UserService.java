package org.corodiak.kcss1devt1auth.service;

import org.corodiak.kcss1devt1auth.auth.jwt.AuthToken;
import org.corodiak.kcss1devt1auth.exception.EmailDuplicateException;
import org.corodiak.kcss1devt1auth.type.dto.SignInDto;
import org.corodiak.kcss1devt1auth.type.dto.SignUpDto;
import org.corodiak.kcss1devt1auth.type.dto.UserInfoUpdateDto;
import org.corodiak.kcss1devt1auth.type.dto.UserPasswordUpdateDto;
import org.corodiak.kcss1devt1auth.type.vo.UserVo;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {
	AuthToken signup(SignUpDto signUpDto) throws EmailDuplicateException;

	void resign(Long userSeq);

	void userInfoUpdate(Long userSeq, UserInfoUpdateDto userInfoUpdateDto) throws UsernameNotFoundException;

	void userPasswordUpdate(Long userSeq, UserPasswordUpdateDto userPasswordUpdateDto) throws UsernameNotFoundException;

	AuthToken signIn(SignInDto signInDto) throws UsernameNotFoundException;

	UserVo getUserInfo(Long userSeq) throws UsernameNotFoundException;
}
