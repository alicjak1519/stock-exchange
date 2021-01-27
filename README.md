# stock-exchange

Stock exchange simulation realised in the form of the multithreading app in Java.

<p align="center">
<img src="https://github.com/alicjak1519/stock-exchange/blob/main/assets/stock_exchange_demo.gif" width="400" height="333" />  
</p>

### App structure

A StockExchange consists of OrderSheets, one for each stockholder. 
Single OrderSheet contains **BlockingQueues** with SellOrders and BuyOrders sorted by the best price (the lowest first for buying orders, and the highest first for selling orders), and by creation date.

The main thread in the app creates StockExchange and generates an initial set of orders for each stockholder.  
Every OrderSheet is **ConcurrentMap** and has got its own **thread** with running MatchMaker.
Threads are managed by **ScheduledExecutorService**.
Matchmakers combine in parallel suitable orders into the transaction.

<p align="center">
<img src="https://github.com/alicjak1519/stock-exchange/blob/main/assets/stock_exchange_diagram.png"  />
</p>
