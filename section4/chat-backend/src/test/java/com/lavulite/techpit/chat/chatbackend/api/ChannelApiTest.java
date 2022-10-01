package com.lavulite.techpit.chat.chatbackend.api;

import java.util.stream.Stream;
import javax.sql.DataSource;
import org.dbunit.Assertion;
import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.csv.CsvURLDataSet;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class ChannelApiTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private DataSource dataSource;

  @ParameterizedTest
  @MethodSource("createTestProvider")
  public void createTest(String requestBody, String expectedBody, String dbPath) throws Exception {
    IDatabaseTester databaseTester = new DataSourceDatabaseTester(dataSource);
    var givenUrl = this.getClass().getResource("/channel/create/" + dbPath + "/given/");
    databaseTester.setDataSet(new CsvURLDataSet(givenUrl));
    databaseTester.onSetup();

    mockMvc.perform(
        MockMvcRequestBuilders.post("/channel")
            .content(requestBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect((result) -> JSONAssert.assertEquals(
            expectedBody,
            result.getResponse().getContentAsString(),
            false));

    var actualDataSet = databaseTester.getDataSet();
    var expectedUri = this.getClass().getResource("/channel/create/" + dbPath + "/expected/");
    var expectedDataSet = new CsvURLDataSet(expectedUri);
    Assertion.assertEquals(expectedDataSet, actualDataSet);
  }

  private static Stream<Arguments> createTestProvider() {
    return Stream.of(
        Arguments.arguments(
            """
                {
                  "name": "はじめてのチャンネル"
                }
                """,
            """
                {
                  "id": 1,
                  "name": "はじめてのチャンネル"
                }
                  """,
            "no-record"),
        Arguments.arguments(
            """
                {
                  "name": "APIで追加するチャンネル"
                }
                """,
            """
                {
                  "id": 3,
                  "name": "APIで追加するチャンネル"
                }
                  """,
            "multi-record"));
  }

  @ParameterizedTest
  @MethodSource("findAllTestProvider")
  public void findAllTest(String expectedBody, String dbPath) throws Exception {
    IDatabaseTester databaseTester = new DataSourceDatabaseTester(dataSource);
    var givenUrl = this.getClass().getResource("/channel/findAll/" + dbPath + "/given/");
    databaseTester.setDataSet(new CsvURLDataSet(givenUrl));
    databaseTester.onSetup();

    mockMvc.perform(
        MockMvcRequestBuilders.get("/channel")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect((result) -> JSONAssert.assertEquals(
            expectedBody,
            result.getResponse().getContentAsString(),
            false));

    var actualDataSet = databaseTester.getDataSet();
    var expectedUri = this.getClass().getResource("/channel/findAll/" + dbPath + "/given/");
    var expectedDataSet = new CsvURLDataSet(expectedUri);
    Assertion.assertEquals(expectedDataSet, actualDataSet);
  }

  private static Stream<Arguments> findAllTestProvider() {
    return Stream.of(
        Arguments.arguments("[]", "no-record"),
        Arguments.arguments("""
              [
                {
                  "id": 1,
                  "name": "はじめてのチャンネル"
                },
                {
                  "id": 2,
                  "name": "2つ目のチャンネル"
                }
              ]
            """, "multi-record")
    );
  }

}
