package com.qa.APIdatadriven;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class DataDrivenApiTest {

	@Test(dataProvider = "empdata1")
	void addEmployees(String empname, String empsal, String empage) {

		RestAssured.baseURI = "http://dummy.restapiexample.com/api/v1";

		// Request payload
		RequestSpecification httpRequest = RestAssured.given();
		JSONObject requestParams = new JSONObject();
		requestParams.put("name", empname);
		requestParams.put("salary", empsal);
		requestParams.put("age", empage);

		// Header information
		httpRequest.header("content-type", "application/json");

		httpRequest.body(requestParams.toJSONString());

		Response response = httpRequest.request(Method.POST, "/create");

		// capturing the response

		String responseBody = response.getBody().asString();
		System.out.println("Response :" + responseBody);

		// Validation
		Assert.assertEquals(responseBody.contains(empname), true);
		Assert.assertEquals(responseBody.contains(empsal), true);
		Assert.assertEquals(responseBody.contains(empage), true);

		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, 200);
	}

	@DataProvider(name = "empdata1")
	String[][] getData() throws IOException {
		String path = System.getProperty("user.dir") + "/src/test/java/com/qa/APIdatadriven/Empdata.xlsx";
		int rowCount = Utils.getRowCount(path, "Sheet1");
		int colCount = Utils.getCellCount(path, "Sheet1", 1);
		String empdata[][] = new String[rowCount][colCount];
		
		for (int i = 1; i <= rowCount; i++) {
			for (int j = 0; j < colCount; j++) {
				empdata[i - 1][j] = Utils.getCellData(path, "Sheet1", i, j);
			}
		}
		// String empdata[][]= {{"abcx7878","40000","30"},{"pqzs2344","68899","40"}};
		return (empdata);
	}

}
