package com.techacademy.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import org.springframework.ui.ModelMap;

import com.techacademy.entity.User;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class UserControllerTest {
    private MockMvc mockMvc;

    private final WebApplicationContext webApplicationContext;

    UserControllerTest(WebApplicationContext context) {
        this.webApplicationContext = context;
    }

    @BeforeEach
    void beforeEach() {
        // Spring Securityを有効にする
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity()).build();
    }

    @Test
    @DisplayName("User更新画面")
    @WithMockUser
    void testGetUser() throws Exception {
        // HTTPリクエストに対するレスポンスの検証
        MvcResult result = mockMvc.perform(get("/user/update/1/")) // URLにアクセス
            .andExpect(status().isOk()) // ステータスを確認
            .andExpect(model().attributeExists("user")) // Modelの内容を確認
            .andExpect(model().hasNoErrors()) // Modelのエラー有無の確認
            .andExpect(view().name("user/update")) // viewの確認
            .andReturn(); // 内容の取得

        // userの検証
        // Modelからuserを取り出す
        User user = (User)result.getModelAndView().getModel().get("user");
        assertEquals(1, user.getId());
        assertEquals("キラメキ太郎", user.getName());
    }

    @Test
    @DisplayName("User一覧画面")
    @WithMockUser
    public void testGetList() throws Exception {
        // GETリクエストを送信し、結果を取得
        MvcResult result = mockMvc.perform(get("/user/list"))
            .andExpect(status().isOk()) // HTTPステータスが200OK
            .andExpect(model().attributeExists("userlist")) // Modelにuserlistが含まれている
            .andExpect(model().hasNoErrors()) // Modelにエラーが無い
            .andExpect(view().name("user/list")) // viewの名前が user/list
            .andReturn();

        // Modelからuserlistを取り出す
        ModelMap modelMap = result.getModelAndView().getModelMap();
        List<User> userlist = (List<User>) modelMap.get("userlist");

        // 件数が3件であること
        assert userlist.size() == 3;

        // userlistから1件ずつ取り出し、idとnameを検証
        assert userlist.get(0).getId() == 1;
        assert userlist.get(0).getName().equals("キラメキ太郎");

        assert userlist.get(1).getId() == 2;
        assert userlist.get(1).getName().equals("キラメキ次郎");

        assert userlist.get(2).getId() == 3;
        assert userlist.get(2).getName().equals("キラメキ花子");

    }

}