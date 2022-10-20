package com.lavulite.techpit.chat.chatbackend.api;

import java.util.stream.Stream;

import javax.sql.DataSource;

import org.dbunit.IDatabaseTester;
import org.dbunit.Assertion;
import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.dataset.SortedTable;
import org.dbunit.dataset.csv.CsvURLDataSet;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.RegularExpressionValueMatcher;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class MessageApiTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private DataSource dataSource;

  @ParameterizedTest
  @MethodSource("postTestProvider")
  public void postTest(String requestBody, String expectedBody, String dbPath) throws Exception {
    IDatabaseTester databaseTester = new DataSourceDatabaseTester(dataSource);
    var givenUrl = this.getClass().getResource("/messages/post/" + dbPath + "/given/");
    databaseTester.setDataSet(new CsvURLDataSet(givenUrl));
    databaseTester.onSetup();

    mockMvc.perform(
        MockMvcRequestBuilders.post("/messages") // postメソッドを利用
            .content(requestBody) // contentでリクエストボディを設定する
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect((result) -> JSONAssert.assertEquals(
            expectedBody,
            result.getResponse().getContentAsString(),
            // 実行するたびに値が変わる項目のテストをするための詳細設定
            new CustomComparator(
                // 余分なJSON項目が含まれていてもOKとする
                JSONCompareMode.LENIENT,
                // idが所定のフォーマットであることを検証する
                new Customization("id", new RegularExpressionValueMatcher<>("\\d{12}-[a-zA-Z0-9\\-]{36}")),
                // timestampは検証対象外とする(どんな値でもOK)
                new Customization("timestamp", (o1, o2) -> true))));

    // 比較時のソートに使用するカラムを指定する。
    String[] sortBy = {"channel_id", "username", "text"};
    // 比較対象外にするカラムを指定する。
    String[] excludeColumns = {"id", "timestamp"}; 

    var actualDataSet = databaseTester.getConnection().createDataSet();
    // 比較対象外のカラムを除外後、ソートする。
    var actualMessagesTable = new SortedTable(
      DefaultColumnFilter.excludedColumnsTable(
        actualDataSet.getTable("messages"), 
        excludeColumns
        ), sortBy);
        
    var expectedUri = this.getClass().getResource("/messages/post/" + dbPath + "/expected/");
    var expectedDataSet = new CsvURLDataSet(expectedUri);
    // 比較対象外のカラムを除外後、ソートする。
    var expectedMessagesTable = new SortedTable(
      DefaultColumnFilter.excludedColumnsTable(
        expectedDataSet.getTable("messages"), 
        excludeColumns
      ), sortBy);
    Assertion.assertEquals(expectedMessagesTable, actualMessagesTable);
  }

  private static Stream<Arguments> postTestProvider() {
    return Stream.of(
      Arguments.arguments(
          """
              {
                "channelId": 1,
                "text": "APIリクエストしたメッセージ"
              }
              """,
          """
              {
                "id": "202210151201-8097c0d2-ddc7-f02a-9dbf-29dfcd646d2b",
                "channelId": 1,
                "text": "APIリクエストしたメッセージ",
                "username": "guest",
                "timestamp": "2022-10-15 12:01:00"
              }
                """,
          "success"));
  }
}
