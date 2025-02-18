package com.example.board.post;

import com.example.board.member.JPAMemberService;
import com.example.board.member.MemberDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JPAPostService service;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private JPAMemberService memberService;

    @BeforeEach
    void createDto(){
        MemberDto memberDto = new MemberDto(null, "Sangsun", 26, "Game");
        memberService.createMember(memberDto);
    }

    @Test
    @DisplayName("post 생성 test")
    public void createPostTest() throws Exception {
        //given
        PostDto postDto = new PostDto(null, "test", "testcontent", "Sangsun");
        //when //then
        mockMvc.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postDto))
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-save",
                        requestFields(
                                fieldWithPath("id").type(JsonFieldType.NULL).description("id"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("title"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("content"),
                                fieldWithPath("memberName").type(JsonFieldType.STRING).description("memberName")
                        ),
                        requestFields(
                                fieldWithPath("id").type(JsonFieldType.NULL).description("id"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("title"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("content"),
                                fieldWithPath("memberName").type(JsonFieldType.STRING).description("memberName")
                        )));
    }

    @Test
    @DisplayName("postList 확인 test")
    public void getPostListTest() throws Exception {
        //given
        PostDto postDto1 = new PostDto(null, "test", "testcontent", "Sangsun");
        PostDto postDto2 = new PostDto(null, "test", "test", "Sangsun");
        PostDto postDto3 = new PostDto(null, "test", "content", "Sangsun");
        service.createPost(postDto1);
        service.createPost(postDto2);
        service.createPost(postDto3);
        //when //then
        mockMvc.perform(get("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("postList"));
    }

    @Test
    @DisplayName("post 확인 by id test")
    public void getPostByIdTest() throws Exception {
        //given
        PostDto postDto1 = new PostDto(null, "test", "testcontent", "Sangsun");
        PostDto postDto2 = new PostDto(null, "test", "test", "Sangsun");
        PostDto postDto3 = new PostDto(null, "test", "content", "Sangsun");
        service.createPost(postDto1);
        service.createPost(postDto2);
        service.createPost(postDto3);
        //when //then
        mockMvc.perform(get("/posts/2")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("postById",
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("id"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("title"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("content"),
                                fieldWithPath("memberName").type(JsonFieldType.STRING).description("memberName")
                        )));
    }


    @Test
    @DisplayName("post update test")
    public void getUpdatePostTest() throws Exception {
        //given
        PostDto postDto1 = new PostDto(null, "test", "testcontent", "Sangsun");
        PostDto postDto2 = new PostDto(null, "test", "test", "Sangsun");
        PostDto postDto3 = new PostDto(null, "test", "content", "Sangsun");
        service.createPost(postDto1);
        service.createPost(postDto2);
        service.createPost(postDto3);
        PostDto updatePost = new PostDto(2L, "test", "updated", "Sangsun");
        //when //then
        mockMvc.perform(post("/posts/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePost))
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("postUpdate",
                        requestFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("id"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("title"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("content"),
                                fieldWithPath("memberName").type(JsonFieldType.STRING).description("memberName")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("id"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("title"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("content"),
                                fieldWithPath("memberName").type(JsonFieldType.STRING).description("memberName")
                        )));
    }
}