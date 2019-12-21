# CurrencyApp


<b>CONCERNS</B>


Should I always calculate values again when actually converting because those values might have been changed
* if user was afk more than 5 min with selected convertions

* or same User is logged on different devices and performing convertions at the same time(changes his balances irt)

Should I use service because user can close app in middle of convertion

Does number of currencies in api can change, then what do I to the user balances? Are those currencies still should be supported to the user?

Do I leave not supported currencies in balance?


Currency precision
* not sure if double is good enough(better than float atleast)

* best solution probably is to use BigDecimal class, but it is about 20x slower than binary types


What does it mean "picker"
* if picker was in literal sense then I should have used custom picker(like DatePicker, TimePicker) with dialog

* if not in literal sense, I think spinner is fine


Currencies API respones wasn't very convenient for ROOM/Sqlite(unless currency fields are static)
* converted response to map with moshi adapter

* couldn't create adapter to convert from object with objects to list with custom objects(containing type, value fields).



//Motion layout bugs()

FIX: dont use motion layout || update motion layout state when data is updated(slows down the performance)

BUGS:
1.NestedscrollView in motion layout  acts weird and makes regionId view unclickable, fast fix is too add clickableView on top. 

2.When I click on editTextt in the fragment it prevents updating my lists (recyclerView, and spinners items). Adapters data gets updated but UI does not.

*For recyclerView smoothScrollPosition helps if it is possible to scroll

*Losing edit text focus and hiding keyboard doesn't help

*Spinners onItemSelected method does not get triggered.

*EditText is bugged, when user typed text that goes beyond view bounds, ediText doesn't scroll.

*Swapping Motion layout to constraint layout completely fixes all problems.

*Changing constraintLayout to alpha5 helps, but in release motion layout not as smooth and it creates other bugs(tried versions from alpha5 to beta4).

*Without clicking ediText in the first place, everything works fine.
