package com.yuyuko.mall.session.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@ApiModel(description = "用户会话信息")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserSessionInfo {
    public UserSessionInfo(Long id) {
        this.id = id;
    }

    @ApiModelProperty(value = "用户id",position = 1)
    private Long id;

    @ApiModelProperty(value = "用户登录账号",position = 2)
    private String username;

    @ApiModelProperty(value = "用户昵称",position = 3)
    private String nickname;

    @ApiModelProperty(value = "用户头像",position = 4)
    private String avatar;

    @ApiModelProperty(value = "卖家标志",position = 5)
    private Boolean isSeller;
}
