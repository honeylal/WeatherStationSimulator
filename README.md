# About the application

This application which artificially simulates one or more different weather stations and outputs weather data in a standard format. We need to provide the historical weather information against each weather station to simulate the weather. The application is capable of holding multiple weather stations at a time (only the JVM memory is the limit ;) ).

# Just about the data model 
This application having a list of WeatherStation each WeatherStation having list of Seasons (store different weather seasons that come under that weather station eg Winter, Summer, Monsoon etc.. we can configure up to 12 seasons against one weather station), ExtremeWeatherSeason (store different extreme season that come under that weather station example Floods, Cyclone etc.... Note: If you need more fancy, add more details in to it.... ). ExtremeWeatherSeason is a sub class of Seasons (so it has all the public features of Seasons and some extra .....). For every Season we can configure n number of SeasonDayâ€™s (represents one day weather details) more number of SeasonDayâ€™s help the application to generate more random weather readings. SeasonDay having a list of WeatherData with respected different time in a single day. WeatherData class that actually holding the weather reading information like temperature, pressure, dewPoint, weatherCondition etc....

# How it works.

>We need to select one WeatherStation and need to provide a date range to print the simulated weather details.

- First the application create a list of days from the date range, for each day we need to find one season (this we get from the Weather station season mapping, but we need to provide the Calendar.MONTH for current date).
#### Wow... lets add some surprise factor, What it is?

- Some time we may have some extreme weather conditions like Floods, Cyclone etc... we can add that factor also here. We already have extreme weather details from weather data files.
- Before selecting the season for a day we are doing a dice
* > Dice logic : we get the current system time in Millisecond if it is completely divisible by 15 the current day season go to any of the extreme condition we added against the station.

* >Note: The extreme season name also get by using a dice (system time in Millisecond % total no of extreme weather season configured) 

- Now we have one season, the next stage we randomly select 5 days (now it configured as 5) from the current season (here we are dice logic  (Math.random() X 1000) % total configured days).

#### Now we have 5 random days in one season to predict our weather. How its working  from here?

- Here is the logic (Consider this case : We have 5 temperature entries for 01:00 PM with that how we are predicting).

-  First we find the mean of these 5 values.
-  Next we find the standard deviation.
-  So this is saying the most frequent predictive values range from (mean - standard deviation)  to (mean + standard deviation)

* >Here also we are adding little surprise factor via doing some dice.
Do we need to add or subtract the mean is also taken randomly.
The mean is also divided by (1,2, 3 or 4) this value also taken randomly. 


# So, now are you thinking, how to run this application locally in your machine? Here we go...
Software prerequisites
- Java 1.7 or higher 
- Apache Maven 3.3.9
- Git 2.9.2

# Done with the software? Here are the remaining steps...

### Get the code from Git
URL : https://github.com/honeylal/WeatherStationSimulator.git
>Download the application from the Git to one folder in your machine (later on we referring this folder as LocalAppHome) 

### Building for source
- Open the command prompt
- Go to LocalAppHome 
- Run this command
```sh 
mvn install
```
> Note : We need to have Internet connection during this step (Maven need to download the dependency file...). If you see below message we are done this step 

```sh 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] -----------------------------------------------------------------------
```

### Running the application

>Now you can see that one new folder that is created in your LocalAppHome with name â€œtargetâ€�.

- Go to LocalAppHome\target and copy the  WeatherStationSimulator-1.0-SNAPSHOT.jar in to another folder (later on we referring this folder as LocalAppJarHome).
- Copy the weatherData folder form LocalAppHome to LocalAppJarHome (we all set).
- Go to command prompt
- Go to LocalAppJarHome and run this command: 
```sh 
java -jar WeatherStationSimulator-1.0-SNAPSHOT.jar
```

# Details on the weather data files.
>Note : For each station we need to create one text file. 
       If system see # in the first of line that line is ignored while processing the data file.

### The syntax 
```sh
#WEATHER_STATION_DETAILS:Station name|Latitude|Longitude|Elevation
WEATHER_STATION_DETAILS:Cochin International Airp|10.1520|76.4019|26
#WEATHER_SEASONS:SeasonName@Month,Month|SeasonName@Month,Month
# WINTER@1,2,12 >> Season winter for Jan Feb and Dec 
WEATHER_SEASONS:WINTER@1,2,12|SUMMER@3,4,5|SWMONSOON@6,7,8|NEMONSOON@9,10,11
#EXTREME_SEASONS_WEATHER:ExtreameSeasonName@MMonth|ExtreameSeasonName@Month
EXTREME_SEASONS_WEATHER:EX1@3
#SEASON_NAME:WINTER >> means the conning seasond ays details go to this season
SEASON_NAME:WINTER
# SEASON_DAY:1 >> below details are going to the first day of this season
SEASON_DAY:1
#You can get the same from https://www.wunderground.com/history/ against day (select Comma Delimited File)
# Sample url : https://www.wunderground.com/history/airport/VOCI/2016/8/12/DailyHistory.html?req_city=Cochin&req_state=&req_statename=India&reqdb.zip=00000&reqdb.magic=1&reqdb.wmo=43353&format=1  
#TimeIST,TemperatureC,Dew PointC,Humidity,Sea Level PressurehPa,VisibilityKm,Wind Direction,Wind SpeedKm/h,Gust SpeedKm/h,Precipitationmm,Events,Conditions,WindDirDegrees,DateUTC
12:00 AM,27.0,22.0,74,1009,4.0,Calm,Calm,-,N/A,,Haze,0,2014-12-31 18:30:00
```

### Enjoy...
