package dmit2015.model;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;

/**
 * This class contains weather forecast data that is converted from C# to Java taken from the
 * Microsoft Blazor Web App project template in Visual Studio 2022.
 *
 * @version 2024.05.13
 */
public class WeatherForecast {

    @Future(message = "Date must be in the future.")
    private LocalDate date;

    @Min(value = -40,message = "TempC must be between -40 and 40")
    @Max(value = 40,message = "TempC must be between -40 and 40")
    private int temperatureC;

    private String summary;

    public int getTemperatureF() {
        return (int) (32 + temperatureC / 0.5556);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getTemperatureC() {
        return temperatureC;
    }

    public void setTemperatureC(int temperatureC) {
        this.temperatureC = temperatureC;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }


}