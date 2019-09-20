package com.yuyuko.mall.admin.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterMessage  {
    @NotNull
    private Long userId;

    @Length(max = 16)
    private String username;
}
