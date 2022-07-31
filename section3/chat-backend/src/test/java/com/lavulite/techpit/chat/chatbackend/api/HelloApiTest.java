package com.lavulite.techpit.chat.chatbackend.api;

import java.net.URL;
import java.nio.file.Paths;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.dbunit.Assertion;
import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.csv.CsvDataSet;
import org.dbunit.dataset.csv.CsvURLDataSet;
import org.junit.jupiter.api.Test;
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
public class HelloApiTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private DataSource dataSource;

  @ParameterizedTest
  @MethodSource("helloTestProvider")
  public void helloTest(String queryString, String expectedBody, String dbPath) throws Exception {
    IDatabaseTester databaseTester = new DataSourceDatabaseTester(dataSource);
    URL givenUrl = this.getClass().getResource("/hello/hello/" + dbPath + "/given/");
    databaseTester.setDataSet(new CsvURLDataSet(givenUrl));
    databaseTester.onSetup();

    mockMvc.perform(
        MockMvcRequestBuilders.get("/hello" + queryString)
            .accept(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect((result) -> JSONAssert.assertEquals(
            expectedBody,
            result.getResponse().getContentAsString(),
            false));

    var actualDataSet = databaseTester.getDataSet();
    URL expectedUrl = this.getClass().getResource("/hello/hello/" + dbPath + "/expected/");
    var expectedDataSet = new CsvURLDataSet(expectedUrl);
    Assertion.assertEquals(expectedDataSet, actualDataSet);
  }

  private static Stream<Arguments> helloTestProvider() {
    return Stream.of(
        Arguments.arguments(
            "",
            """
                {
                  "message": "Hello, world!"
                }
                """,
            "default"),
        Arguments.arguments(
            "?name=techpit",
            """
                {
                  "message": "Hello, techpit"
                }
                """,
            "techpit"));
  }
}
