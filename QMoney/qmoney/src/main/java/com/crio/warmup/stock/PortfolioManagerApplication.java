
package com.crio.warmup.stock;


import com.crio.warmup.stock.dto.*;
import com.crio.warmup.stock.log.UncaughtExceptionHandler;
import com.crio.warmup.stock.portfolio.PortfolioManager;
import com.crio.warmup.stock.portfolio.PortfolioManagerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
// import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.Collections;
// import java.util.Comparator;
// import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;
// import java.util.stream.Stream;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import java.nio.file.Paths;
// import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
// import java.util.Comparator;
// import java.util.List;
// import java.util.UUID;
// import java.util.logging.Logger;
// import java.util.stream.Collectors;
// import java.util.stream.Stream;
// import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.client.RestTemplate;


public class PortfolioManagerApplication {

  // TODO: CRIO_TASK_MODULE_JSON_PARSING
  //  Task:
  //       - Read the json file provided in the argument[0], The file is available in the classpath.
  //       - Go through all of the trades in the given file,
  //       - Prepare the list of all symbols a portfolio has.
  //       - if "trades.json" has trades like
  //         [{ "symbol": "MSFT"}, { "symbol": "AAPL"}, { "symbol": "GOOGL"}]
  //         Then you should return ["MSFT", "AAPL", "GOOGL"]
  //  Hints:
  //    1. Go through two functions provided - #resolveFileFromResources() and #getObjectMapper
  //       Check if they are of any help to you.
  //    2. Return the list of all symbols in the same order as provided in json.

  //  Note:
  //  1. There can be few unused imports, you will need to fix them to make the build pass.
  //  2. You can use "./gradlew build" to check if your code builds successfully.

  public static List<String> mainReadFile(String[] args) throws IOException, URISyntaxException {

    ObjectMapper mapper = getObjectMapper();
    File file = resolveFileFromResources(args[0]);
    List<String> symbolList = new ArrayList<>();
    Trade[] trades = mapper.readValue(file, Trade[].class);
    for(Trade trade: trades){
      symbolList.add(trade.getSymbol());
    }
     return symbolList;
  }

  // Note:
  // 1. You may need to copy relevant code from #mainReadQuotes to parse the Json.
  // 2. Remember to get the latest quotes from Tiingo API.






  // TODO: CRIO_TASK_MODULE_CALCULATIONS
  //  Now that you have the list of PortfolioTrade and their data, calculate annualized returns
  //  for the stocks provided in the Json.
  //  Use the function you just wrote #calculateAnnualizedReturns.
  //  Return the list of AnnualizedReturns sorted by annualizedReturns in descending order.

  // Note:
  // 1. You may need to copy relevant code from #mainReadQuotes to parse the Json.
  // 2. Remember to get the latest quotes from Tiingo API.




  // TODO: CRIO_TASK_MODULE_REST_API
  //  Find out the closing price of each stock on the end_date and return the list
  //  of all symbols in ascending order by its close value on end date.

  // Note:
  // 1. You may have to register on Tiingo to get the api_token.
  // 2. Look at args parameter and the module instructions carefully.
  // 2. You can copy relevant code from #mainReadFile to parse the Json.
  // 3. Use RestTemplate#getForObject in order to call the API,
  //    and deserialize the results in List<Candle>



  private static void printJsonObject(Object object) throws IOException {
    Logger logger = Logger.getLogger(PortfolioManagerApplication.class.getCanonicalName());
    ObjectMapper mapper = new ObjectMapper();
    logger.info(mapper.writeValueAsString(object));
  }

  private static File resolveFileFromResources(String filename) throws URISyntaxException {
    return Paths.get(
        Thread.currentThread().getContextClassLoader().getResource(filename).toURI()).toFile();
  }

