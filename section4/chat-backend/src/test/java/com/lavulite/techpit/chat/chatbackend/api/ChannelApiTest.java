package com.lavulite.techpit.chat.chatbackend.api;

import java.nio.file.Paths;
import javax.sql.DataSource;

import org.dbunit.Assertion;
import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.csv.CsvDataSet;
import org.junit.jupiter.api.Test;
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

  private static final String BASEDIR = "src/test/resources";

  @Test
  public void createTest() throws Exception {
    IDatabaseTester databaseTester = new DataSourceDatabaseTester(dataSource);
    var uri = Paths.get(BASEDIR + "/channel/create/no-record/given").toFile();
    databaseTester.setDataSet(new CsvDataSet(uri));
    databaseTester.onSetup();

    mockMvc.perform(
        MockMvcRequestBuilders.post("/channel")
            .content("""
                {
                  "name": "チャンネル名"
                }
                """)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect((result) -> JSONAssert.assertEquals("""
            {
              "id": 0,
              "name": "チャンネル名"
            }
              """,
            result.getResponse().getContentAsString(),
            false));

    var actualDataSet = databaseTester.getDataSet();
    var expectedUri = Paths.get(BASEDIR + "/channel/create/no-record/expected").toFile();
    var expectedDataSet = new CsvDataSet(expectedUri);
    Assertion.assertEquals(expectedDataSet, actualDataSet);
  }

}
