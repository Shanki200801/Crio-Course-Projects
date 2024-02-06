
package com.crio.warmup.stock.quotes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.crio.warmup.stock.dto.AlphavantageCandle;
import com.crio.warmup.stock.dto.AlphavantageDailyResponse;
import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.exception.StockQuoteServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.client.RestTemplate;

public class AlphavantageService implements StockQuotesService {

  RestTemplate restTemplate;

  protected AlphavantageService(RestTemplate restTemplate){
    this.restTemplate = restTemplate;
  }

  // @Override
  // public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to)
  //     throws JsonProcessingException {
  //   String url = buildUri(symbol);
  //   ObjectMapper mapper = getObjectMapper();
  //   RestTemplate restTemplate = new RestTemplate();
  //   String responseAsString = restTemplate.getForObject(url,String.class);
  //   JsonNode dailyDataNode = mapper.readTree(responseAsString).findValue("Time Series (Daily)");
  //   List<Candle> candles = new ArrayList<>();

  //   for(Iterator<String> it = dailyDataNode.fieldNames(); it.hasNext();){
  //     String ddate = it.next();
  //     LocalDate thisDate = LocalDate.parse(ddate);
  //     if((thisDate.equals(from)||thisDate.isAfter(from))&&(thisDate.isBefore(to)||thisDate.equals(to))){
  //       JsonNode dayNode = dailyDataNode.get(ddate);
  //       AlphavantageCandle candle = new AlphavantageCandle();
  //       candle.setOpen(dayNode.get("1. open").asDouble());
  //       candle.setClose(dayNode.get("4. close").asDouble());
  //       candle.setHigh(dayNode.get("2. high").asDouble());
  //       candle.setLow(dayNode.get("3. low").asDouble());
  //       try {
  //         candle.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(thisDate.toString()));
  //       } catch (ParseException e) {
          
  //         e.printStackTrace();
  //       }
  //       candles.add(candle);
  //     }
  //   }

    
  //   return getSortedCandles(candles);
  // }

  @Override
  public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to) throws StockQuoteServiceException{
    String uri = buildUri(symbol);
    ObjectMapper mapper = getObjectMapper();
    String responseString = restTemplate.getForObject(uri, String.class );
    
    System.out.println("Debug string -------------/n"+responseString+"-------------------");
    AlphavantageDailyResponse alphavantageDailyResponse = null;
    try {
      alphavantageDailyResponse = mapper.readValue(responseString, AlphavantageDailyResponse.class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    Map<LocalDate, AlphavantageCandle> filteredMap = getFilteredMap(alphavantageDailyResponse, from, to);
    if(filteredMap.isEmpty())
      throw new StockQuoteServiceException("invalid data return or no data found");
    List<Candle> sortedCandleList = getSortedCandlesList(filteredMap);
    return sortedCandleList; 
    
  }

  // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  //  Implement the StockQuoteService interface as per the contracts. Call Alphavantage service
  //  to fetch daily adjusted data for last 20 years.
  //  Refer to documentation here: https://www.alphavantage.co/documentation/
  //  --
  //  The implementation of this functions will be doing following tasks:
  //    1. Build the appropriate url to communicate with third-party.
  //       The url should consider startDate and endDate if it is supported by the provider.
  //    2. Perform third-party communication with the url prepared in step#1
  //    3. Map the response and convert the same to List<Candle>
  //    4. If the provider does not support startDate and endDate, then the implementation
  //       should also filter the dates based on startDate and endDate. Make sure that
  //       result contains the records for for startDate and endDate after filtering.
  //    5. Return a sorted List<Candle> sorted ascending based on Candle#getDate
  //  IMP: Do remember to write readable and maintainable code, There will be few functions like
  //    Checking if given date falls within provided date range, etc.
  //    Make sure that you write Unit tests for all such functions.
  //  Note:
  //  1. Make sure you use {RestTemplate#getForObject(URI, String)} else the test will fail.
  //  2. Run the tests using command below and make sure it passes:
  //    ./gradlew test --tests AlphavantageServiceTest
  //CHECKSTYLE:OFF
    //CHECKSTYLE:ON
  //  CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  //  1. Write a method to create appropriate url to call Alphavantage service. The method should
  //     be using configurations provided in the {@link @application.properties}.
  //  2. Use this method in #getStockQuote.
  private static String getAlphaVantageToken(){
    return "SZRX2SJDQSJ11BPB";
  }
  protected String buildUri(String symbol){
    return String.format("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=%s&outputsize=full&apikey=%s",symbol, getAlphaVantageToken() );
  }

  private static ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }
  private static List<Candle> getSortedCandlesList(Map<LocalDate, AlphavantageCandle> map){
    
    List<Candle> candleList = new ArrayList<>();
    for(AlphavantageCandle candle: map.values()){
      candleList.add(candle);
    }
    candleList.sort((c1,c2)-> c1.getDate().compareTo(c2.getDate()));
    return candleList;
  }

  private static List<Candle> getSortedCandles(List<Candle> candles){
    candles.sort((s1,s2)-> s1.getDate().compareTo(s2.getDate()));
    return candles;
  }

  private static Map<LocalDate, AlphavantageCandle> getFilteredMap(AlphavantageDailyResponse alphavantageDailyResponse, LocalDate from, LocalDate to) throws StockQuoteServiceException{
    Map<LocalDate, AlphavantageCandle> initialmap = alphavantageDailyResponse.getCandles();
    Map<LocalDate, AlphavantageCandle> filteredMap = new HashMap<>();
    if( initialmap == null){
      throw new StockQuoteServiceException("map is null");
    }
    

    for(LocalDate date: initialmap.keySet()){
      if((date.equals(from)||date.isAfter(from))&&(date.isBefore(to)||date.equals(to))){
        AlphavantageCandle currCandle = initialmap.get(date);
        try {
          currCandle.setDate(new SimpleDateFormat("yyyy-MM-dd").parse( date.toString()));
        } catch (ParseException e) {
          e.printStackTrace();

        }
        filteredMap.put(date, currCandle);

      }
      // if(filteredMap.isEmpty()){
      //   throw new StockQuoteServiceException("Data is not present");
      // }
    }

    return filteredMap;
  }
  // TODO: CRIO_TASK_MODULE_EXCEPTIONS
  //   1. Update the method signature to match the signature change in the interface.
  //   2. Start throwing new StockQuoteServiceException when you get some invalid response from
  //      Alphavantage, or you encounter a runtime exception during Json parsing.
  //   3. Make sure that the exception propagates all the way from PortfolioManager, so that the
  //      external user's of our API are able to explicitly handle this exception upfront.
  //CHECKSTYLE:OFF

}

