package com.salogics.pdfanalyser.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class TravellingTools {

    @Tool(description = "Get the weather of a city")
    public String getWeather(String city){
        return switch (city){
            case "Delhi" -> "Sunny, 26 Degrees";
            case "Bangalore" -> "Cloudy, Weather";
            default -> "Hottt!!";
        };
    }
}
