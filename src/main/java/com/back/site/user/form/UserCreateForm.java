package com.back.site.user.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserCreateForm (
    @Size(min = 3, max = 25)
    @NotEmpty(message = "사용자ID는 필수항목입니다.")
    String username,

    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    String password1,

    @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
    String password2,

    @NotEmpty(message = "이메일은 필수항목입니다.")
    @Email
    String email
){
    public boolean isPasswordMatching(){
        return password1 != null && password1.equals(password2);
    }
}
