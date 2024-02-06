
package com.crio.warmup.stock.portfolio;

// import static java.time.temporal.ChronoUnit.DAYS;
// import static java.time.temporal.ChronoUnit.SECONDS;

import com.crio.warmup.stock.dto.AnnualizedReturn;
import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.dto.PortfolioTrade;
import com.crio.warmup.stock.dto.TiingoCandle;
import com.crio.warmup.stock.exception.StockQuoteServiceException;
import com.crio.warmup.stock.quotes.StockQuotesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
// import java.util.concurrent.ExecutionException;
// import java.util.concurrent.ExecutorService;
// import java.util.concurrent.Executors;
// import java.util.concurrent.Future;
// import java.util.concurrent.TimeUnit;
// import java.util.stream.Collectors;
import org.springframework.web.client.RestTemplate;

public class PortfolioManagerImpl implements PortfolioManager {
  RestTemplate restTemplate;
  StockQuotesService stockQuotesService;



  // Caution: Do not delete or modify the constructor, or else your build will break!
  // This is absolutely necessary for backward compatibility
  protected PortfolioManagerImpl(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }
  protected PortfolioManagerImpl(StockQuotesService stockQuotesService) {
    this.stockQuotesService = stockQuotesService;
   }


  // CRIO_TASK_MODULE_REFACTOR
  // 1. Now we want to convert our code into a module, so we will not call it from main anymore.
  //    Copy your code from Module#3 PortfolioManagerApplication#calculateAnnualizedReturn
  //    into #calculateAnnualizedReturn function here and ensure it follows the method signature.
  // 2. Logic to read Json file and convert them into Objects will not be required further as our
  //    clients will take care of it, going forward.

  // Note:
  // Make sure to exercise the tests inside PortfolioManagerTest using command below:
  // ./gradlew test --tests PortfolioManagerTest

  //CHECKSTYLE:OFF






  private Comparator<AnnualizedReturn> getComparator() {
    return Comparator.comparing(AnnualizedReturn::getAnnualizedReturn).reversed();
  }

  //CHECKSTYLE:OFF

  // TODO: CRIO_TASK_MODULE_REFACTOR
  //  Extract the logic to call Tiingo third-party APIs to a separate function.
  //  Remember to fill out the buildUri function and use that.


  public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to) throws StockQuoteServiceException{
      //  
      //   String uri = buildUri(symbol, from, to);
      //   try {
      //     TiingoCandle[] CandleList = restTemplate.getForObject(uri, TiingoCandle[].class);
      //     return Arrays.asList(CandleList); 
      //   } catch (Exception e) {
      //     //TODO: handle exception
      //     System.out.println(e);
      //     return Collections.emptyList();
      //   }
        try {
          return stockQuotesService.getStockQuote(symbol, from, to);
        } catch (JsonProcessingException e) {

          e.printStackTrace();
          return Collections.emptyList();
        }
        
  }

  protected String buildUri(String symbol, LocalDate startDate, LocalDate endDate) {
       return String.format("https://api.tiingo.com/tiingo/daily/%s/prices?startDate=%s&endDate=%s&token=%s",symbol,startDate.toString(),endDate.toString(),getToken());
  }

  private static String getToken(){
    return "32843d0a9fae831c33806f51a3c4164c3b6b3372";
    // return "aec2485ca60ea5c7117e15cee7562a780688e060";
  }

  static Double getOpeningPriceOnStartDate(List<Candle> candles) {

    candles.sort((c1,c2)->c1.getDate().compareTo(c2.getDate()));
    return candles.get(0).getOpen();
  }


  public static Double getClosingPriceOnEndDate(List<Candle> candles) {
    candles.sort((c1,c2)->c1.getDate().compareTo(c2.getDate()));
    return candles.get(candles.size()-1).getClose();
  }

  public static AnnualizedReturn calculateReturn(LocalDate endDate,
      PortfolioTrade trade, Double buyPrice, Double sellPrice) {
        double totalReturns = (sellPrice - buyPrice)/buyPrice;
        double years = (double) ChronoUnit.DAYS.between(trade.getPurchaseDate(), endDate) / 365.24;
        double annualized_returns = Math.pow((1+totalReturns),(1/years))-1;
      return new AnnualizedReturn(trade.getSymbol(), annualized_returns, totalReturns);
  }


  @Override
  public List<AnnualizedReturn> calculateAnnualizedReturn(List<PortfolioTrade> portfolioTrades,
      LocalDate endDate) throws StockQuoteServiceException  {
        List<AnnualizedReturn> returns = new ArrayList<>();
        for(PortfolioTrade trade:portfolioTrades){
          List<Candle> candles = getStockQuote(trade.getSymbol(),trade.getPurchaseDate(), endDate);
          double buyPrice = getOpeningPriceOnStartDate(candles);
          double sellPrice = getClosingPriceOnEndDate(candles);
          returns.add(calculateReturn(endDate, trade, buyPrice, sellPrice));
        }
        returns.sort(getComparator());
        // returns.sort((r1,r2)->r2.getAnnualizedReturn().compareTo(r1.getAnnualizedReturn()));
        return returns;
      }
  @Override
  public List<AnnualizedReturn> calculateAnnualizedReturnParallel(
      List<PortfolioTrade> portfolioTrades, LocalDate endDate, int numThreads)
      throws InterruptedException, StockQuoteServiceException {
    
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<AnnualizedReturn>> futures = new ArrayList<>();
        
        for(PortfolioTrade trade: portfolioTrades){
          Callable<AnnualizedReturn> task = () -> {
            List<Candle> candles = getStockQuote(trade.getSymbol(), trade.getPurchaseDate(), endDate);
            double buyPrice = getOpeningPriceOnStartDate(candles);
            double sellPrice = getClosingPriceOnEndDate(candles);
            return calculateReturn(endDate, trade, buyPrice, sellPrice);
          };
          futures.add(executor.submit(task));
         
        }

        //collect data
        List<AnnualizedReturn> returns = new ArrayList<>();
        for(Future<AnnualizedReturn> returnFuture: futures){
        
          try {
            returns.add(returnFuture.get());
          } catch (Exception e) {
            e.printStackTrace();
            throw new StockQuoteServiceException("Provider error");
            
          }
        }

        executor.shutdown();
        returns.sort(getComparator());
      return returns;
  }


  // ¶TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  //  Modify the function #getStockQuote and start delegating to calls to
  //  stockQuoteService provided via newly added constructor of the class.
  //  You also have a liberty to completely get rid of that function itself, however, make sure
  //  that you do not delete the #getStockQuote function.

}
