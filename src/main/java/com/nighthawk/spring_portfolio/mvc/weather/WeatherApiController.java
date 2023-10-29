package com.nighthawk.spring_portfolio.mvc.weather;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeatherApiController {
    public static void main(String[] args) {
        // Specify the latitude and longitude
        double latitude = 33.01479454987898;
        double longitude = -117.12140255005595;
        // Create the URI with the latitude and longitude
        URI pointUri = URI.create("https://api.weather.gov/points/" + latitude + "," + longitude);
        try {
            // Send the request to get the forecast office URL
            HttpResponse<String> pointResponse = HttpClient.newHttpClient().send(
                    HttpRequest.newBuilder()
                            .uri(pointUri)
                            .build(),
                    HttpResponse.BodyHandlers.ofString());
            if (pointResponse.statusCode() == 200) {
                // Parse the forecast office URL from the response
                String pointResponseText = pointResponse.body();
                Pattern officePattern = Pattern.compile("forecastOffice\":\"(.*?)\"");
                Matcher officeMatcher = officePattern.matcher(pointResponseText);
                if (officeMatcher.find()) {
                    String forecastOffice = officeMatcher.group(1);
                    // Create the URI for the forecast
                    URI forecastUri = URI.create(forecastOffice + "/forecast");
                    // Send the request to get the weather forecast
                    HttpResponse<String> forecastResponse = HttpClient.newHttpClient().send(
                            HttpRequest.newBuilder()
                                    .uri(forecastUri)
                                    .header("User-Agent", "Java HttpClient")
                                    .build(),
                            HttpResponse.BodyHandlers.ofString());
                    if (forecastResponse.statusCode() == 200) {
                        // Print the weather forecast
                        System.out.println(forecastResponse.body());
                    } else {
                        System.out
                                .println("Failed to retrieve forecast. Status code: " + forecastResponse.statusCode());
                    }
                } else {
                    System.out.println("Failed to find forecast office URL.");
                }
            } else {
                System.out.println("Failed to retrieve point data. Status code: " + pointResponse.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}