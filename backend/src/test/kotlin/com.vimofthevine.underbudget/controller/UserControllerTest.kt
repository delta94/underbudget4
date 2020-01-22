package com.vimofthevine.underbudget.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.*
import com.vimofthevine.underbudget.dto.UserIdResponse
import com.vimofthevine.underbudget.dto.UserProfileResponse
import com.vimofthevine.underbudget.dto.UserRegistrationRequest
import com.vimofthevine.underbudget.security.UserPrincipal
import com.vimofthevine.underbudget.service.UserService

import java.time.Instant
import java.util.UUID

import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.web.server.ResponseStatusException

@WebMvcTest(UserController::class)
class UserControllerTest : AbstractControllerTest() {
  @MockBean 
  lateinit var users: UserService

  @Autowired
  lateinit var mvc: MockMvc

  @Test
  fun `create user should reject when missing name`() {
    mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
      .content(ObjectMapper().writeValueAsString(mapOf(
        "email" to "bob@test.com",
        "password" to "password123456"
      ))))
      .andExpect(status().isBadRequest)
      .andExpect(jsonPath("$.message", equalTo("Validation Failed")))
  }

  @Test
  fun `create user should reject when missing email`() {
    mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
      .content(ObjectMapper().writeValueAsString(mapOf(
        "name" to "robert",
        "password" to "password123456"
      ))))
      .andExpect(status().isBadRequest)
      .andExpect(jsonPath("$.message", equalTo("Validation Failed")))
  }

  @Test
  fun `create user should reject when missing password`() {
    mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
      .content(ObjectMapper().writeValueAsString(mapOf(
        "name" to "robert",
        "email" to "bob@test.com"
      ))))
      .andExpect(status().isBadRequest)
      .andExpect(jsonPath("$.message", equalTo("Validation Failed")))
  }

  @Test
  fun `create user should reject when name is too short`() {
    mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
      .content(ObjectMapper().writeValueAsString(mapOf(
        "name" to "bob",
        "email" to "bob@test.com",
        "password" to "password123456"
      ))))
      .andExpect(status().isBadRequest)
      .andExpect(jsonPath("$.message", equalTo("Validation Failed")))
  }

  @Test
  fun `create user should reject when name is too long`() {
    mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
      .content(ObjectMapper().writeValueAsString(mapOf(
        "name" to "bobbybobbybobbybobbybobbybobbybobbybobbybobbybobbybobbybobbybobbybobbybobbybobbybobbybobbybobbybobbybobbybobbybobbybobbybobbybobbybobby",
        "email" to "bob@test.com",
        "password" to "password123456"
      ))))
      .andExpect(status().isBadRequest)
      .andExpect(jsonPath("$.message", equalTo("Validation Failed")))
  }

  @Test
  fun `create user should reject when name is invalid`() {
    mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
      .content(ObjectMapper().writeValueAsString(mapOf(
        "name" to "bobby!",
        "email" to "bob@test.com",
        "password" to "password123456"
      ))))
      .andExpect(status().isBadRequest)
      .andExpect(jsonPath("$.message", equalTo("Validation Failed")))
  }

  @Test
  fun `create user should reject when email is invalid`() {
    mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
      .content(ObjectMapper().writeValueAsString(mapOf(
        "name" to "robert",
        "email" to "notanemail",
        "password" to "password123456"
      ))))
      .andExpect(status().isBadRequest)
      .andExpect(jsonPath("$.message", equalTo("Validation Failed")))
  }

  @Test
  fun `create user should reject when password is too short`() {
    mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
      .content(ObjectMapper().writeValueAsString(mapOf(
        "name" to "robert",
        "email" to "bob@test.com",
        "password" to "password"
      ))))
      .andExpect(status().isBadRequest)
      .andExpect(jsonPath("$.message", equalTo("Validation Failed")))
  }

  @Test
  fun `create user should succeed with valid registration`() {
    whenever(users.registerUser(any<UserRegistrationRequest>()))
      .thenReturn(UserIdResponse(userId = UUID.fromString("aaaabbbb-aaaa-bbbb-cccc-aaaabbbbcccc")))
    mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
      .content(ObjectMapper().writeValueAsString(mapOf(
        "name" to "robert",
        "email" to "bob@test.com",
        "password" to "password123456"
      ))))
      .andExpect(status().isCreated)
      .andExpect(jsonPath("$.userId", equalTo("aaaabbbb-aaaa-bbbb-cccc-aaaabbbbcccc")))
  }

  @Test
  fun `get current user should reject when unauthenticated`() {
    mvc.perform(get("/api/users/me")).andExpect(status().isUnauthorized)
  }

  @Test
  fun `get current user should return user profile`() {
    whenever(users.getUserProfile(UUID.fromString("aaaabbbb-aaaa-bbbb-cccc-aaaabbbbcccc")))
      .thenReturn(UserProfileResponse(
        id = UUID.fromString("aaaabbbb-aaaa-bbbb-cccc-aaaabbbbcccc"),
        name = "myusername",
        email = "my@email.com",
        created = Instant.parse("2020-01-21T00:00:00.00Z"),
        lastUpdated = Instant.parse("2020-01-22T00:00:00.00Z")
      ))
    mvc.perform(get("/api/users/me").with(user(UserPrincipal(
      id = UUID.fromString("aaaabbbb-aaaa-bbbb-cccc-aaaabbbbcccc"),
      name = "theusername",
      hashedPassword = "hashed-password"
    ))))
      .andExpect(status().isOk)
      .andExpect(jsonPath("$.id", equalTo("aaaabbbb-aaaa-bbbb-cccc-aaaabbbbcccc")))
      .andExpect(jsonPath("$.name", equalTo("myusername")))
      .andExpect(jsonPath("$.email", equalTo("my@email.com")))
      .andExpect(jsonPath("$.created", containsString("2020-01-21")))
      .andExpect(jsonPath("$.lastUpdated", containsString("2020-01-22")))
  }
}