  private static ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }


  // TODO: CRIO_TASK_MODULE_JSON_PARSING
  //  Follow the instructions provided in the task documentation and fill up the correct values for
  //  the variables provided. First value is provided for your reference.
  //  A. Put a breakpoint on the first line inside mainReadFile() which says
  //    return Collections.emptyList();
  //  B. Then Debug the test #mainReadFile provided in PortfoliomanagerApplicationTest.java
  //  following the instructions to run the test.
  //  Once you are able to run the test, perform following tasks and record the output as a
  //  String in the function below.
  //  Use this link to see how to evaluate expressions -
  //  https://code.visualstudio.com/docs/editor/debugging#_data-inspection
  //  1. evaluate the value of "args[0]" and set the value
  //     to the variable named valueOfArgument0 (This is implemented for your reference.)
  //  2. In the same window, evaluate the value of expression below and set it
  //  to resultOfResolveFilePathArgs0
  //     expression ==> resolveFileFromResources(args[0])
  //  3. In the same window, evaluate the value of expression below and set it
  //  to toStringOfObjectMapper.
  //  You might see some garbage numbers in the output. Dont worry, its expected.
  //    expression ==> getObjectMapper().toString()
  //  4. Now Go to the debug window and open stack trace. Put the name of the function you see at
  //  second place from top to variable functionNameFromTestFileInStackTrace
  //  5. In the same window, you will see the line number of the function in the stack trace window.
  //  assign the same to lineNumberFromTestFileInStackTrace
  //  Once you are done with above, just run the corresponding test and
  //  make sure its working as expected. use below command to do the same.
  //  ./gradlew test --tests PortfolioManagerApplicationTest.testDebugValues

  public static List<String> debugOutputs() {

     String valueOfArgument0 = "trades.json";
     String resultOfResolveFilePathArgs0 = "/home/crio-user/workspace/shashank-criodo-ME_QMONEY_V2/qmoney/bin/main/trades.json";
     String toStringOfObjectMapper = "com.fasterxml.jackson.databind.ObjectMapper@6150c3ec";
     String functionNameFromTestFileInStackTrace = "PortfolioManagerApplicationTest.mainReadFile";
     String lineNumberFromTestFileInStackTrace = "29";


    return Arrays.asList(new String[]{valueOfArgument0, resultOfResolveFilePathArgs0,
        toStringOfObjectMapper, functionNameFromTestFileInStackTrace,
        lineNumberFromTestFileInStackTrace});
  }


  // Note:
  // Remember to confirm that you are getting same results for annualized returns as in Module 3.
  public static List<String> mainReadQuotes(String[] args) throws IOException, URISyntaxException {
    //args[0] contains PortfolioTrade object json and args[1] contains end date
    ObjectMapper mapper = getObjectMapper();
    RestTemplate restTemplate = new RestTemplate();
    LocalDate endDate = LocalDate.parse(args[1]);
    File inputFile = resolveFileFromResources(args[0]);
   
    HashMap<String,Double> symbolToClosePrice = new HashMap<>();

    //Parse data in  file into array
    PortfolioTrade[] trades = mapper.readValue(inputFile, PortfolioTrade[].class);

    // Check if dates are valid
    // Check if Stock names are correct
    for(PortfolioTrade trade:trades){
      isDateValid(trade, endDate);
      isStockValid(trade);
      // Make API calls and get close price on end date
      String url = prepareUrl(trade, endDate, getToken());
      TiingoCandle[] responseCandles = restTemplate.getForObject(url, TiingoCandle[].class);
      symbolToClosePrice.put(trade.getSymbol(),responseCandles[0].getClose());
    }
    // Sort collection
    // Sort the entries by value and collect the keys into a list
      List<String> sortedKeys = symbolToClosePrice.entrySet().stream()
      .sorted(Map.Entry.<String, Double>comparingByValue())
      .map(Map.Entry::getKey)
      .collect(Collectors.toList());


    // return only names
    return sortedKeys;
  }

  private static void isDateValid(PortfolioTrade trade, LocalDate endDate){
    if(trade.getPurchaseDate().isAfter(endDate)){
      throw new RuntimeException("Invalid purchase date");
    }
  }
  private static void isStockValid(PortfolioTrade trade){
    RestTemplate rt = new RestTemplate();
    String url = String.format("https://api.tiingo.com/tiingo/daily/%s?token=%s",trade.getSymbol(), getToken());
    ResponseEntity<String> res = rt.getForEntity(url, String.class);

    if(!res.getStatusCode().equals(HttpStatus.OK)){
      throw new RuntimeException("Invalid stock as symbol");
    }
  }

  // TODO:
  //  After refactor, make sure that the tests pass by using these two commands
  //  ./gradlew test --tests PortfolioManagerApplicationTest.readTradesFromJson
  //  ./gradlew test --tests PortfolioManagerApplicationTest.mainReadFile
  public static List<PortfolioTrade> readTradesFromJson(String filename) throws IOException, URISyntaxException {
    File file = resolveFileFromResources(filename);
    ObjectMapper mapper = getObjectMapper();
    List<PortfolioTrade> tradeList = new ArrayList<>();
    PortfolioTrade[] trades = mapper.readValue(file, PortfolioTrade[].class);
    for(PortfolioTrade trade: trades){
      tradeList.add(trade);
    }
    return tradeList;
  }

  public static List<TiingoCandle> readTradesfromUrl(URL url) throws IOException{
    ObjectMapper mapper = getObjectMapper();
    List<TiingoCandle> tradeList = new ArrayList<>();
    TiingoCandle[] trades = mapper.readValue(url, TiingoCandle[].class);
    for(TiingoCandle trade: trades){
      tradeList.add(trade);
    }
    return tradeList;
  }


  // TODO:
  //  Build the Url using given parameters and use this function in your code to cann the API.
  public static String prepareUrl(PortfolioTrade trade, LocalDate endDate, String token) {
    
    String url = String.format("https://api.tiingo.com/tiingo/daily/%s/prices?startDate=%s&endDate=%s&token=%s", trade.getSymbol(), trade.getPurchaseDate(), endDate, token);

    return url;
  }
  // TODO:
  //  Ensure all tests are passing using below command
  //  ./gradlew test --tests ModuleThreeRefactorTest
  static Double getOpeningPriceOnStartDate(List<Candle> candles) {

    candles.sort((c1,c2)->c1.getDate().compareTo(c2.getDate()));
    return candles.get(0).getOpen();
  }


  public static Double getClosingPriceOnEndDate(List<Candle> candles) {
    candles.sort((c1,c2)->c1.getDate().compareTo(c2.getDate()));
    return candles.get(candles.size()-1).getClose();
  }


  public static List<Candle> fetchCandles(PortfolioTrade trade, LocalDate endDate, String token) {
    RestTemplate restTemplate = new RestTemplate();
    String url = String.format("https://api.tiingo.com/tiingo/daily/%s/prices?startDate=%s&endDate=%s&token=%s",trade.getSymbol(),trade.getPurchaseDate().toString(),endDate.toString(),token);
    TiingoCandle[] candles = restTemplate.getForObject(url,TiingoCandle[].class);
    return Arrays.asList(candles);
  }

  public static List<AnnualizedReturn> mainCalculateSingleReturn(String[] args)
      throws IOException, URISyntaxException {
        List<PortfolioTrade> trades = readTradesFromJson(args[0]);
        List<AnnualizedReturn> returns = new ArrayList<>();
        LocalDate endDate = LocalDate.parse(args[1]);
        String token = getToken();
        for(PortfolioTrade trade:trades){
          List<Candle> candles = fetchCandles(trade, endDate, token);
          double buyPrice = getOpeningPriceOnStartDate(candles);
          double sellPrice = getClosingPriceOnEndDate(candles);
          returns.add(calculateAnnualizedReturns(endDate, trade, buyPrice, sellPrice));
        }
        returns.sort((r1,r2)->r2.getAnnualizedReturn().compareTo(r1.getAnnualizedReturn()));
     return returns;
  }

  // TODO: CRIO_TASK_MODULE_CALCULATIONS
  //  Return the populated list of AnnualizedReturn for all stocks.
  //  Annualized returns should be calculated in two steps:
  //   1. Calculate totalReturn = (sell_value - buy_value) / buy_value.
  //      1.1 Store the same as totalReturns
  //   2. Calculate extrapolated annualized returns by scaling the same in years span.
  //      The formula is:
  //      annualized_returns = (1 + total_returns) ^ (1 / total_num_years) - 1
  //      2.1 Store the same as annualized_returns
  //  Test the same using below specified command. The build should be successful.
  //     ./gradlew test --tests PortfolioManagerApplicationTest.testCalculateAnnualizedReturn

  public static AnnualizedReturn calculateAnnualizedReturns(LocalDate endDate,
      PortfolioTrade trade, Double buyPrice, Double sellPrice) {
        double totalReturns = (sellPrice - buyPrice)/buyPrice;
        double years = (double) ChronoUnit.DAYS.between(trade.getPurchaseDate(), endDate) / 365.24;
        double annualized_returns = Math.pow((1+totalReturns),(1/years))-1;
      return new AnnualizedReturn(trade.getSymbol(), annualized_returns, totalReturns);
  }













  // TODO: CRIO_TASK_MODULE_REFACTOR
  //  Once you are done with the implementation inside PortfolioManagerImpl and
  //  PortfolioManagerFactory, create PortfolioManager using PortfolioManagerFactory.
  //  Refer to the code from previous modules to get the List<PortfolioTrades> and endDate, and
  //  call the newly implemented method in PortfolioManager to calculate the annualized returns.

  // Note:
  // Remember to confirm that you are getting same results for annualized returns as in Module 3.

  public static List<AnnualizedReturn> mainCalculateReturnsAfterRefactor(String[] args)
      throws Exception {
       PortfolioManager portfolioManager = PortfolioManagerFactory.getPortfolioManager("provider", new RestTemplate());
       File file = resolveFileFromResources(args[0]);
       LocalDate endDate = LocalDate.parse(args[1]);
       ObjectMapper objectMapper = getObjectMapper();
       PortfolioTrade[] portfolioTrades = objectMapper.readValue(file, PortfolioTrade[].class);
       return portfolioManager.calculateAnnualizedReturn(Arrays.asList(portfolioTrades), endDate);
  }


  public static void main(String[] args) throws Exception {
    Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
    ThreadContext.put("runId", UUID.randomUUID().toString());

    printJsonObject(mainReadFile(args));
    printJsonObject(mainReadQuotes(args));
    printJsonObject(mainCalculateSingleReturn(args));
    printJsonObject(mainCalculateReturnsAfterRefactor(args));
  }

  public static String getToken() {
    return "32843d0a9fae831c33806f51a3c4164c3b6b3372";
    // return "aec2485ca60ea5c7117e15cee7562a780688e060";
  }
}


